package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

/*
createGame: Create a new game.
getGame: Retrieve a specified game with the given game ID.
listGames: Retrieve all games.
updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID.
    This is used when players join a game or when a move is made.
 */

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
    public void updateGame(String playerColor, int gameID) {

    }

    @Override
    public void clear() {
        games.clear();
    }
}
