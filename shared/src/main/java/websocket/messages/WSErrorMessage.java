package websocket.messages;

public class WSErrorMessage extends ServerMessage {

    private final String errorMessage;

    public WSErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
