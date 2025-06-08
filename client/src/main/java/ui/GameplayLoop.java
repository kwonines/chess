package ui;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;

import java.util.Scanner;

public class GameplayLoop {
    public void run(String authToken, int gameID) throws Exception {
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        SocketFacade facade = new SocketFacade();
        facade.connect(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID));
        while (loop) {
            System.out.println("Enter a command");
            System.out.print(">");
            input = scanner.nextLine();
            switch (input) {
                case "help":
                    System.out.println("Enter one of the following commands: \n   redraw\n   leave\n   moveString\n   resign\n   highlight");
                    break;
                case "redraw":
                    Client.parseMessage(new Gson().toJson(new LoadGameMessage(null)));
                    break;
                case "leave":
                    facade.leave(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
                    loop = false;
                    continue;
                case "resign":
                    facade.resign(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
                    loop = false;
                    continue;
                case "move":
                    System.out.print("Enter a move you would like to make (e.g. a1c3)\n>");
                    String moveString = scanner.nextLine();
                    if (moveString.matches("^[a-h][1-8][a-h][1-8]$")) {
                        MakeMoveCommand command = getMoveCommand(authToken, gameID, moveString);
                        facade.makeMove(command);
                        Client.parseMessage(new Gson().toJson(new LoadGameMessage(null)));
                    } else {
                        System.out.println("The entered value is not a move. Please make sure you enter <START POSITION><END POSITION> (e.g. d3e4)");
                    }

            }
        }
    }

    private static MakeMoveCommand getMoveCommand(String authToken, int gameID, String moveString) {
        String coordinates = "";
        coordinates += moveString.charAt(0) - 96;
        coordinates += moveString.charAt(1);
        coordinates += moveString.charAt(2) - 96;
        coordinates += moveString.charAt(3);
        ChessPosition startPosition = new ChessPosition(Character.getNumericValue(coordinates.charAt(1)), Character.getNumericValue(coordinates.charAt(0)));
        ChessPosition endPosition = new ChessPosition(Character.getNumericValue(coordinates.charAt(3)), Character.getNumericValue(coordinates.charAt(2)));
        ChessMove move = new ChessMove(startPosition, endPosition);
        return new MakeMoveCommand(authToken, gameID, move);
    }
}
