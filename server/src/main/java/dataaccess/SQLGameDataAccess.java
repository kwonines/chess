package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameDataAccess {
    @Override
    public void clear() {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var clearStatement = connection.prepareStatement("DELETE FROM games")) {
                clearStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            System.out.println("Error: something went wrong (SQLException)");
        } catch (DataAccessException exception) {
            System.out.println("Error: something went wrong (DataAccessException");
        }
    }

    @Override
    public void addGame(GameData gameData) {
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
        } catch (SQLException e) {
            System.out.println("Something went wrong(SQL Exception)");
        } catch (DataAccessException e) {
            System.out.println("Something went wrong(DataAccessException)");
        }
    }

    @Override
    public GameData findGame(int gameID) {
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
                    else return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong (SQLException on findGame");
            return null;
        } catch (DataAccessException e) {
            System.out.println("Something went wrong (DataAccessException on findGame");
            return null;
        }
    }

    @Override
    public ArrayList<GameData> listGames() {
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
        } catch (SQLException e) {
            System.out.println("Something went wrong (SQLException on listGames");
            return null;
        } catch (DataAccessException e) {
            System.out.println("Something went wrong (DataAccessException on listGames");
            return null;
        }
    }

    @Override
    public void updateGame(int gameID, GameData newGame) {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("UPDATE games SET whiteUsername =?, blackUsername =?, game =? WHERE gameID =?")) {
                statement.setString(1, newGame.whiteUsername());
                statement.setString(2, newGame.blackUsername());
                statement.setString(3, new Gson().toJson(newGame.game()));
                statement.setInt(4, gameID);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong (SQLException on updateGame");
        } catch (DataAccessException e) {
            System.out.println("Something went wrong (DataAccessException on updateGame");
        }
    }
}
