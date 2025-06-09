package ui;

import chess.*;
import com.google.gson.Gson;
import model.GameData;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import websocket.messages.WSErrorMessage;

import java.util.*;

import static ui.EscapeSequences.*;


public final class Client {

    private static final ServerFacade SERVER = new ServerFacade();
    private static ChessGame latestGame;

    public static void parseMessage(String jsonMessage) {
        Gson gson = new Gson();
        ServerMessage message = gson.fromJson(jsonMessage, ServerMessage.class);
        switch (message.getServerMessageType()) {
            case LOAD_GAME:
                LoadGameMessage loadMessage = gson.fromJson(jsonMessage, LoadGameMessage.class);
                System.out.println();
                if (loadMessage.game() == null) {
                    drawBoard();
                } else {
                    latestGame = loadMessage.game();
                    drawBoard();
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
        System.out.print(">");
    }

    public static boolean checkForPromotion(ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> validMoves = latestGame.validMoves(startPosition);
        ChessMove promotionMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN);
        if (validMoves != null) {
            return validMoves.contains(promotionMove);
        } else {
            return false;
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
            var result = SERVER.register(regUsername, regPassword, email);
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
            var result = SERVER.login(logUsername, logPassword);
            System.out.println("Logged in as " + result.username());
            return result.authToken();
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public static boolean logout(String authToken) {
        try {
            SERVER.logout(authToken);
            System.out.println("Logged out");
            return true;
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public static void listGames(String authToken, HashMap<Integer, GameData> games) {
        try {
            ArrayList<GameData> result = SERVER.listGames(authToken).games();
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
            SERVER.createGame(authToken, gameName);
            System.out.println("Game created");
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void joinGame(String authToken, HashMap<Integer, GameData> games, Scanner scanner) {
        try {
            ArrayList<GameData> result = SERVER.listGames(authToken).games();
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
                    SERVER.joinGame(ChessGame.TeamColor.BLACK, games.get(gameNumber).gameID(), authToken);
                    new GameplayLoop().run(authToken, games.get(gameNumber).gameID());
                } else if (Objects.equals(stringColor, "w") || Objects.equals(stringColor, "white")) {
                    PlayerColor.setPlayerColor(ChessGame.TeamColor.WHITE);
                    SERVER.joinGame(ChessGame.TeamColor.WHITE, games.get(gameNumber).gameID(), authToken);
                    new GameplayLoop().run(authToken, games.get(gameNumber).gameID());
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
            ArrayList<GameData> result = SERVER.listGames(authToken).games();
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
                PlayerColor.setPlayerColor(ChessGame.TeamColor.WHITE);
                new ObserveLoop().run(authToken, games.get(gameNumber).gameID());
            }
        } catch (ResponseException exception) {
            System.out.println(exception.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void drawBoard() {
        ChessGame.TeamColor color = PlayerColor.getPlayerColor();
        ChessBoard board = latestGame.getBoard();
        char[] columns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        if (color == ChessGame.TeamColor.WHITE) {
            drawWhiteColumnLetters(columns);
            for (int row = 8; row > 0; row--) {
                System.out.print(" " + (row) + " ");
                for (int col = 1; col < 9; col++) {
                    printNormal(board, row, col);
                }
                System.out.print(RESET_BG_COLOR + " " + (row) + " ");
                System.out.println();
            }
            drawWhiteColumnLetters(columns);
        } else {
            drawBlackColumnLetters(columns);
            for (int row = 1; row < 9; row++) {
                System.out.print(" " + (row) + " ");
                for (int col = 8; col > 0; col--) {
                    printNormal(board, row, col);
                }
                System.out.print(RESET_BG_COLOR + " " + (row) + " ");
                System.out.println();
            }
            drawBlackColumnLetters(columns);
        }
    }

    private static void drawBlackColumnLetters(char[] columns) {
        System.out.print(EMPTY);
        for (int i = 7; i >= 0; i--) {
            System.out.print(FRONT_SPACER + columns[i] + END_SPACER);
        }
        System.out.println();
    }

    private static void drawWhiteColumnLetters(char[] columns) {
        System.out.print(EMPTY);
        for (int i = 0; i < 8; i++) {
            System.out.print(FRONT_SPACER + columns[i] + END_SPACER);
        }
        System.out.println();
    }

    public static void highlightMoves(ChessPosition startPosition) {
        ChessGame.TeamColor color = PlayerColor.getPlayerColor();
        Collection<ChessMove> validMoves = latestGame.validMoves(startPosition);
        ChessBoard board = latestGame.getBoard();
        if (board.getPiece(startPosition) == null) {
            System.out.println("There is no piece at this position");
            System.out.print(">");
            return;
        }
        if (validMoves.isEmpty()) {
            System.out.println("This piece has no valid moves");
            System.out.print(">");
            return;
        }
        char[] columns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        if (color == ChessGame.TeamColor.WHITE) {
            drawWhiteColumnLetters(columns);
            for (int row = 8; row > 0; row--) {
                System.out.print(" " + (row) + " ");
                for (int col = 1; col < 9; col++) {
                    printSquareType(startPosition, validMoves, board, row, col);
                }
                System.out.print(RESET_BG_COLOR + " " + (row) + " ");
                System.out.println();
            }
            drawWhiteColumnLetters(columns);
        } else {
            drawBlackColumnLetters(columns);
            for (int row = 1; row < 9; row++) {
                System.out.print(" " + (row) + " ");
                for (int col = 8; col > 0; col--) {
                    printSquareType(startPosition, validMoves, board, row, col);
                }
                System.out.print(RESET_BG_COLOR + " " + (row) + " ");
                System.out.println();
            }
            drawBlackColumnLetters(columns);
        }
        System.out.print(">");
    }

    private static void printSquareType(ChessPosition startPosition, Collection<ChessMove> validMoves, ChessBoard board, int row, int col) {
        ChessPosition endPosition;
        ChessMove move;
        ChessMove promotionMove;
        endPosition = new ChessPosition(row, col);
        move = new ChessMove(startPosition, endPosition);
        promotionMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN);
        if (validMoves.contains(move) || validMoves.contains(promotionMove)) {
            printHighlight(board, row, col);
        } else {
            printNormal(board, row, col);
        }
    }

    private static void printNormal(ChessBoard board, int row, int col) {
        printSquare(board, row, col, SET_BG_COLOR_DARK_GREY, SET_BG_COLOR_LIGHT_GREY);
    }

    private static void printHighlight(ChessBoard board, int row, int col) {
        printSquare(board, row, col, SET_BG_COLOR_DARK_GREEN, SET_BG_COLOR_GREEN);
    }

    private static void printSquare(ChessBoard board, int row, int col, String setBgColorDarkGreen, String setBgColorGreen) {
        if (row % 2 == 1 && col % 2 == 1) {
            System.out.print(setBgColorDarkGreen);
        } else if (row % 2 == 1) {
            System.out.print(setBgColorGreen);
        } else if (col % 2 == 1) {
            System.out.print(setBgColorGreen);
        } else {
            System.out.print(setBgColorDarkGreen);
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            System.out.print(EMPTY);
        } else {
            System.out.print(piece);
        }
    }
}
