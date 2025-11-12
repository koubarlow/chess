package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static client.EscapeSequences.*;
import static client.EscapeSequences.GREEN;

public class ChessClient {

    private String username = null;
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
                case "list" -> listGames();
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
            this.username = params[0];
            String password = params[1];
            String email = params[2];
            server.register(new RegisterRequest(username, password, email));
            return String.format("You registered and signed in as %s.", username);
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws Exception {
        if (params.length >= 1) {
            this.username = params[0];
            String password = params[1];
            server.login(new LoginRequest(username, password));
            return String.format("You signed in as %s.", username);
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD>");
    }

    public String createGame(String... params) throws Exception {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(new CreateGameRequest(gameName));
            return String.format("You created a game named: %s.", gameName);
        }
        throw new Exception("Expected: <NAME>");
    }

    public String listGames() throws Exception {
        assertSignedIn();
        GameList gameList = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (GameData game : gameList) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws Exception {
        if (params.length >= 1) {
            ChessGame.TeamColor teamColor = null;
            
            int gameId = Integer.parseInt(params[0]);
            if (Objects.equals(params[1], "white")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }
            server.joinGame(new JoinGameRequest(teamColor, gameId, username));
            return String.format("You joined game %s as %s.", gameId, teamColor);
        }
        throw new Exception("Exception: <ID> <WHITE|BLACK>");
    }

    public String logout(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDOUT;
            server.logout();
            return String.format("You signed out. Thank you for playing, %s.", username);
            this.username = null;
        }
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

    private void assertSignedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }
}
