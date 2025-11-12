package client;

import model.CreateGameRequest;
import model.LoginRequest;
import model.RegisterRequest;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static client.EscapeSequences.*;
import static client.EscapeSequences.GREEN;

public class ChessClient {

    private String visitorName = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(LOGO + " ♕ Welcome to chess. Sign in to start.");
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }

        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            server.register(new RegisterRequest(params[0], params[1], params[2]));
            return String.format("You registered and signed in as %s.", visitorName);
        }
        throw new Exception("Exception: <yourname>");
    }

    public String login(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            server.login(new LoginRequest(params[0], params[1]));
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception("Exception: <yourname>");
    }

    public String createGame(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            server.createGame(new CreateGameRequest(params[0]));
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception("Exception: <yourname>");
    }

    public String listGames(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            server.listGames();
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception("Exception: <yourname>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }
}
