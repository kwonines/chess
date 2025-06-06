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
                    case CONNECT -> connect(session, username, command);
                    case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                }
            }
        } catch (ServerErrorException | IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, ServerErrorException {
        connections.add(username, new ConnectionData(username, command.getGameID(), session));
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

        connections.notify(command.getGameID(), username, notification);
        ChessGame game = gameDataAccess.findGame(command.getGameID()).game();
        session.getRemote().sendString(gson.toJson(new LoadGameMessage(game)));
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) {
        try {
            ChessGame game = gameDataAccess.findGame(command.getGameID()).game();
            game.makeMove(command.getMove());
        } catch (ServerErrorException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {

        }
    }
}
