package ui;

import websocket.commands.UserGameCommand;

import java.util.Scanner;

public class GameplayLoop {
    public void run(String authToken, int gameID) throws Exception {
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        SocketFacade facade = new SocketFacade();
        facade.connect(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID));
        while (loop) {
            input = scanner.nextLine();
            switch (input) {

            }
        }
    }
}
