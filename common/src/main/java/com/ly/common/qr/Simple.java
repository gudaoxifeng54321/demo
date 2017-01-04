package com.ly.common.qr;

import com.google.zxing.Result;

public class Simple {
  public static void main(String[] args) {
    // bug();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 2000; i++) {
      sb.append((char) i);
    }
    String s = "http://localhost:8080/ksp/knowledge/knowledge/12345678-12345678-12345678-12345678";
    for (int i = 10; i < 1000; i += 10) {
      QREncoder.encodeToFile(s, i, i, "f:/Temp/qr" + i + ".png", "png");
    }

    // Result r = QRDecoder.decodeFromFile("d:/tmp/qr.png");
    // Map<String,String> map=new HashMap<String, String>();
    // Iterator<String> io=map.keySet().iterator();
    // while(io.hasNext()){
    // map.get(io.next());
    // }
    // System.out.println(r);
  }

  public static void bug() {
    QREncoder.encodeToFile("{URLTO:http://baidu.com}", 400, 400, "d:/tmp/bd.png", "png");
    Result r = QRDecoder.decodeFromFile("d:/tmp/bd.png");
    System.out.println(r);
  }
}
