package com.ly.common.img;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


  public class BufferedImageBuilder {
    private final static Logger log = LoggerFactory.getLogger(BufferedImageBuilder.class);
    public static BufferedImage toBufferedImage(Image image) {  
      try {
        if (image instanceof BufferedImage) {  
          return (BufferedImage) image;  
      }  
      // This code ensures that all the pixels in the image are loaded  
      image = new ImageIcon(image).getImage();  
      BufferedImage bimage = null;  
      GraphicsEnvironment ge = GraphicsEnvironment  
              .getLocalGraphicsEnvironment();  
      try {  
          int transparency = Transparency.OPAQUE;  
          GraphicsDevice gs = ge.getDefaultScreenDevice();  
          GraphicsConfiguration gc = gs.getDefaultConfiguration();  
          bimage = gc.createCompatibleImage(image.getWidth(null),  
                  image.getHeight(null), transparency);  
      } catch (HeadlessException e) {  
        //log.error("缩放图片错误",e);
      }  
      if (bimage == null) {  
          // Create a buffered image using the default color model  
          int type = BufferedImage.TYPE_INT_RGB;  
          bimage = new BufferedImage(image.getWidth(null),  
                  image.getHeight(null), type);  
      }  
      // Copy image to buffered image  
      Graphics g = bimage.createGraphics();  
      // Paint the image onto the buffered image  
      g.drawImage(image, 0, 0, null);  
      g.dispose();  
      return bimage;  
      } catch (Exception e) {
        log.error("缩放图片错误",e);
      }
      return null;
  }  
}
