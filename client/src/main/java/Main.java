import java.util.Objects;
import java.util.Scanner;

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
                        System.out.println("Register Success! You are now logged in as " + result.username());
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
                        System.out.println("Logged in as " + result.username());
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
                case "create":
                    System.out.println("creating a game not yet implemented");
                    break;
                case "list":
                    System.out.println("listing games not yet implemented");
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
            input = scanner.nextLine();
        }
    }
}