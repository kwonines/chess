package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDataAccess implements AuthDataAccess {
    @Override
    public void clear() {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM authorizations")) {
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            System.out.println("Error: something went wrong (SQLException in clear)");
        } catch (DataAccessException exception) {
            System.out.println("Error: something went wrong (DataAccessException in clear)");
        }
    }

    @Override
    public void addAuth(AuthData authData) {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("INSERT INTO authorizations (authToken, username) VALUES (?,?)")) {
                statement.setString(1, authData.authToken());
                statement.setString(2, authData.username());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong (SQLException in addAuth)");
        } catch (DataAccessException e) {
            System.out.println("Something went wrong (DataAccessException in addAuth)");
        }

    }

    @Override
    public AuthData findAuth(String authToken) {
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
        } catch (SQLException e) {
            System.out.println("Something went wrong (SQLException in findAuth)");
            return null;
        } catch (DataAccessException e) {
            System.out.println("Something went wrong (DataAccessException in findAuth)");
            return null;
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM authorizations WHERE authToken =?")) {
                statement.setString(1, authToken);
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            System.out.println("Error: something went wrong (SQLException in clear)");
        } catch (DataAccessException exception) {
            System.out.println("Error: something went wrong (DataAccessException in clear)");
        }
    }
}
