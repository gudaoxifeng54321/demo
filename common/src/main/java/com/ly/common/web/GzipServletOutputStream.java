package com.ly.common.web;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;

class GzipServletOutputStream extends ServletOutputStream {
  private GZIPOutputStream gzipos;

  public GzipServletOutputStream(ServletOutputStream sos) throws Exception {
    this.gzipos = new GZIPOutputStream(sos);
  }

  @Override
  public void write(int arg0) throws IOException {
    gzipos.write(arg0);
  }

  public GZIPOutputStream getGZIPOutputStream() {
    return gzipos;
  }
}
