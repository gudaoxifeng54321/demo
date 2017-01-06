package com.ly.common.web;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class GzipWrapper extends HttpServletResponseWrapper {
  private GzipServletOutputStream gzipsos;

  public GzipWrapper(HttpServletResponse response) throws Exception {
    super(response);
    gzipsos = new GzipServletOutputStream(response.getOutputStream());
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return gzipsos;
  }

  @Override
  public void setContentLength(int len) {
  }

  public GZIPOutputStream getGzipOutputStream() {
    return gzipsos.getGZIPOutputStream();
  }
}
