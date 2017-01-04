/*
 * Copyright (c) 2011-2012, IUnknown. All rights reserved.
 * 
 * Project: AuthentiKate Me!
 * 
 * Description: This project is a small tool that automates the login-procedure of Firewall authentication for accessing NetAct labs in NSN.
 * This program will perform auto-authentication to the Tampere, Bangalore and Munich Juniper Infranet Controller's.
 */

package com.ly.common.script;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.ly.common.script.NotificationTray.STATE;

public class AuthentiKateMe {

  private DefaultHttpClient  mHttpclientTampere;
  private DefaultHttpClient  mHttpclientBangalore;
  private DefaultHttpClient  mHttpclientMunich;

  public NotificationTray    mTray;

  private AuthentiKateMeMain mAuthentiKateMeMain;

  enum ERROR_CODE {
    INIT_FAILED, CONNECTION_FAILED, CREDENTIALS_WRONG, SUCCESS, PARTIAL_FAILURE
  }

  enum SIGNED_IN_STATUS {
    SUCCESS, PARTIAL, FAILED
  }

  enum PING_STATUS {
    PING_SUCCESS, PING_FAILED
  }

  SIGNED_IN_STATUS            mSignedInStatus              = SIGNED_IN_STATUS.FAILED;

  private final static String HTTP_STATUS_200_OK           = "HTTP/1.1 200 OK";
  private final static String HTTP_STATUS_302_MOVED        = "HTTP/1.1 302 Moved";
  private final static String HTTP_STATUS_302_FOUND        = "HTTP/1.1 302 Found";

  private final static String SUCCESS_STRING_AUTHENTICATED = "Successfully authenticated to the following servers:\n\nTampere\t: Success\nBangalore\t: Success\nMunich\t\t: Success";
  // private final static String SUCCESS_STRING_AUTHENTICATED =
  // "Successfully authenticated to the following servers:\n\nTampere\t\t: Success\nBangalore\t: Success\nMunich\t\t: Success";
  private final static String ERR_STRING_CONN_FAILURE      = "Connection lost to the Authentication Servers.\nPlease check your network connectivity.";
  private final static String ERR_STRING_AUTH_FAILURE      = "Authentication failure.\nPlease check your Username and Password.";

  // Tampere AUTH Links
  private final static String mTampereAuthHost             = "logintolabra.tre.noklab.net";
  private final static String mTampereAuthLinkTest         = "https://logintolabra.tre.noklab.net/dana-na/auth/url_5/welcome.cgi";
  private final static String mTampereAuthLinkLogout       = "https://logintolabra.tre.noklab.net/dana-na/auth/logout.cgi";
  private final static String mTampereAuthLinks[]          = new String[] {
      "https://logintolabra.tre.noklab.net/dana-na/auth/url_5/login.cgi",
      "https://logintolabra.tre.noklab.net/dana/home/starter0.cgi?check=yes", "https://logintolabra.tre.noklab.net/dana/home/starter.cgi",
      "https://logintolabra.tre.noklab.net/dana/home/eprun.cgi", "https://logintolabra.tre.noklab.net/dana/home/infranet.cgi" };

  // Bangalore AUTH Links
  private final static String mBangaloreAuthHost           = "10.58.148.122";
  private final static String mBangaloreAuthLinkTest       = "https://10.58.148.122/dana-na/auth/url_default/welcome.cgi";
  private final static String mBangaloreAuthLinkLogout     = "https://10.58.148.122/dana-na/auth/logout.cgi";
  private final static String mBangaloreAuthLinks[]        = new String[] { "https://10.58.148.122/dana-na/auth/url_default/login.cgi",
      "https://10.58.148.122/dana/home/starter0.cgi?check=yes", "https://10.58.148.122/dana/home/starter.cgi",
      "https://10.58.148.122/dana/home/eprun.cgi", "https://10.58.148.122/dana/home/infranet.cgi" };

