package com.ly.common.script;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * http模拟器
 * 
 * @author lan
 * 
 */
public class HttpInvoker {

  private static class Holder {
    private static HttpInvoker instance = new HttpInvoker();
  }

  private static Log              log = LogFactory.getLog(HttpInvoker.class);

  private HttpClient              client;                                     // 客户端
  private String                  host;                                       // 主机地址
  private ResponseHandler<String> handler;                                    // 应答处理
  private boolean                 wrap;                                       // 是否进行Agent伪装

  private HttpInvoker() {
    handler = new BasicResponseHandler();

    ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
    cm.setMaxTotal(10);
    client = new DefaultHttpClient(cm);

  }

  /**
   * 获取模拟器实例
   * 
   * @return
   */
  public static HttpInvoker getInstance() {
    return Holder.instance;
  }

  /**
   * 设置基本路径,如http://localhost:8080/report
   * 
   * @param host
   */
  public HttpInvoker setHost(String host) {
    if (!host.endsWith("/")) {
      host += "/";
    }
    this.host = host;
    return this;
  }

  /**
   * 进行User-Agent伪装
   */
  public HttpInvoker setWrap(boolean wrap) {
    this.wrap = wrap;
    return this;
  }

  /**
   * 使用安全套接字连接
   * 
   * @return
   */
  public HttpInvoker setSSL() {
    try {
      SSLContext ssc = SSLContext.getInstance("TLS");
      ssc.init(null, new TrustManager[] { new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

      } }, new SecureRandom());
      SSLSocketFactory ssf = new SSLSocketFactory(ssc, new X509HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
          System.out.println("WARNING: Hostname is not matched for cert.");
          return true;
        }

        @Override
        public void verify(String s, SSLSocket sslsocket) throws IOException {
          // TODO Auto-generated method stub

        }

        @Override
        public void verify(String s, X509Certificate x509certificate) throws SSLException {
          // TODO Auto-generated method stub

        }

        @Override
        public void verify(String s, String[] as, String[] as1) throws SSLException {
          // TODO Auto-generated method stub

        }
      });
      Scheme sch = new Scheme("https", 443, ssf);
      client.getConnectionManager().getSchemeRegistry().register(sch);
    } catch (KeyManagementException e) {
      log.error(e.getMessage(), e);
    } catch (NoSuchAlgorithmException e) {
      log.error(e.getMessage(), e);
    }
    return this;
  }

  /**
   * 请求地址和host拼接成完整url
   * 
   * @param action
   * @return
   */
  private String makeURL(String action) {
    if (action == null) {
      action = "";
    }
    if (action.startsWith("/")) {
      action = action.substring(1);
    }
    return (this.host == null ? "" : this.host) + action;
  }

  /**
   * 伪装成FF8
   * 
   * @param request
   */
  private void agent(HttpRequestBase request) {
    request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,image/png,image/*;q=0.8,*/*;q=0.5");
    request.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
    request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:8.0) Gecko/20100101 Firefox/8.0");
    request.setHeader("Accept-Encoding", "gzip, deflate");
    request.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
    request.setHeader("Connection", "Keep-Alive");
  }

  /**
   * 模拟get操作
   * 
   * @param action
   *          请求地址,如果设置了host,则只需要action即可
   * @return server返回结果
   */
  public String get(String action) {
    HttpResponse response = doGet(action);
    if (response == null) {
      return null;
    }
    try {
      return handler.handleResponse(response);
    } catch (HttpResponseException e) {
      log.error(e.getMessage(), e);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 模拟get
   * 
   * @param action
   * @return
   */
  private HttpResponse doGet(String action) {
    HttpGet get = new HttpGet(makeURL(action));
    if (wrap) {
      agent(get);
    }
    try {
      return client.execute(get);
    } catch (ClientProtocolException e) {
      log.error(e.getMessage(), e);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 模拟post操作
   * 
   * @param action
   *          请求地址,如果设置了host,则只需要action即可
   * @param params
   *          post的参数,允许为null
   * @return server返回结果
   */
  public String post(String action, Map<String, String> params) {
    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    if (params != null && !params.isEmpty()) {
      for (Map.Entry<String, String> entry : params.entrySet()) {
        NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue());
        pairs.add(nvp);
      }
    }
    try {
      return doPost(action, new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 模拟post
   * 
   * @param action
   * @param entity
   * @return
   */
  private String doPost(String action, HttpEntity entity) {
    HttpPost post = new HttpPost(makeURL(action));
    if (wrap) {
      agent(post);
    }
    try {
      post.setEntity(entity);
      return client.execute(post, new BasicResponseHandler());
    } catch (ClientProtocolException e) {
      log.error(e.getMessage(), e);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 上传文件操作
   * 
   * @param action
   *          请求地址,如果设置了host,则只需要action即可
   * @param params
   *          post的参数,允许为null.value只接受String,InputStream,byte[]和File.<b> InputStream请自行关闭.</b>
   * @return server返回的结果
   * 
   *         public String upload(String action, Map<String, Object> params) { MultipartEntity entity = new
   *         MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); Charset utf8 = Charset.forName("UTF-8"); if (params != null &&
   *         !params.isEmpty()) { for (Map.Entry<String, Object> entry : params.entrySet()) { String name = entry.getKey(); Object value =
   *         entry.getValue(); if (value instanceof String) { try { entity.addPart(name, new StringBody((String) value, utf8)); } catch
   *         (UnsupportedEncodingException e) { log.error(e.getMessage(), e); } } else if (value instanceof File) { entity.addPart(name, new
   *         FileBody((File) value)); } else if (value instanceof InputStream) { entity.addPart(name, new InputStreamBody((InputStream)
   *         value, name)); } else if (value instanceof byte[]) { entity.addPart(name, new ByteArrayBody((byte[]) value, name)); }
   * 
   *         } }
   * 
   *         return doPost(action, entity); }
   */
  /**
   * 下载文件,文件将保存到系统临时文件夹
   * 
   * @param action
   *          get地址
   * @param suffix
   *          文件后缀,为null则默认为".tmp"文件
   * @param callback
   *          下载进度的回调,允许为null
   * @return 文件或者null
   */
  public File download(String action, String suffix, final DownloadCallback callback) {
    HttpResponse response = doGet(action);
    if (response == null) {
      return null;
    }

    HttpEntity entity = response.getEntity();
    if (entity == null) {
      return null;
    }
    if (suffix != null && !suffix.startsWith(".")) {
      suffix = "." + suffix;
    }

    File f = null;
    InputStream is = null;
    BufferedOutputStream bos = null;
    try {
      is = entity.getContent();
      f = File.createTempFile(String.valueOf(System.currentTimeMillis()), suffix);
      final long length = entity.getContentLength();
      log.info("开始下载:" + action + ",长度:" + length + " 到文件:" + f.getAbsolutePath());
      bos = new BufferedOutputStream(new FileOutputStream(f));
      byte[] b = new byte[1024 * 5];
      int l = -1;

      final AtomicLong count = new AtomicLong(0);
      Thread caller = null;

      if (callback != null) {
        caller = new Thread() {
          @Override
          public void run() {
            long last = System.currentTimeMillis();
            for (;;) {
              try {
                Thread.sleep(callback.interval());
                long now = System.currentTimeMillis();
                call(count.get(), now - last);
                last = now;
              } catch (InterruptedException e) {
                break;
              }
            }
            call(count.get(), System.currentTimeMillis() - last);
          }

          private void call(long count, long gap) {
            CallbackEvent event = new CallbackEvent(length, count, gap);
            callback.call(event);
          }
        };
        caller.start();
      }
      while ((l = is.read(b)) != -1) {
        bos.write(b, 0, l);
        count.addAndGet(l);
      }
      if (caller != null) {
        caller.interrupt();
      }
      log.info("下载完毕.");
      bos.flush();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (is != null) {
          is.close();
        }
      } catch (Exception e) {
      }
      try {
        if (bos != null) {
          bos.close();
        }
      } catch (Exception e) {
      }
    }
    return f;
  }

  /**
   * 断开所有连接
   */
  public void destroy() {
    if (client != null) {
      client.getConnectionManager().shutdown();
    }
  }
}

/**
 * 下载回调接口
 * 
 * @author lan
 * 
 */
interface DownloadCallback {

  /**
   * 回调方法
   * 
   * @param event
   */
  public void call(CallbackEvent event);

  /**
   * 回调间隔,单位为毫秒
   * 
   * @return
   */
  public int interval();

}

/**
 * 下载回调事件
 * 
 * @author lan
 * 
 */
class CallbackEvent {

  /**
   * 文件大小,有可能是-1
   */
  private long total;
  /**
   * 当前已获得大小
   */
  private long current;
  /**
   * 距离上次回调的时间间隔,单位为毫秒
   */
  private long realInterval;

  public CallbackEvent(long total, long current, long realInterval) {
    this.total = total;
    this.current = current;
    this.realInterval = realInterval;
  }

  public long getTotal() {
    return total;
  }

  public long getCurrent() {
    return current;
  }

  public long getRealInterval() {
    return realInterval;
  }

}
