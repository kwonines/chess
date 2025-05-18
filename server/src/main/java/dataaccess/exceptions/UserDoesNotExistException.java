package dataaccess.exceptions;

import dataaccess.DataAccessException;

public class UserDoesNotExistException extends DataAccessException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
