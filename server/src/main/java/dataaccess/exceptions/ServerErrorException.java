package dataaccess.exceptions;

import dataaccess.DataAccessException;

public class ServerErrorException extends DataAccessException {
  public ServerErrorException(String message) {
    super(message);
  }
}
