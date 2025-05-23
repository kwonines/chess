package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.ServerErrorException;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameDataAccess {
    @Override
    public void clear() throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM games")) {
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException exception) {
            throw new ServerErrorException("Internal Server error");
        }
    }

    @Override
    public void addGame(GameData gameData) throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("INSERT INTO games " +
                    "(gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")) {
                statement.setInt(1, gameData.gameID());
                statement.setString(2, gameData.whiteUsername());
                statement.setString(3, gameData.blackUsername());
                statement.setString(4, gameData.gameName());
                statement.setString(5, new Gson().toJson(gameData.game()));

                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new ServerErrorException("Internal server error");
        }
    }

    @Override
    public GameData findGame(int gameID) throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement
                    ("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID =?")) {
                statement.setInt(1, gameID);
                try (var result = statement.executeQuery()) {
                    if (result.next()) {
                        String whiteUsername = result.getString("whiteUsername");
                        String blackUsername = result.getString("blackUsername");
                        String gameName = result.getString("gameName");
                        ChessGame game = new Gson().fromJson(result.getString("game"), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                    else {
                        return null;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new ServerErrorException("Internal server error");
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement
                    ("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games")) {
                try (var result = statement.executeQuery()) {
                    ArrayList<GameData> games = new ArrayList<>();
                    while (result.next()) {
                        int gameID = result.getInt("gameID");
                        String whiteUsername = result.getString("whiteUsername");
                        String blackUsername = result.getString("blackUsername");
                        String gameName = result.getString("gameName");
                        ChessGame game = new Gson().fromJson(result.getString("game"), ChessGame.class);
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                    return games;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new ServerErrorException("Internal server error");
        }
    }

    @Override
    public void updateGame(int gameID, GameData newGame) throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("UPDATE games SET whiteUsername =?, blackUsername =?, game =? WHERE gameID =?")) {
                statement.setString(1, newGame.whiteUsername());
                statement.setString(2, newGame.blackUsername());
                statement.setString(3, new Gson().toJson(newGame.game()));
                statement.setInt(4, gameID);

                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new ServerErrorException("Internal server error");
        }
    }
}
