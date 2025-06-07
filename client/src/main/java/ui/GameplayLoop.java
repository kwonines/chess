package ui;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class GameplayLoop {
    public void run(String authToken, int gameID) throws Exception {
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        SocketFacade facade = new SocketFacade();
        facade.connect(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID));
        while (loop) {
            System.out.print(">");
            input = scanner.nextLine();
            switch (input) {
                case "help":
                    System.out.println("Enter one of the following commands: \n   redraw\n   leave\n   move\n   resign\n   highlight");
                    break;
                case "redraw":
                    Client.messageParser(new Gson().toJson(new LoadGameMessage(null)));
            }
        }
    }
}
