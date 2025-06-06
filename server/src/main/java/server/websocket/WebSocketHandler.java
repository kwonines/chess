package server.websocket;


import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLGameDataAccess;
import dataaccess.exceptions.ServerErrorException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.JoinCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

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
            String username = dataAccess.findAuth(command.getAuthToken()).username();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, /*(JoinCommand)*/ command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
            }

        } catch (ServerErrorException | IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, ServerErrorException {
        connections.add(username, new ConnectionData(username, command.getGameID(), session));
        Notification notification = new Notification(username + " has joined the game as "/* + command.connectType()*/);
        connections.notifyOthersInGame(command.getGameID(), username, notification);
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
