package ui;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;

import java.util.Scanner;

public class ObserveLoop {
    public void run(String authToken, int gameID) throws Exception {
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        SocketFacade facade = new SocketFacade();
        facade.connect(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID));
        while (loop) {
            input = scanner.nextLine();
            switch (input) {
                case "help":
                    System.out.println("Enter one of the following commands: \n   redraw\n   leave\n   highlight");
                    break;
                case "redraw":
                    Client.parseMessage(new Gson().toJson(new LoadGameMessage(null)));
                    break;
                case "leave":
                    facade.leave(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
                    System.out.println("You have left the game");
                    loop = false;
                    continue;
                case "highlight":
                    GameplayLoop.highlight(scanner);
                    break;
                default:
                    System.out.println("Unknown command. Please try again or type \"help\" for list of commands");
                    System.out.print(">");
            }
        }
    }
}
