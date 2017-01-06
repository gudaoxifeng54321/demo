package com.ly.common.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source
 * code file is protected by copyright law and international treaties.
 * Unauthorized distribution of source code files, programs, or portion of the
 * package, may result in severe civil and criminal penalties, and will be
 * prosecuted to the maximum extent under the law.
 * 密码验证器 
 * @author liyang
 * @since 2015-12-15
 */
public class GoPlusAuthenticator extends Authenticator{
  String userName = null;
  String password = null;
  public GoPlusAuthenticator() {
  }
  public GoPlusAuthenticator(String username, String password) {
   this.userName = username;
   this.password = password;
  }
  protected PasswordAuthentication getPasswordAuthentication() {
   return new PasswordAuthentication(userName, password);
  }
}