  // Muenchen AUTH Links (Kara8, Saeteri, Athens and Munich)
  private final static String mMunichAuthHost              = "logintolabra.mchm.nsn-rdnet.net";
  private final static String mMunichAuthLinkTest          = "https://logintolabra.mchm.nsn-rdnet.net/dana-na/auth/url_default/welcome.cgi";
  private final static String mMunichAuthLinkLogout        = "https://logintolabra.mchm.nsn-rdnet.net/dana-na/auth/logout.cgi";
  private final static String mMunichAuthLinks[]           = new String[] {
      "https://logintolabra.mchm.nsn-rdnet.net/dana-na/auth/url_default/login.cgi",
      "https://logintolabra.mchm.nsn-rdnet.net/dana/home/starter0.cgi?check=yes",
      "https://logintolabra.mchm.nsn-rdnet.net/dana/home/starter.cgi", "https://logintolabra.mchm.nsn-rdnet.net/dana/home/eprun.cgi",
      "https://logintolabra.mchm.nsn-rdnet.net/dana/home/infranet.cgi" };

  public void initialize(AuthentiKateMeMain authentiKateMeMain) {

    mAuthentiKateMeMain = authentiKateMeMain;

    mTray = new NotificationTray(this, authentiKateMeMain);
    mTray.initialize(STATE.DOWN);
  }

  public PING_STATUS ping() {

    STATE currState = mTray.getState();

    if (currState == STATE.DOWN)
      mTray.setState(STATE.DOWN_RECONN);
    else if (currState == STATE.UP)
      mTray.setState(STATE.UP_RECONN);
    else if (currState == STATE.PARTIAL)
      mTray.setState(STATE.PARTIAL_RECONN);

    // Do the actual PING operation.
    PING_STATUS status = pingInternalViaICMP();

    mTray.setState(currState);

    if (status != PING_STATUS.PING_SUCCESS) {

      mTray.setState(STATE.DOWN);

      mSignedInStatus = SIGNED_IN_STATUS.FAILED;

      mTray.setStatusMessage(ERR_STRING_CONN_FAILURE, MessageType.ERROR);

      mTray.showBalloonMessage(ERR_STRING_CONN_FAILURE, MessageType.ERROR);
    }

    return status;
  }

  private PING_STATUS pingInternalViaICMP() {

    System.out.println("...");

    System.out.print(new Date() + " - Performing connectivity check for Tampere Authentication Server...");
    PING_STATUS errCode = pingIcmp(mTampereAuthHost);

    if (errCode != PING_STATUS.PING_SUCCESS) {

      System.out.println("Failed");
      return PING_STATUS.PING_FAILED;
    }

    System.out.println("OK");

    System.out.print(new Date() + " - Performing connectivity check for Bangalore Authentication Server...");
    errCode = pingIcmp(mBangaloreAuthHost);

    if (errCode != PING_STATUS.PING_SUCCESS) {

      System.out.println("Failed");
      return PING_STATUS.PING_FAILED;
    }

    System.out.println("OK");

    System.out.print(new Date() + " - Performing connectivity check for Munich Authentication Server...");
    errCode = pingIcmp(mMunichAuthHost);

    if (errCode != PING_STATUS.PING_SUCCESS) {

      System.out.println("Failed");
      return PING_STATUS.PING_FAILED;
    }

    System.out.println("OK");

    return PING_STATUS.PING_SUCCESS;
  }

  private PING_STATUS pingIcmp(String strHost) {

    Socket socket = null;
    boolean reachable = false;

    try {
      socket = new Socket(strHost, 443);
      reachable = true;

    } catch (Exception e) {
      reachable = false;
    } finally {
      if (socket != null)
        try {
          socket.close();
        } catch (Exception e) {
        }
    }

    if (reachable == true)
      return PING_STATUS.PING_SUCCESS;

    return PING_STATUS.PING_FAILED;
  }

