package com.ly.sys.core.service;

import org.junit.Before;
import org.junit.Test;

import com.ly.common.encrypt.AES;

public class EncryptTest {
  AES aes;

  @Before
  public void setUp() {
    aes = new AES("2016.01_go2plus_chengdu_china");
  }

  @Test
  public void t1() {
    System.out.println("用户名："+aes.encrypt(""));
    System.out.println("密码："+aes.encrypt(""));
    System.out.println("平台："+aes.encrypt("db_go2"));
  }
}
