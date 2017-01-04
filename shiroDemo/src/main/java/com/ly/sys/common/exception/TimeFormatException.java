package com.ly.sys.common.exception;

public class TimeFormatException extends RuntimeException {
  private static final long serialVersionUID = -5844536921092226898L;

  public TimeFormatException() {
   super();
  }
  
  public TimeFormatException(String message) {
    super(message);
  }
  public TimeFormatException(String message,Throwable cause) {
    super(message,cause);
  }
  public TimeFormatException(Throwable cause) {
    super(cause);
  }
}