  private PING_STATUS pingInternalViaHttpGet() {

    DefaultHttpClient httpClient = getHttpConnection();

    System.out.println("...");

    System.out.println(new Date() + " - Performing connectivity check for Tampere Authentication Server");
    ERROR_CODE errCode = executeHttpGetRequest(httpClient, mTampereAuthLinkTest);

    if (errCode != ERROR_CODE.SUCCESS)
      return PING_STATUS.PING_FAILED;

    System.out.println(new Date() + " - Performing connectivity check for Bangalore Authentication Server");
    errCode = executeHttpGetRequest(httpClient, mBangaloreAuthLinkTest);

    if (errCode != ERROR_CODE.SUCCESS)
      return PING_STATUS.PING_FAILED;

    System.out.println(new Date() + " - Performing connectivity check for Munich Authentication Server");
    errCode = executeHttpGetRequest(httpClient, mMunichAuthLinkTest);

    if (errCode != ERROR_CODE.SUCCESS)
      return PING_STATUS.PING_FAILED;

    return PING_STATUS.PING_SUCCESS;
  }

  public ERROR_CODE signIn(String username, String password) {

    // For the first time, we need to avoid doing the shutdown, as the
    // connections wouldn't have been initialized.
    if (mHttpclientTampere != null)
      mHttpclientTampere.getConnectionManager().shutdown();
    if (mHttpclientBangalore != null)
      mHttpclientBangalore.getConnectionManager().shutdown();
    if (mHttpclientMunich != null)
      mHttpclientMunich.getConnectionManager().shutdown();

    mHttpclientTampere = getHttpConnection();
    mHttpclientBangalore = getHttpConnection();
    mHttpclientMunich = getHttpConnection();

    if (mHttpclientTampere == null || mHttpclientBangalore == null || mHttpclientMunich == null) {

      System.out.println("AuthentiKateMe! ERROR@initialize: Failed to initialize the HTTPS connection.");

      return ERROR_CODE.INIT_FAILED;
    }

    STATE currState = mTray.getState();

    if (currState == STATE.DOWN)
      mTray.setState(STATE.DOWN_RECONN);
    else if (currState == STATE.UP)
      mTray.setState(STATE.UP_RECONN);
    else if (currState == STATE.PARTIAL)
      mTray.setState(STATE.PARTIAL_RECONN);

    System.out.println(new Date() + " - Signing into Tampere Authentication Server");
    ERROR_CODE errCodeTampere = signIn(mHttpclientTampere, username, password, mTampereAuthLinkTest, mTampereAuthLinks, "Tampere", "NSN-AD");

    System.out.println(new Date() + " - Signing into Bangalore Authentication Server");
    ERROR_CODE errCodeBangalore = signIn(mHttpclientBangalore, username, password, mBangaloreAuthLinkTest, mBangaloreAuthLinks,
        "Bangalore", "NSN AD");

    System.out.println(new Date() + " - Signing into Munich Authentication Server");
    ERROR_CODE errCodeMunich = signIn(mHttpclientMunich, username, password, mMunichAuthLinkTest, mMunichAuthLinks, "Munich", "NSN AD");

    System.out.println("...");

    mTray.setState(currState);

    if (errCodeTampere == ERROR_CODE.SUCCESS && errCodeBangalore == ERROR_CODE.SUCCESS && errCodeMunich == ERROR_CODE.SUCCESS) { // All
                                                                                                                                 // success!

      mTray.setState(STATE.UP);

      mTray.setStatusMessage(SUCCESS_STRING_AUTHENTICATED, MessageType.INFO);

      // Show balloon message only if previously we had a connection failure to Auth Servers
      // if (mSignedInStatus == SIGNED_IN_STATUS.FAILED || mSignedInStatus == SIGNED_IN_STATUS.PARTIAL)
      {

        mTray.showBalloonMessage(SUCCESS_STRING_AUTHENTICATED, MessageType.INFO);
      }

      mSignedInStatus = SIGNED_IN_STATUS.SUCCESS;

      return ERROR_CODE.SUCCESS;

    } else if (errCodeTampere == ERROR_CODE.CONNECTION_FAILED && errCodeBangalore == ERROR_CODE.CONNECTION_FAILED
        && errCodeMunich == ERROR_CODE.CONNECTION_FAILED) { // Full failure case - Connection down.

      mTray.setState(STATE.DOWN);

      mSignedInStatus = SIGNED_IN_STATUS.FAILED;

      mTray.setStatusMessage(ERR_STRING_CONN_FAILURE, MessageType.ERROR);

      mTray.showBalloonMessage(ERR_STRING_CONN_FAILURE, MessageType.ERROR);

      return ERROR_CODE.CONNECTION_FAILED;

    } else if (errCodeTampere == ERROR_CODE.CREDENTIALS_WRONG || errCodeBangalore == ERROR_CODE.CREDENTIALS_WRONG
        || errCodeMunich == ERROR_CODE.CREDENTIALS_WRONG) { // Full failure case - Wrong credentials

      mTray.setState(STATE.DOWN);

      mSignedInStatus = SIGNED_IN_STATUS.FAILED;

      mTray.setStatusMessage(ERR_STRING_AUTH_FAILURE, MessageType.ERROR);

      mTray.showBalloonMessage(ERR_STRING_AUTH_FAILURE, MessageType.ERROR);

      return ERROR_CODE.CREDENTIALS_WRONG;

    } else { // One or more of them failed!

      String strMessage = "Login failed for one or more Authentication Servers:\n\n";

      strMessage += "Tampere\t: ";
      if (errCodeTampere == ERROR_CODE.SUCCESS)
        strMessage += "Success\n";
      else if (errCodeTampere == ERROR_CODE.CONNECTION_FAILED)
        strMessage += "Connection Failed\n";
      else if (errCodeTampere == ERROR_CODE.CREDENTIALS_WRONG)
        strMessage += "Invalid Username/Password\n";
      else
        strMessage += "Unknown\n";

      strMessage += "Bangalore\t: ";
      if (errCodeBangalore == ERROR_CODE.SUCCESS)
        strMessage += "Success\n";
      else if (errCodeBangalore == ERROR_CODE.CONNECTION_FAILED)
        strMessage += "Connection Failed\n";
      else if (errCodeBangalore == ERROR_CODE.CREDENTIALS_WRONG)
        strMessage += "Invalid Username/Password\n";
      else
        strMessage += "Unknown\n";

      strMessage += "Munich\t\t: ";
      if (errCodeMunich == ERROR_CODE.SUCCESS)
        strMessage += "Success";
      else if (errCodeMunich == ERROR_CODE.CONNECTION_FAILED)
        strMessage += "Connection Failed";
      else if (errCodeMunich == ERROR_CODE.CREDENTIALS_WRONG)
        strMessage += "Invalid Username/Password";
      else
        strMessage += "Unknown";

      mTray.setStatusMessage(strMessage, MessageType.WARNING);

      // Show balloon message only if previously we had a successful/failed connection to Auth Servers
      // if (mSignedInStatus == SIGNED_IN_STATUS.SUCCESS || mSignedInStatus == SIGNED_IN_STATUS.FAILED)
      {

        mTray.showBalloonMessage(strMessage, MessageType.WARNING);
      }

      mTray.setState(STATE.PARTIAL);

      mSignedInStatus = SIGNED_IN_STATUS.PARTIAL;

      // Force a connection failure, even if we succeeded for few auth servers.
      return ERROR_CODE.PARTIAL_FAILURE;
    }
  }

