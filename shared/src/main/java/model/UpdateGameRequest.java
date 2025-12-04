package model;

import chess.ChessGame;

public record UpdateGameRequest(ChessGame.TeamColor playerColor, Integer gameID, String username, GameData gameData) {
}
