package com.ly.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Unreal
 */
public class WebFilter implements Filter {
  private String  encoding;
  private boolean gzip;
  private boolean noCache;

  @Override
  public void destroy() {

  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (encoding != null) {
      request.setCharacterEncoding(encoding);
    }
    if (noCache) {
      response.setHeader("Cache-Control", "no-cache");
      response.setHeader("Pragma", "no-cache");
      response.setDateHeader("Expires", 0);
    }
    if (gzip) {
      String acceptEncodings = request.getHeader("Accept-Encoding");
      if (acceptEncodings != null && acceptEncodings.indexOf("gzip") > -1) {
        try {
          GzipWrapper gzipWarpper = new GzipWrapper(response);
          response.setHeader("Content-Encoding", "gzip");
          chain.doFilter(request, gzipWarpper);
          gzipWarpper.getGzipOutputStream().flush();
          gzipWarpper.getGzipOutputStream().close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {

      }
    }
    try {
      chain.doFilter(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    encoding = config.getInitParameter("encoding");
    gzip = "yes".equals(config.getInitParameter("gzip"));
    noCache = "yes".equals(config.getInitParameter("noCache"));
  }
}