  /**
   * Note: This method is not thread safe!
   */
  public ERROR_CODE signIn(DefaultHttpClient httpclient, String username, String password, String authTestLink, String authLinks[],
      String message, String strRealm) {

    // Show balloon message only if not-signed-in yet.
    // if (mSignedInStatus == SIGNED_IN_STATUS.FAILED)
    mTray.showBalloonMessage("Authenticating to " + message + " Authentication Server...", MessageType.INFO);

    ERROR_CODE errCode;

    errCode = executeHttpGetRequest(httpclient, authTestLink);

    if (errCode != ERROR_CODE.SUCCESS) {

      return ERROR_CODE.CONNECTION_FAILED;
    }

    for (String httpPostURL : authLinks) {

      errCode = executeHttpPostRequest(httpclient, httpPostURL, username, password, strRealm);

      if (errCode != ERROR_CODE.SUCCESS) {

        return errCode;
      }
    }

    return ERROR_CODE.SUCCESS;
  }

  public ERROR_CODE signOut() {

    if (mSignedInStatus == SIGNED_IN_STATUS.FAILED)
      return ERROR_CODE.SUCCESS;

    System.out.println(new Date() + " - Signing out...");
    ERROR_CODE errCode = ERROR_CODE.SUCCESS;

    try {
      if (mHttpclientTampere != null) {

        System.out.println(new Date() + " - Signing out of Tampere Authentication Server");

        errCode = executeHttpGetRequest(mHttpclientTampere, mTampereAuthLinkLogout);
      }
    } catch (Exception ex) {
      // Eat it!
      System.out.println("AuthentiKateMe! ERROR@signOut Tampere: " + ex.getMessage());
    }

    try {
      if (mHttpclientBangalore != null) {

        System.out.println(new Date() + " - Signing out of Bangalore Authentication Server");

        errCode = executeHttpGetRequest(mHttpclientBangalore, mBangaloreAuthLinkLogout);
      }
    } catch (Exception ex) {
      // Eat it!
      System.out.println("AuthentiKateMe! ERROR@signOut Bangalore: " + ex.getMessage());
    }

    try {
      if (mHttpclientMunich != null) {

        System.out.println(new Date() + " - Signing out of Munich Authentication Server");

        errCode = executeHttpGetRequest(mHttpclientMunich, mMunichAuthLinkLogout);
      }
    } catch (Exception ex) {
      // Eat it!
      System.out.println("AuthentiKateMe! ERROR@signOut Munich: " + ex.getMessage());
    }

    try {
      if (mHttpclientTampere != null)
        mHttpclientTampere.getConnectionManager().shutdown();
    } catch (Exception ex) {
      // Eat it!
      System.out.println("AuthentiKateMe! ERROR@signOut@shutdown Tampere: " + ex.getMessage());
    }

    try {
      if (mHttpclientBangalore != null)
        mHttpclientBangalore.getConnectionManager().shutdown();
    } catch (Exception ex) {
      // Eat it!
      System.out.println("AuthentiKateMe! ERROR@signOut@shutdown Bangalore: " + ex.getMessage());
    }

    try {
      if (mHttpclientMunich != null)
        mHttpclientMunich.getConnectionManager().shutdown();
    } catch (Exception ex) {
      // Eat it!
      System.out.println("AuthentiKateMe! ERROR@signOut@shutdown Munich: " + ex.getMessage());
    }

    return errCode;
  }

