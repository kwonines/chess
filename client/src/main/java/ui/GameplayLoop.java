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
        Thread.sleep(50);
        while (loop) {
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
                    System.out.println("You have left the game");
                    loop = false;
                    continue;
                case "resign":
                    facade.resign(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
                    break;
                case "move":
                    System.out.print("Enter a move you would like to make (e.g. a1c3)\n>");
                    String moveString = scanner.nextLine();
                    if (moveString.matches("^[a-h][1-8][a-h][1-8]$")) {
                        MakeMoveCommand command = getMoveCommand(authToken, gameID, moveString);
                        facade.makeMove(command);
                        Thread.sleep(50);
                    } else {
                        System.out.println("The entered value is not a move. Please make sure you enter <START POSITION><END POSITION> (e.g. d3e4)");
                    }
                    break;
                case "highlight":
                    highlight(scanner);
                    break;
                default:
                    System.out.println("Unknown command. Please try again or type \"help\" for list of commands");
                    System.out.print(">");
            }
        }
    }

    public static void highlight(Scanner scanner) {
        System.out.println("Enter the position for a piece you would like moves highlighted for (e.g. b7)");
        String coordinate = scanner.nextLine();
        if (coordinate.matches("^[a-h][1-8]$")) {
            ChessPosition position = convertToPosition(coordinate);
            Client.highlightMoves(position);
        } else {
            System.out.println("The entered value is not a valid coordinate (e.g. b7 or a3). Pleas try again");
        }
    }

    private static MakeMoveCommand getMoveCommand(String authToken, int gameID, String moveString) {
        String coordinates = "";
        coordinates += moveString.charAt(0) - 96;
        coordinates += moveString.charAt(1);
        coordinates += moveString.charAt(2) - 96;
        coordinates += moveString.charAt(3);
        ChessPosition startPosition =
                new ChessPosition(Character.getNumericValue(coordinates.charAt(1)), Character.getNumericValue(coordinates.charAt(0)));
        ChessPosition endPosition =
                new ChessPosition(Character.getNumericValue(coordinates.charAt(3)), Character.getNumericValue(coordinates.charAt(2)));
        ChessMove move = new ChessMove(startPosition, endPosition);
        return new MakeMoveCommand(authToken, gameID, move);
    }

    private static ChessPosition convertToPosition(String coordinate) {
        String coordinates = "";
        coordinates += coordinate.charAt(0) - 96;
        coordinates += coordinate.charAt(1);
        return new ChessPosition(Character.getNumericValue(coordinates.charAt(1)), Character.getNumericValue(coordinates.charAt(0)));
    }
}
