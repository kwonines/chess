package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDataAccess implements UserDataAccess {

    @Override
    public void clear() {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM users")) {
                statement.executeUpdate();
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
            try (var statement = connection.prepareStatement("SELECT username, password, email FROM users WHERE username =?")) {
                statement.setString(1, username);
                try (var result = statement.executeQuery()) {
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
            try (var statement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
                statement.setString(1, userData.username());
                String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
                statement.setString(2, hashedPassword);
                statement.setString(3, userData.email());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: something went wrong (SQLException)");
        }
    }
}
