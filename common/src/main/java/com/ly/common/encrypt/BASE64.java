package com.ly.common.encrypt;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 通过BASE64算法对文本进行编码解码
 * 
 * @author Gaffer
 */
public final class BASE64 {
  private static BASE64Encoder encoder = new BASE64Encoder();
  private static BASE64Decoder decoder = new BASE64Decoder();

  public static String encrypt(String source) {
    return encoder.encode(source.getBytes());
  }

  public static String decrypt(String encrypted) {
    String decrypted = null;
    try {
      decrypted = new String(decoder.decodeBuffer(encrypted));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return decrypted;
  }
}
