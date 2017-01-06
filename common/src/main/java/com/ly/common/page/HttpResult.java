package com.ly.common.page;

import org.apache.http.HttpResponse;

public class HttpResult {

  public HttpResult() {
  }

  public HttpResult(HttpResponse response, String content) {
    this.response = response;
    this.content = content;
  }

  public HttpResponse getResponse() {
    return response;
  }

  public void setResponse(HttpResponse response) {
    this.response = response;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String toString() {
    return content;
  }

  private HttpResponse response;
  private String       content;
}
