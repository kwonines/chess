package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

public record ConnectionData(String username, int gameID, Session session) {}
