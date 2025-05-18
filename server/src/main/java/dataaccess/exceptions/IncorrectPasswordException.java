package dataaccess.exceptions;

import dataaccess.DataAccessException;

public class IncorrectPasswordException extends DataAccessException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
