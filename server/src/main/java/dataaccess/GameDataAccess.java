package dataaccess;

import dataaccess.exceptions.ServerErrorException;
import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    void clear() throws ServerErrorException;
    void addGame(GameData gameData) throws ServerErrorException;
    GameData findGame(int gameID) throws ServerErrorException;
    ArrayList<GameData> listGames() throws ServerErrorException;
    void updateGame(int gameID, GameData newGame) throws ServerErrorException;
}
