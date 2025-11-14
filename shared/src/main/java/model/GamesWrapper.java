package model;

import java.util.List;

public class GamesWrapper {
    List<GameData> games;

    public GamesWrapper(List<GameData> games) {
        this.games = games;
    }

    public List<GameData> games() {
        return this.games;
    }
}
