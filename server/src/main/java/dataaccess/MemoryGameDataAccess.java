package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess {
    HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void addGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData findGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(int gameID, GameData newGame) {
        games.replace(gameID, newGame);
    }

    @Override
    public void clear() {
        games.clear();
    }
}