  private DefaultHttpClient getHttpConnection() {

    try {
      SSLContext ctx = SSLContext.getInstance("TLS");
      TrustManager tm = new X509TrustManager() {

        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {

          // Intentionally left blank!
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {

          // Intentionally left blank!
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {

          // Very generous ;)
          return null;
        }
      };

      ctx.init(null, new TrustManager[] { tm }, null);

      SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      DefaultHttpClient httpClient = new DefaultHttpClient();

      ClientConnectionManager ccm = httpClient.getConnectionManager();

      SchemeRegistry sr = ccm.getSchemeRegistry();
      sr.register(new Scheme("https", 443, ssf));

      return httpClient;

    } catch (Exception ex) {

      System.out.println("AuthentiKateMe! ERROR@getHttpConnection: " + ex.getMessage());
      return null;
    }
  }

  private ERROR_CODE executeHttpGetRequest(DefaultHttpClient httpclient, String httpURL) {

    HttpGet httpget = new HttpGet(httpURL);

    HttpResponse response = null;

    try {
      response = httpclient.execute(httpget);

      System.out.println(new Date() + " - " + response);

      HttpEntity entity = response.getEntity();
      EntityUtils.consume(entity);

      if (response.getStatusLine().toString().compareTo(HTTP_STATUS_200_OK) != 0)
        return ERROR_CODE.CONNECTION_FAILED;

    } catch (ClientProtocolException e) {

      System.out.println("AuthentiKateMe! ERROR@executeHttpGetRequest: " + e.getMessage());
      return ERROR_CODE.CONNECTION_FAILED;

    } catch (IOException e) {

      System.out.println("AuthentiKateMe! ERROR@executeHttpGetRequest: " + e.getMessage());
      return ERROR_CODE.CONNECTION_FAILED;
    }

    return ERROR_CODE.SUCCESS;
  }

  private ERROR_CODE executeHttpPostRequest(DefaultHttpClient httpclient, String httpURL, String username, String password, String strRealm) {

    HttpPost httpost = new HttpPost(httpURL);

    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    nvps.add(new BasicNameValuePair("username", username));
    nvps.add(new BasicNameValuePair("password", password));
    nvps.add(new BasicNameValuePair("tz_offset", "gmt"));
    nvps.add(new BasicNameValuePair("realm", strRealm)); // Tampere expects "NSN-AD, Munich & BLR expects "NSN AD"
    nvps.add(new BasicNameValuePair("btnSubmit", "Sign In"));
    nvps.add(new BasicNameValuePair("help", "Authentication with SecurID"));

    try {

      httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

      HttpResponse response = httpclient.execute(httpost);

      System.out.println(new Date() + " - " + response);

      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

      while ((rd.readLine()) != null) {
      }

      if (response.toString().contains("p=failed"))
        return ERROR_CODE.CREDENTIALS_WRONG;

      if (response.getStatusLine().toString().compareTo(HTTP_STATUS_200_OK) != 0)
        if (response.getStatusLine().toString().compareTo(HTTP_STATUS_302_MOVED) != 0)
          if (response.getStatusLine().toString().compareTo(HTTP_STATUS_302_FOUND) != 0) {
            return ERROR_CODE.CONNECTION_FAILED;
          }

    } catch (IllegalStateException e) {

      System.out.println("AuthentiKateMe! ERROR@executeHttpPostRequest: " + e.getMessage());

      return ERROR_CODE.CONNECTION_FAILED;

    } catch (IOException e) {

      System.out.println("AuthentiKateMe! ERROR@executeHttpPostRequest: " + e.getMessage());

      return ERROR_CODE.CONNECTION_FAILED;
    }

    return ERROR_CODE.SUCCESS;
  }

}

/*
 * LOG EXAMPLE ==>
 * 
 * HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8, Set-Cookie: DSEPAgentInstalled=; path=/; expires=Mon, 24-Dec-2001 17:17:34 GMT;
 * secure, Date: Thu, 22 Dec 2011 17:17:34 GMT, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store,
 * Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [Set-Cookie: DSASSERTREF=x; path=/; expires=Thu, 01 Jan 1970 22:00:00 GMT;
 * secure, Set-Cookie: DSID=e311a626c332e14ebfadffc491e9e157; path=/; secure, Set-Cookie: DSFirstAccess=1324574255; path=/; secure, Date:
 * Thu, 22 Dec 2011 17:17:35 GMT, location: https://logintolabra.tre.noklab.net/dana/home/starter0.cgi?check=yes, Content-Type: text/html;
 * charset=utf-8, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding:
 * chunked] HTTP/1.1 302 Moved [location: https://logintolabra.tre.noklab.net/dana/home/starter.cgi, Content-Type: text/html; charset=utf-8,
 * Set-Cookie: DSLastAccess=1324574254; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control:
 * no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location: https://logintolabra.tre.noklab.net/dana/home/eprun.cgi,
 * Content-Type: text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574254; path=/; Secure, Connection: Keep-Alive, Keep-Alive:
 * timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location:
 * https://logintolabra.tre.noklab.net/dana/home/infranet.cgi, Content-Type: text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574256;
 * path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1,
 * Transfer-Encoding: chunked] HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8, Date: Thu, 22 Dec 2011 17:17:36 GMT, Set-Cookie:
 * DSLastAccess=1324574256; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store,
 * Expires: -1, Transfer-Encoding: chunked]
 * 
 * HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8, Set-Cookie: DSEPAgentInstalled=; path=/; expires=Mon, 24-Dec-2001 17:17:44 GMT;
 * secure, Date: Thu, 22 Dec 2011 17:17:44 GMT, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store,
 * Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [Set-Cookie: DSASSERTREF=x; path=/; expires=Thu, 01 Jan 1970 22:00:00 GMT;
 * secure, Set-Cookie: DSID=fb3eee52ffbb547430afec75c5390540; path=/; secure, Set-Cookie: DSFirstAccess=1324574266; path=/; secure, Date:
 * Thu, 22 Dec 2011 17:17:46 GMT, location: https://10.58.148.122/dana/home/starter0.cgi?check=yes, Content-Type: text/html; charset=utf-8,
 * Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked]
 * HTTP/1.1 302 Moved [location: https://10.58.148.122/dana/home/starter.cgi, Content-Type: text/html; charset=utf-8, Set-Cookie:
 * DSLastAccess=1324574267; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store,
 * Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location: https://10.58.148.122/dana/home/eprun.cgi, Content-Type:
 * text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574267; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma:
 * no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location:
 * https://10.58.148.122/dana/home/infranet.cgi, Content-Type: text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574268; path=/;
 * Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding:
 * chunked] HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8, Date: Thu, 22 Dec 2011 17:17:49 GMT, Set-Cookie:
 * DSLastAccess=1324574268; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store,
 * Expires: -1, Transfer-Encoding: chunked]
 * 
 * 
 * HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8, Set-Cookie: DSEPAgentInstalled=; path=/; expires=Mon, 24-Dec-2001 17:17:53 GMT;
 * secure, Date: Thu, 22 Dec 2011 17:17:53 GMT, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store,
 * Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [Set-Cookie: DSASSERTREF=x; path=/; expires=Thu, 01 Jan 1970 22:00:00 GMT;
 * secure, Set-Cookie: DSID=1c1d172f5d88aa66a72b1d0ee0a6f6ee; path=/; secure, Set-Cookie: DSFirstAccess=1324574275; path=/; secure, Date:
 * Thu, 22 Dec 2011 17:17:55 GMT, location: https://logintolabra.mchm.nsn-rdnet.net/dana/home/starter0.cgi?check=yes, Content-Type:
 * text/html; charset=utf-8, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1,
 * Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location: https://logintolabra.mchm.nsn-rdnet.net/dana/home/starter.cgi, Content-Type:
 * text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574275; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma:
 * no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location:
 * https://logintolabra.mchm.nsn-rdnet.net/dana/home/eprun.cgi, Content-Type: text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574275;
 * path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1,
 * Transfer-Encoding: chunked] HTTP/1.1 302 Moved [location: https://logintolabra.mchm.nsn-rdnet.net/dana/home/infranet.cgi, Content-Type:
 * text/html; charset=utf-8, Set-Cookie: DSLastAccess=1324574276; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma:
 * no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8,
 * Date: Thu, 22 Dec 2011 17:17:57 GMT, Set-Cookie: DSLastAccess=1324574276; path=/; Secure, Connection: Keep-Alive, Keep-Alive: timeout=15,
 * Pragma: no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked]
 * 
 * 
 * AutentiKateMe! INFO: Exiting... HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8, Date: Thu, 22 Dec 2011 17:18:59 GMT, Connection:
 * Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 200 OK
 * [Content-Type: text/html; charset=utf-8, Date: Thu, 22 Dec 2011 17:19:10 GMT, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma:
 * no-cache, Cache-Control: no-store, Expires: -1, Transfer-Encoding: chunked] HTTP/1.1 200 OK [Content-Type: text/html; charset=utf-8,
 * Date: Thu, 22 Dec 2011 17:19:12 GMT, Connection: Keep-Alive, Keep-Alive: timeout=15, Pragma: no-cache, Cache-Control: no-store, Expires:
 * -1, Transfer-Encoding: chunked]
 */