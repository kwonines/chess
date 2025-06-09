package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private static final ConcurrentHashMap<String, ConnectionData> CONNECTIONS = new ConcurrentHashMap<>();

    public void notify(int gameID, String rootUser, Notification notification) throws IOException {
        for (var connection : CONNECTIONS.values()) {
            if (connection.session().isOpen()) {
                if (!Objects.equals(connection.username(), rootUser) && connection.gameID() == gameID) {
                    String json = new Gson().toJson(notification);
                    connection.session().getRemote().sendString(json);
                }
            }
        }
    }

    public void sendBoard(int gameID, ChessGame game) throws IOException {
        for (var connection : CONNECTIONS.values()) {
            if (connection.session().isOpen()) {
                if (connection.gameID() == gameID) {
                    String json = new Gson().toJson(new LoadGameMessage(game));
                    connection.session().getRemote().sendString(json);
                }
            }
        }
    }

    public void add(String username, ConnectionData connection) {
        CONNECTIONS.put(username, connection);
    }

    public void remove(String username) {
        CONNECTIONS.remove(username);
    }
}
