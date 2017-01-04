package com.ly.common.upload;

public class UploadFileResult {
  
  public String message;
  private String filePath;
  
  public UploadFileResult(String message, String filePath) {
    this.message = message;
    this.setFilePath(filePath);
  }

  public UploadFileResult(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
  
  
}
