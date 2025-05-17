package dataaccess.exceptions;

import dataaccess.DataAccessException;

//Indicates that something (like a username) was missing from a request
public class BadRequestException extends DataAccessException {
    public BadRequestException(String message) {
        super(message);
    }
}
