package server.websocket;

import com.google.gson.Gson;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private static final ConcurrentHashMap<String, ConnectionData> connections = new ConcurrentHashMap<>();

    public void notifyOthersInGame(int gameID, String rootUser, Notification notification) throws IOException {
        for (var connection : connections.values()) {
            if (connection.session().isOpen()) {
                if (!Objects.equals(connection.username(), rootUser)) {
                    String json = new Gson().toJson(notification);
                    connection.session().getRemote().sendString(json);
                }
            }
        }
    }

    public void add(String username, ConnectionData connection) {
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }
}
