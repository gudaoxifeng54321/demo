package com.ly.common.mail;


import com.ly.common.X;
/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source
 * code file is protected by copyright law and international treaties.
 * Unauthorized distribution of source code files, programs, or portion of the
 * package, may result in severe civil and criminal penalties, and will be
 * prosecuted to the maximum extent under the law.
 * 邮件发送调用类
 * @author liyang
 * @since 2015-12-15
 */
public class SendEMail {

  /**
   * 调用此方法发送邮件
   * @param mailAdd 邮箱地址
   * @param content 内容
   * @param subject 标题
   * @return
   */
  public static boolean mail(String mailAdd,String subject,String content){
   // 这个类主要是设置邮件
   MailSenderInfo mailInfo = new MailSenderInfo();
   mailInfo.setMailServerHost(X.getConfig("com.go2plus.globle.email.mailServerHost"));
   mailInfo.setMailServerPort(X.getConfig("com.go2plus.globle.email.mailServerPort"));
   mailInfo.setValidate(true);
   mailInfo.setUserName(X.getConfig("com.go2plus.globle.email.userName"));
   mailInfo.setPassword(X.getConfig("com.go2plus.globle.email.password"));// 您的邮箱密码
   mailInfo.setFromAddress(X.getConfig("com.go2plus.globle.email.fromAddress"));
   mailInfo.setToAddress(mailAdd);
   mailInfo.setSubject(subject);//邮件标题
   mailInfo.setContent(content);
   // 这个类主要来发送邮件
  // SimpleMailSender sms = new SimpleMailSender();
   boolean result = SimpleMailSender.sendHtmlMail(mailInfo);// 发送文体格式
   // sms.sendHtmlMail(mailInfo);//发送html格式
   return result;
  }
}
