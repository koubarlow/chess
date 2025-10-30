package model;

import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameData setId(int id) { return new GameData(id, this.whiteUsername, this.blackUsername, this.gameName, this.game); }
    public String toString() { return new Gson().toJson(this); }
}
