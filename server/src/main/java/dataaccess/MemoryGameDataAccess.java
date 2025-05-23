package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess {
    private static final HashMap<Integer, GameData> GAMES = new HashMap<>();

    @Override
    public void addGame(GameData gameData) {
        GAMES.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData findGame(int gameID) {
        return GAMES.get(gameID);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(GAMES.values());
    }

    @Override
    public void updateGame(int gameID, GameData newGame) {
        GAMES.replace(gameID, newGame);
    }

    @Override
    public void clear() {
        GAMES.clear();
    }
}
