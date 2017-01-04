package com.ly.common.mvc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;
import com.ly.common.encrypt.MD5;
import com.ly.common.web.VerificationPictureGenerator;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source
 * code file is protected by copyright law and international treaties.
 * Unauthorized distribution of source code files, programs, or portion of the
 * package, may result in severe civil and criminal penalties, and will be
 * prosecuted to the maximum extent under the law.
 * 
 * 所有Controller 基类, 提供一些对box操作的公共方法
 * 
 * @author gaofeng
 * @since 2015-10-30
 */
public class BaseVerificationController extends BaseController {
  private static final Logger        log                    = LoggerFactory.getLogger(BaseVerificationController.class);
  private static final int           VERIFICATION_POOL_SIZE = 50;//验证码池大小 指定一共生成多少张验证码
  private Map<String, BufferedImage> verificationPool       = VerificationPictureGenerator.generate(VERIFICATION_POOL_SIZE);
  private ArrayList<String>          verificationKeys       = new ArrayList<String>(VERIFICATION_POOL_SIZE);

  public BaseVerificationController() {
    verificationKeys.addAll(verificationPool.keySet());
  }

  protected String outputVerification(Box box, HttpServletResponse response) throws Exception {
    String key = verificationKeys.get(X.RANDOM.nextInt(VERIFICATION_POOL_SIZE));
    try {
      //验证码 存cookie中需要加密
      Cookie c = new Cookie(X.ENCRYPTED+X.KEY, key);
      c.setMaxAge(-1);
      box.setCookie(X.ENCRYPTED+X.KEY, c);
      //设置时间
      String time = X.nowString();
      Cookie t = new Cookie(X.ENCRYPTED+X.TIME, time);//(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())
      t.setMaxAge(-1);
      box.setCookie(X.ENCRYPTED+X.TIME, t);
      //HASH 
      String hash = MD5.md5Encode(MD5.md5Encode(key.toLowerCase()+X.USER_PASS_PREFIX)+X.USER_PASS_PREFIX+time);
      Cookie h = new Cookie(X.ENCRYPTED+"hash", hash);
      c.setMaxAge(-1);
      box.setCookie(X.ENCRYPTED+"hash", h);
		
      writeCookies(box, response);
      ImageIO.write(verificationPool.get(key), "JPEG", response.getOutputStream());
      return key;
    } catch (IOException e) {
      log.error(e.getMessage());
      return X.EMPTY;
    }
  }
}