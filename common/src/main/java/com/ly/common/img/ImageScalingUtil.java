package com.ly.common.img;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WriteParameter;
import com.alibaba.simpleimage.render.WriteRender;
import com.ly.common.X;
import com.ly.common.io.FileUtil;

public class ImageScalingUtil {
  private static final Logger log = LoggerFactory.getLogger(ImageScalingUtil.class);

  /**
   * 
   * @param originalFile
   * @param resizedFile
   * @param targetWidth
   * @param quality
   * @param isLarge 是否放大
   * @return 返回缩放后长度，如果返回-1缩放失败，如果返回0:则是不允许方法返回值(没有进行缩放)
   * @throws Exception
   */
  public static int resize(File originalFile, File resizedFile, int targetWidth, float quality,boolean isLarge) throws Exception {
    long t = System.currentTimeMillis();
    try {
      //BufferedImage bi = ImageIO.read(originalFile);
      Image src=Toolkit.getDefaultToolkit().getImage(originalFile.getPath());  
      BufferedImage bi=BufferedImageBuilder.toBufferedImage(src);//Image to BufferedImage 
      if(null==bi){
        return -1; 
      }
      int w = bi.getWidth();
      //判断原图的宽度<缩放后的宽度
      if(!isLarge){
        if(w<=targetWidth){
          return 0;
        }
      }
      int h = bi.getHeight();
      float r = targetWidth * 1.0f / w;
      int targetHeight = (int) (h * r);
      Image itemp = bi.getScaledInstance(targetWidth, targetHeight, BufferedImage.SCALE_SMOOTH);
      BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);    //TYPE_INT_RGB
      Graphics2D g = bufferedImage.createGraphics();
      g.drawImage(itemp, 0, 0, null);
      g.dispose();
      FileOutputStream out = new FileOutputStream(resizedFile);
      ImageWriter imageWriter = (ImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
      ImageOutputStream ios = ImageIO.createImageOutputStream(out);
      imageWriter.setOutput(ios);
      JPEGImageWriteParam jpegParams = null;
      if (quality >= 0 && quality <= 1) {
        jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
        jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(quality);
      }
      imageWriter.write(null, new IIOImage(bufferedImage, null, null), jpegParams);
      ios.close();
      out.flush();
      out.close();
      imageWriter.dispose();
      log.debug("targetWidth : {}  quality : {}  time: {}", targetWidth, quality, (System.currentTimeMillis() - t));
      return targetHeight;
    } catch (Exception e) {
      log.info(null,e);
      return 0;
    }
   
  }

  
  /**
   * 采用阿里缩放图片
   * @param originalFile
   * @param resizedFile
   * @param targetWidth
   * @param quality 图片质量
   * @param isLarge
   * @return
   * @throws Exception
   */
  public static int resize2(File originalFile, File resizedFile, int targetWidth, float quality,boolean isLarge) throws Exception {
    log.info("ImageScalingUtil.resize2()");
    long t = System.currentTimeMillis();
    String extension = FileUtil.getExtension(originalFile.getName());
    BufferedImage bi = ImageIO.read(originalFile);
    if(null==bi){
      return -1; 
    }
    int w = bi.getWidth();
  //判断原图的宽度<缩放后的宽度
    if(!isLarge){
      if(w<=targetWidth){
        return 0;
      }
    }
    int h = bi.getHeight();
    float r = targetWidth * 1.0f / w;
    int targetHeight = (int) (h * r);
    ScaleParameter scaleParam = new ScaleParameter(targetWidth, targetHeight);  //将图像缩略到1024x1024以内，不足1024x1024则不做任何处理
    FileInputStream inStream = null;
    FileOutputStream outStream = null;
    WriteRender wr = null;
    try {
        inStream = new FileInputStream(originalFile);
        outStream = new FileOutputStream(resizedFile);
        ImageRender rr = new ReadRender(inStream);
        ImageRender sr = new ScaleRender(rr, scaleParam);
        WriteParameter writeParameter = new WriteParameter();
        writeParameter.setDefaultQuality(quality);
        wr = new WriteRender(sr, outStream,X.getImageFormat(extension),writeParameter);
        wr.render();                            //触发图像处理
    } catch(Exception e) {
      log.info(null,e); 
      return 0;
    } finally {
        IOUtils.closeQuietly(inStream);         //图片文件输入输出流必须记得关闭
        IOUtils.closeQuietly(outStream);
        if (wr != null) {
            try {
                wr.dispose();                   //释放simpleImage的内部资源
            } catch (SimpleImageException ignore) {
                log.info(null,ignore); 
            }
        }
    }
    log.debug("targetWidth : {}  quality : {}  time: {}", targetWidth, quality, (System.currentTimeMillis() - t));
    return targetHeight;
  }
  
  
  public static void main(String[] args) throws Exception {
    File originalImage = new File("E://test/10.jpg");
    resize(originalImage, new File("E://test/101.jpg"), 750, 0.9f,true);
    resize2(originalImage, new File("E://test/102.jpg"), 750, 0.9f,true);
    /*File originalImage = new File("/Users/gaofeng/Desktop/img/x.jpg");
    for (int i = 100; i >= 90; i -= 2) {
      resize(originalImage, new File("/Users/gaofeng/Desktop/img/x750_" + i + ".jpg"), 750, (float) i / 100,false);
    }*/
  }
}
