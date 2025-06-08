package ui;

import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

public class SocketFacade extends Endpoint {

    public Session session;

    public SocketFacade() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String msg) {
                Client.parseMessage(msg);
            }
        });
    }

    public void connect(UserGameCommand command) throws Exception {
        String msg = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(msg);
    }

    public void leave(UserGameCommand command) throws Exception {
        String msg = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(msg);
    }

    public void resign(UserGameCommand command) throws Exception {
        String msg = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(msg);
    }

    public void makeMove(MakeMoveCommand command) throws Exception {
        String msg = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

