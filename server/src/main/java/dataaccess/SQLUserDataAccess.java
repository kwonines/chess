package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDataAccess implements UserDataAccess {

    @Override
    public void clear() {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var clearStatement = connection.prepareStatement("DELETE FROM users")) {
                clearStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            System.out.println("Error: something went wrong (SQLException)");
        } catch (DataAccessException exception) {
            System.out.println("Error: something went wrong (DataAccessException");
        }
    }

    @Override
    public UserData findUser(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var searchStatement = connection.prepareStatement("SELECT username, password, email FROM users WHERE username =?")) {
                searchStatement.setString(1, username);
                try (var result = searchStatement.executeQuery()) {
                    if (result.next()) {
                        return new UserData(result.getString("username"),
                                result.getString("password"), result.getString("email"));
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: something went wrong (SQLException)");
            return null;
        }
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var addStatement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
                addStatement.setString(1, userData.username());
                addStatement.setString(2, userData.password());
                addStatement.setString(3, userData.email());

                addStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: something went wrong (SQLException)");
        }
    }
}
