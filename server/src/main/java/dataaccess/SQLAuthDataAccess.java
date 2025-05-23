package dataaccess;

import dataaccess.exceptions.ServerErrorException;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDataAccess implements AuthDataAccess {
    @Override
    public void clear() throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM authorizations")) {
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException exception) {
            throw new ServerErrorException("Internal server error");
        }
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("INSERT INTO authorizations (authToken, username) VALUES (?,?)")) {
                statement.setString(1, authData.authToken());
                statement.setString(2, authData.username());

                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new ServerErrorException("Internal server error");
        }

    }

    @Override
    public AuthData findAuth(String authToken) throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT authToken, username FROM authorizations WHERE authToken =?")) {
                statement.setString(1, authToken);
                try (var result = statement.executeQuery()) {
                    if (result.next()) {
                        return new AuthData(result.getString("username"), result.getString("authToken"));
                    }
                    else return null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new ServerErrorException("Internal server error");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws ServerErrorException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM authorizations WHERE authToken =?")) {
                statement.setString(1, authToken);
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException exception) {
            throw new ServerErrorException("Internal server error");
        }
    }
}
