package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    void addGame(GameData gameData);
    GameData findGame(int gameID);
    ArrayList<GameData> listGames();
    void updateGame(String playerColor, int gameID);
}
