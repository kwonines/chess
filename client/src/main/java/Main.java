import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import static ui.EscapeSequences.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ServerFacade server = new ServerFacade();
        Scanner scanner = new Scanner(System.in);


        System.out.print("Welcome to Chess! Enter a command to begin:\n   register\n   login\n   quit\n>");

        String input = scanner.nextLine();

        while (!Objects.equals(input, "quit")) {
            String authToken;
            switch (input) {
                case "register":
                    System.out.println("Enter a username to register:");
                    String regUsername = scanner.nextLine();
                    System.out.println("Enter a password:");
                    String regPassword = scanner.nextLine();
                    System.out.println("Enter an email address:");
                    String email = scanner.nextLine();
                    try {
                        var result = server.register(regUsername, regPassword, email);
                        authToken = result.authToken();
                        System.out.print("Register Success! You are now logged in as " + result.username() + "\n>");
                        startLoggedInLoop(server, authToken);
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case "login":
                    System.out.println("Enter username:");
                    String logUsername = scanner.nextLine();
                    System.out.println("Enter password:");
                    String logPassword = scanner.nextLine();
                    try {
                        var result = server.login(logUsername, logPassword);
                        authToken = result.authToken();
                        System.out.print("Logged in as " + result.username() + "\n>");
                        startLoggedInLoop(server, authToken);
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case "help":
                    System.out.println("Type any of the following commands to use them:\n   register\n   login\n   quit");
                    break;
                default:
                    System.out.println("Unknown command, please try again (or type \"help\" for list of available commands)");
            }
            System.out.print("Enter a command:\n>");
            input = scanner.nextLine();
        }
    }

    private static void startLoggedInLoop(ServerFacade server, String authToken) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        HashMap<Integer, GameData> games = new HashMap<>();
        boolean loop = true;
        while (loop) {
            switch (input) {
                case "help":
                    System.out.println("""
                            Type any of the following commands to use them:\
                            
                               logout
                               create
                               list
                               join
                               observe""");
                    break;
                case "logout":
                    try {
                        server.logout(authToken);
                        System.out.println("Logged out");
                        loop = false;
                        continue;
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case "list":
                    try {
                        ArrayList<GameData> result = server.listGames(authToken).games();
                        games.clear();
                        if (result.isEmpty()) {
                            System.out.println("No games to show");
                        } else {
                            for (int i = 0; i < result.size(); i++) {
                                System.out.println((i + 1) + ":");
                                System.out.println("   Game Name: " + result.get(i).gameName());
                                System.out.println("   White Player: " + result.get(i).whiteUsername());
                                System.out.println("   Black Player: " + result.get(i).blackUsername());
                                games.put(i + 1, result.get(i));
                            }
                        }
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case "create":
                    System.out.println("Please enter a name for your game:");
                    String gameName = scanner.nextLine();
                    try {
                        server.createGame(authToken, gameName);
                        System.out.println("Game created");
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case "join":
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
                                break;
                            } finally {
                                scanner.nextLine();
                            }
                            if (games.get(gameNumber) == null) {
                                System.out.println("Game does not exist, please try joining again");
                                break;
                            }
                            System.out.println("Enter (w)hite to join as white, or (b)lack to join as black");
                            String stringColor = scanner.nextLine();
                            if (Objects.equals(stringColor, "w") || Objects.equals(stringColor, "white")) {
                                server.joinGame(ChessGame.TeamColor.WHITE, games.get(gameNumber).gameID(), authToken);
                                System.out.println("Successfully joined game " + gameNumber + " as white player");
                                drawBoard(games.get(gameNumber).game().getBoard(), ChessGame.TeamColor.WHITE);
                            } else if (Objects.equals(stringColor, "b") || Objects.equals(stringColor, "black")) {
                                server.joinGame(ChessGame.TeamColor.BLACK, games.get(gameNumber).gameID(), authToken);
                                System.out.println("Successfully joined game " + gameNumber + " as black player");
                                drawBoard(games.get(gameNumber).game().getBoard(), ChessGame.TeamColor.BLACK);
                            } else {
                                System.out.println("Error: incorrect color input. Please try again");
                            }
                        }
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case "observe":
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
                                break;
                            } finally {
                                scanner.nextLine();
                            }
                            if (games.get(gameNumber) == null) {
                                System.out.println("Game does not exist, please try to observe again");
                                break;
                            }
                                drawBoard(games.get(gameNumber).game().getBoard(), ChessGame.TeamColor.WHITE);
                        }
                    } catch (ResponseException exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                default:
                    System.out.println("Unknown command please try again (or type \"help\" for list of available commands)");
                    break;
            }
            System.out.println("Enter a command:");
            System.out.print(">");
            input = scanner.nextLine();
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
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            printPiece(piece, WHITE_BISHOP, WHITE_KING, WHITE_KNIGHT, WHITE_PAWN, WHITE_QUEEN, WHITE_ROOK);
        } else {
            printPiece(piece, BLACK_BISHOP, BLACK_KING, BLACK_KNIGHT, BLACK_PAWN, BLACK_QUEEN, BLACK_ROOK);
        }
    }

    private static void printPiece(ChessPiece piece, String bishop, String king, String knight, String pawn, String queen, String rook) {
        switch (piece.getPieceType()) {
            case BISHOP -> System.out.print(bishop);
            case KING -> System.out.print(king);
            case KNIGHT -> System.out.print(knight);
            case PAWN -> System.out.print(pawn);
            case QUEEN -> System.out.print(queen);
            case ROOK -> System.out.print(rook);
        }
    }
}