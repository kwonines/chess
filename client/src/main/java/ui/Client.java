package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import websocket.messages.WSErrorMessage;

import java.util.*;

import static ui.EscapeSequences.*;


public final class Client {

    private static final ServerFacade server = new ServerFacade();
    private static ChessBoard latestBoard;

    public static void parseMessage(String jsonMessage) {
        Gson gson = new Gson();
        ServerMessage message = gson.fromJson(jsonMessage, ServerMessage.class);
        switch (message.getServerMessageType()) {
            case LOAD_GAME:
                LoadGameMessage loadMessage = gson.fromJson(jsonMessage, LoadGameMessage.class);
                if (loadMessage.game() == null) {
                    drawBoard(latestBoard, PlayerColor.getPlayerColor());
                } else {
                    latestBoard = loadMessage.game().getBoard();
                    drawBoard(latestBoard, PlayerColor.getPlayerColor());
                }
                break;
            case NOTIFICATION:
                Notification notification = gson.fromJson(jsonMessage, Notification.class);
                System.out.println(notification.message());
                break;
            case ERROR:
                WSErrorMessage errorMessage = gson.fromJson(jsonMessage, WSErrorMessage.class);
                System.out.println(errorMessage.errorMessage());
                break;
        }
    }

    public static String register(Scanner scanner) {
        System.out.println("Enter a username to register:");
        String regUsername = scanner.nextLine();
        System.out.println("Enter a password:");
        String regPassword = scanner.nextLine();
        System.out.println("Enter an email address:");
        String email = scanner.nextLine();
        try {
            var result = server.register(regUsername, regPassword, email);
            System.out.println("Register Success! You are now logged in as " + result.username());
            return result.authToken();
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public static String login(Scanner scanner) {
        System.out.println("Enter username:");
        String logUsername = scanner.nextLine();
        System.out.println("Enter password:");
        String logPassword = scanner.nextLine();
        try {
            var result = server.login(logUsername, logPassword);
            System.out.println("Logged in as " + result.username());
            return result.authToken();
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public static boolean logout(String authToken) {
        try {
            server.logout(authToken);
            System.out.println("Logged out");
            return true;
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public static void listGames(String authToken, HashMap<Integer, GameData> games) {
        try {
            ArrayList<GameData> result = server.listGames(authToken).games();
            games.clear();
            if (result.isEmpty()) {
                System.out.println("No games to show");
            } else {
                for (int i = 0; i < result.size(); i++) {
                    System.out.println((i + 1) + ":");
                    System.out.println("   Game Name: " + result.get(i).gameName());
                    if (result.get(i).whiteUsername() == null) {
                        System.out.println("   White Player: none");
                    } else {
                        System.out.println("   White Player: " + result.get(i).whiteUsername());
                    }
                    if (result.get(i).blackUsername() == null) {
                        System.out.println("   Black Player: none");
                    } else {
                        System.out.println("   Black Player: " + result.get(i).blackUsername());
                    }
                    games.put(i + 1, result.get(i));
                }
            }
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void createGame(String authToken, Scanner scanner) {
        System.out.println("Please enter a name for your game:");
        String gameName = scanner.nextLine();
        try {
            server.createGame(authToken, gameName);
            System.out.println("Game created");
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void joinGame(String authToken, HashMap<Integer, GameData> games, Scanner scanner) {
        try {
            ArrayList<GameData> result = server.listGames(authToken).games();
            games.clear();
            if (result.isEmpty()) {
                System.out.println("There are no games available to join");
            } else {
                for (int i = 0; i < result.size(); i++) {
                    games.put(i + 1, result.get(i));
                }
                System.out.println("Enter the number for the game you would like to join (e.g. 2)");
                int gameNumber;
                try {
                    gameNumber = scanner.nextInt();
                } catch (InputMismatchException exception) {
                    System.out.println("Incorrect input type. Please try joining again");
                    return;
                } finally {
                    scanner.nextLine();
                }
                if (games.get(gameNumber) == null) {
                    System.out.println("Game does not exist, please try joining again");
                    return;
                }
                System.out.println("Enter (w)hite to join as white, or (b)lack to join as black");
                String stringColor = scanner.nextLine();
                if (Objects.equals(stringColor, "b") || Objects.equals(stringColor, "black")) {
                    PlayerColor.setPlayerColor(ChessGame.TeamColor.BLACK);
                    server.joinGame(ChessGame.TeamColor.BLACK, games.get(gameNumber).gameID(), authToken);
                } else if (Objects.equals(stringColor, "w") || Objects.equals(stringColor, "white")) {
                    PlayerColor.setPlayerColor(ChessGame.TeamColor.WHITE);
                    server.joinGame(ChessGame.TeamColor.WHITE, games.get(gameNumber).gameID(), authToken);
                } else {
                    System.out.println("Error: incorrect color input. Please try again");
                }
            }
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void observeGame(String authToken, HashMap<Integer, GameData> games, Scanner scanner) {
        try {
            ArrayList<GameData> result = server.listGames(authToken).games();
            games.clear();
            if (result.isEmpty()) {
                System.out.println("There are no games available to observe");
            } else {
                for (int i = 0; i < result.size(); i++) {
                    games.put(i + 1, result.get(i));
                }
                System.out.println("Enter the number for the game you would like to observe (e.g. 2)");
                int gameNumber;
                try {
                    gameNumber = scanner.nextInt();
                } catch (InputMismatchException exception) {
                    System.out.println("Incorrect input type, must be a number (e.g. 5) Please try to observe again");
                    return;
                } finally {
                    scanner.nextLine();
                }
                if (games.get(gameNumber) == null) {
                    System.out.println("Game does not exist, please try to observe again");
                    return;
                }
                drawBoard(games.get(gameNumber).game().getBoard(), ChessGame.TeamColor.WHITE);
            }
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void drawBoard(ChessBoard board, ChessGame.TeamColor color) {
        char[] columns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        if (color == ChessGame.TeamColor.WHITE) {
            System.out.print(EMPTY);
            for (int i = 0; i < 8; i++) {
                System.out.print(FRONT_SPACER + columns[i] + END_SPACER);
            }
            System.out.println();
            for (int row = 1; row < 9; row++) {
                System.out.print(" " + (9 - row) + " ");
                for (int col = 1; col < 9; col++) {
                    printSquare(board, row, col);
                }
                System.out.print(RESET_BG_COLOR + " " + (9 - row) + " ");
                System.out.println();
            }
            System.out.print(EMPTY);
            for (int i = 0; i < 8; i++) {
                System.out.print(FRONT_SPACER + columns[i] + END_SPACER);
            }
            System.out.println();
        } else {
            System.out.print(EMPTY);
            for (int i = 7; i >= 0; i--) {
                System.out.print(FRONT_SPACER + columns[i] + END_SPACER);
            }
            System.out.println();
            for (int row = 8; row > 0; row--) {
                System.out.print(" " + (9 - row) + " ");
                for (int col = 8; col > 0; col--) {
                    printSquare(board, row, col);
                }
                System.out.print(RESET_BG_COLOR + " " + (9 - row) + " ");
                System.out.println();
            }
            System.out.print(EMPTY);
            for (int i = 7; i >= 0; i--) {
                System.out.print(FRONT_SPACER + columns[i] + END_SPACER);
            }
            System.out.println();
        }
    }

    private static void printSquare(ChessBoard board, int row, int col) {
        if (row % 2 == 1 && col % 2 == 1) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        } else if (row % 2 == 1) {
            System.out.print(SET_BG_COLOR_DARK_GREY);
        } else if (col % 2 == 1) {
            System.out.print(SET_BG_COLOR_DARK_GREY);
        } else {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            System.out.print(EMPTY);
        } else {
            System.out.print(piece);
        }
    }
}
