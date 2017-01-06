package com.ly.common.qr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QREncoder {
  private static final int               BLACK  = 0xFF000000;
  private static final int               WHITE  = 0xFFFFFFFF;
  private static final MultiFormatWriter writer = new MultiFormatWriter();

  public static BufferedImage encode(String content, int width, int height) {
    BufferedImage bi = null;
    try {
      HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
      hints.put(EncodeHintType.MARGIN, 1);// 边框大小 默认为4
      hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 编码
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 容错率
      BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
      bi = toBufferedImage(bitMatrix);
    } catch (WriterException e) {
      e.printStackTrace();
    }
    return bi;
  }

  public static boolean encodeToFile(String content, int height, int width, String filePath, String format) {
    BufferedImage bi = encode(content, width, height);
    if (null != bi) {
      try {
        if (ImageIO.write(bi, format, new File(filePath))) {
          return true;
        } else {
          return false;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  // ------------------------------- = -------------------------------

  private static BufferedImage toBufferedImage(BitMatrix matrix) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
      }
    }
    return image;
  }
}
