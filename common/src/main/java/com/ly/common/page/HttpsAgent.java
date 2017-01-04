package com.ly.common.page;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst radix(10) lradix(10)
// Source File Name: HttpsAgent.java

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

// Referenced classes of package com.gf.util.page:
// HttpAgent

@Deprecated
public class HttpsAgent extends HttpAgent {
  private class MyX509HostnameVerifier implements X509HostnameVerifier {

    final HttpsAgent this$0;

    public void verify(String s, SSLSocket sslsocket) throws IOException {
    }

    public void verify(String s, X509Certificate x509certificate) throws SSLException {
    }

    public void verify(String s, String as[], String as1[]) throws SSLException {
    }

    public boolean verify(String s, SSLSession sslsession) {
      System.out.println("WARNING: Hostname is not matched for cert.");
      return true;
    }

    private MyX509HostnameVerifier() {
      this$0 = HttpsAgent.this;
    }

    MyX509HostnameVerifier(MyX509HostnameVerifier myx509hostnameverifier) {
      this();
    }
  }

  private class MyX509TrustManager implements X509TrustManager {

    final HttpsAgent this$0;

    public void checkClientTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    private MyX509TrustManager() {
      this$0 = HttpsAgent.this;
    }

    MyX509TrustManager(MyX509TrustManager myx509trustmanager) {
      this();
    }
  }

  public HttpsAgent(String charset) {
    super(charset);
    setSSL();
  }

  public void setSSL() {
    try {
      SSLContext ssc = SSLContext.getInstance("TLS");
      ssc.init(null, new TrustManager[] { new MyX509TrustManager(null) }, new SecureRandom());
      SSLSocketFactory ssf = new SSLSocketFactory(ssc, new MyX509HostnameVerifier(null));
      Scheme sch = new Scheme("https", 443, ssf);
      client.getConnectionManager().getSchemeRegistry().register(sch);
    } catch (KeyManagementException keymanagementexception) {
    } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
    }
  }
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from: C:\Users\Gaffer\Documents\image.jar Total time: 31 ms Jad reported messages/errors: Exit status: 0 Caught exceptions:
 */