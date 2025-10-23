package model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GameList extends ArrayList<GameData> {
    public GameList() {

    }
    public GameList(Collection<GameData> games) { super(games); }

    public String toString() { return new Gson().toJson(this.toArray()); }

    public String toJson() {
        return new Gson().toJson(Map.of("games", this));
    }
}
