package dataaccess.exceptions;

import dataaccess.DataAccessException;

public class UsernameTakenException extends DataAccessException {
    public UsernameTakenException(String message) {
        super(message);
    }
}
