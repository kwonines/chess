import model.GameData;

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
                    System.out.println("joining a game not yet implemented");
                    break;
                case "observe":
                    System.out.println("observing a game is not yet implemented");
                    break;
                default:
                    System.out.println("Unknown command please try again (or type \"help\" for list of available commands)");
                    break;
            }
            System.out.print(">");
            input = scanner.nextLine();
        }
    }
}