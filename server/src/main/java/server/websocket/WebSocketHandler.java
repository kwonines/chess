package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.exceptions.ServerErrorException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;
import dataaccess.*;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    Gson gson = new Gson();
    ConnectionManager connections = new ConnectionManager();
    GameDataAccess gameDataAccess = new SQLGameDataAccess();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        AuthDataAccess dataAccess = new SQLAuthDataAccess();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

        try {
            AuthData auth = dataAccess.findAuth(command.getAuthToken());
            if (auth == null) {
                session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: unauthorized")));
            } else {
                String username = auth.username();
                switch (command.getCommandType()) {
                    case CONNECT:
                        connect(session, username, command);
                        break;
                    case MAKE_MOVE:
                        command = gson.fromJson(message, MakeMoveCommand.class);
                        makeMove(session, username, (MakeMoveCommand) command);
                        break;
                    case LEAVE:
                        leave(username, command);
                        break;
                    case RESIGN:
                        resign(session, username, command);
                        break;
                }
            }
        } catch (ServerErrorException | IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, ServerErrorException {
        GameData gameData = gameDataAccess.findGame(command.getGameID());
        if (gameData == null) {
            session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: game does not exist")));
            return;
        }

        Notification notification;
        if (gameData.whiteUsername().equals(username)) {
            notification = new Notification(username + " has joined the game as white");
        } else if (gameData.blackUsername().equals(username)) {
            notification = new Notification(username + " has joined the game as black");
        } else {
            notification = new Notification(username + " is observing the game");
        }

        connections.add(username, new ConnectionData(username, command.getGameID(), session));
        connections.notify(command.getGameID(), username, notification);
        ChessGame game = gameDataAccess.findGame(command.getGameID()).game();
        session.getRemote().sendString(gson.toJson(new LoadGameMessage(game)));
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws IOException, ServerErrorException {
        try {
            GameData gameData = gameDataAccess.findGame(command.getGameID());
            ChessGame.TeamColor userColor;
            if (username.equals(gameData.whiteUsername())) {
                userColor = ChessGame.TeamColor.WHITE;
            } if (username.equals(gameData.blackUsername())) {
                userColor = ChessGame.TeamColor.BLACK;
            } else {
                session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: cannot make moves as an observer")));
                return;
            }

            ChessGame game = gameData.game();

            if (game.getTeamTurn() != userColor) {
                session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: not your turn")));
                return;
            }

            if (game.isGameOver()) {
                session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: game is over, cannot make moves")));
                return;
            }

            game.makeMove(command.getMove());

            GameData updatedGame = new GameData(command.getGameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameDataAccess.updateGame(command.getGameID(), updatedGame);

            connections.notify(command.getGameID(), username, new Notification(username + " made move: " + command.getMove().toString()));
            connections.sendBoard(command.getGameID(), game);
        } catch (InvalidMoveException e) {
            session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: invalid move")));
        }
    }

    private void leave(String username, UserGameCommand command) throws ServerErrorException, IOException {
        GameData oldGame = gameDataAccess.findGame(command.getGameID());
        if (username.equals(oldGame.whiteUsername())) {
            gameDataAccess.updateGame(command.getGameID(),
                    new GameData(oldGame.gameID(), null, oldGame.blackUsername(), oldGame.gameName(), oldGame.game()));
        } else if (username.equals(oldGame.blackUsername())) {
            gameDataAccess.updateGame(command.getGameID(),
                    new GameData(oldGame.gameID(), oldGame.whiteUsername(), null, oldGame.gameName(), oldGame.game()));
        }
        connections.notify(command.getGameID(), username, new Notification(username + " has left the game"));
        connections.remove(username);
    }

    private void resign(Session session, String username, UserGameCommand command) throws ServerErrorException, IOException {
        GameData gameData = gameDataAccess.findGame(command.getGameID());

        if (!(username.equals(gameData.whiteUsername())) && !(username.equals(gameData.blackUsername()))) {
            session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: cannot make moves as an observer")));
            return;
        }

        ChessGame game = gameData.game();

        if (game.isGameOver()) {
            session.getRemote().sendString(gson.toJson(new WSErrorMessage("Error: game is already over")));
            return;
        }
        game.end();

        gameDataAccess.updateGame(command.getGameID(), new GameData(command.getGameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));
        connections.notify(command.getGameID(), username, new Notification(username + " has resigned"));
        session.getRemote().sendString(gson.toJson(new Notification("You have resigned")));
    }
}
