import model.GameData;
import ui.Client;

import java.util.*;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Welcome to Chess! Enter a command to begin:\n   register\n   login\n   quit\n   help\n>");
        String input = scanner.nextLine();

        while (!Objects.equals(input, "quit")) {
            String authToken;
            switch (input) {
                case "register":
                    authToken = Client.register(scanner);
                    if (authToken != null) {
                        System.out.print(">");
                        startLoggedInLoop(authToken);
                    }
                    break;
                case "login":
                    authToken = Client.login(scanner);
                    if (authToken != null) {
                        System.out.print(">");
                        startLoggedInLoop(authToken);
                    }
                    break;
                case "help":
                    System.out.println("Type any of the following commands to use them:\n   register\n   login\n   quit\n  help");
                    break;
                default:
                    System.out.println("Unknown command, please try again (or type \"help\" for list of available commands)");
            }
            System.out.print("Enter a command:\n>");
            input = scanner.nextLine();
        }

        scanner.close();
    }

    private static void startLoggedInLoop(String authToken) throws Exception {
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
                    if (Client.logout(authToken)) {
                        loop = false;
                        continue;
                    }
                    break;
                case "list":
                    Client.listGames(authToken, games);
                    break;
                case "create":
                    Client.createGame(authToken, scanner);
                    break;
                case "join":
                    Client.joinGame(authToken, games, scanner);
                    break;
                case "observe":
                    Client.observeGame(authToken, games, scanner);
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
}