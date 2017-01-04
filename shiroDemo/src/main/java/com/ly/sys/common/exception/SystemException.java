package com.ly.sys.common.exception;

import java.sql.SQLException;

public class SystemException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = -5844536921092226898L;
  

  public static SystemException getDAOExceptionCause(SQLException e){
    return new SystemException("database failed.",e);
  }
  
  public SystemException() {
    super();
  }

  public SystemException(String message) {
    super(message);
  }

  public SystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public SystemException(Throwable cause) {
    super(cause);
  }
}
