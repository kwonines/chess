package websocket.commands;

public class JoinCommand extends UserGameCommand {

    private final ConnectType connectType;

    public JoinCommand(String authToken, Integer gameID, ConnectType connectType) {
        super(CommandType.CONNECT, authToken, gameID);
        this.connectType = connectType;
    }

    public enum ConnectType {
        BLACK, WHITE, OBSERVER
    }

    public ConnectType connectType() {
        return connectType;
    }
}
