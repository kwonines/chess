import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Chess! Enter a command to begin:\n   register\n   login\n   quit");

        String input = scanner.nextLine();

        while (!Objects.equals(input, "quit")) {
            switch (input) {
                case "register":
                    System.out.println("register hasn't been implemented");
                    break;
                case "login":
                    System.out.println("login hasn't been implemented");
                    startLoggedInLoop();
                    break;
                case "help":
                    System.out.println("Type any of the following commands to use them:\n   register\n   login\n   quit");
                    break;
                default:
                    System.out.println("Unknown command, please try again (or type \"help\" for list of available commands)");
            }
            System.out.println("Enter a command:");
            input = scanner.nextLine();
        }
    }

    private static void startLoggedInLoop() {
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
                    System.out.println("logout is not implemented, exiting loop");
                    loop = false;
                    continue;
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