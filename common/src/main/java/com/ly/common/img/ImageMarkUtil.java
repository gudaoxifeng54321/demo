package com.ly.common.img;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 图片水印
 * @author liyang
 *
 */
public class ImageMarkUtil {
  private static final Logger log = LoggerFactory.getLogger(ImageMarkUtil.class);
  
  
  
  
  /**   
   * 给图片添加水印   
   * @param iconPath 水印图片路径   
   * @param srcImgPath 源图片路径   
   * @param targerPath 目标图片路径   
   * @param alpha 透明度alpha 必须是范围 [0, 10] 之内（包含边界值）的一个整数
   */    
  public static void markImageByIcon(String iconPath, String srcImgPath,     
          String targerPath,int alpha) {    
      log.info("ImageMarkUtil.markImageByIcon()");
      markImageByIcon(iconPath, srcImgPath, targerPath, null,alpha,0,0) ;   
  } 
  
  /**
   * 给图片添加文字水印   
   * @param pressText 文字
   * @param srcImageFile 原图片
   * @param destImageFile 目标图片(添加文字水印后的图片)
   * @param alpha 透明度alpha 必须是范围 [0, 10] 之内（包含边界值）的一个整数
   * @param color
   */
  public static void markImageByText(String pressText, String srcImageFile, String destImageFile) {  
    log.info("ImageMarkUtil.markImageByText()");
      //设置字体
    Font font = new Font("黑体", Font.BOLD, 18);
    Color color = new Color(184, 184, 184);
      pressText(pressText,srcImageFile,destImageFile, 0,0, 5, -45,color,font);   
  }     
  
  /**
   * 给图片添加水印、可设置水印图片旋转角度   
   * @param iconPath 水印图片
   * @param srcImgPath 原图片
   * @param targerPath 水印后图片
   * @param degree 旋转角度
   * @param alpha 透明度alpha 必须是范围 [0, 10] 之内（包含边界值）的一个整数
   * @param x 横坐标
   * @param y 纵坐标
   */
  public static void markImageByIcon(String iconPath, String srcImgPath,     
          String targerPath, Integer degree,int alpha,int x,int y) {  
      log.info("ImageMarkUtil.markImageByIcon()");
      OutputStream os = null;     
      try {     
          Image srcImg = ImageIO.read(new File(srcImgPath));   
          BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),     
                  srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
          Graphics2D g = buffImg.createGraphics();     
  
          // 设置对线段的锯齿状边缘处理     
          g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
              RenderingHints.VALUE_INTERPOLATION_BILINEAR);     
          g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg     
                  .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);     
  
          if (null != degree) {     
              // 设置水印旋转     
              g.rotate(Math.toRadians(degree),     
                      (double) buffImg.getWidth() / 2, (double) buffImg     
                              .getHeight() / 2);     
          }     
          // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度    
          ImageIcon imgIcon = new ImageIcon(iconPath);     
          // 得到Image对象。     
          Image img = imgIcon.getImage();   
          //设置透明
          g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,(float)alpha/10));     
          // 表示水印图片的位置     
          g.drawImage(img, x, y, null);     
          g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));     
          g.dispose();     
          os = new FileOutputStream(targerPath);     
          // 生成图片     
          ImageIO.write(buffImg, "JPEG", os);     
      } catch (Exception e) {     
          e.printStackTrace();     
      } finally {     
          try {     
              if (null != os)     
                  os.close();     
          } catch (Exception e) {     
              e.printStackTrace();     
          }     
      }     
  }    
  
  
  /**
   * 给图片添加文字水印
   * @param pressText 水印文字
   * @param srcImageFile 源图像地址
   * @param destImageFile 目标图像地址
   * @param color 水印的字体颜色
   * @param fontSize 水印的字体大小
   * @param x 修正值
   * @param y 修正值
   * @param alpha 透明度：alpha 必须是范围 [0, 10] 之内（包含边界值）的一个整数
   * @param degree旋转角度
   */
   public final static void pressText(String pressText, String srcImageFile, String destImageFile, int x, int y, int alpha,Integer degree,Color color,Font font) {
     long startTime = System.currentTimeMillis();
     log.info("开始时间:"+startTime);
     log.info("水印文字:"+pressText);
     try {
       File img = new File(srcImageFile);
       Image src = ImageIO.read(img);
       //获取图片宽度和高度
       int width = src.getWidth(null);
       int height = src.getHeight(null);
       BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       Graphics2D g = image.createGraphics();
       // 开文字抗锯齿 去文字毛刺
       g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
       g.drawImage(src, 0, 0, width, height, null);
       // 设置颜色
       g.setColor(color);
       if (null != degree) {     
         // 设置水印旋转     
         g.rotate(Math.toRadians(degree),     
                 (double) image.getWidth() / 2, (double) image     
                         .getHeight() / 2);     
         }    
       // 设置 Font
       g.setFont(font);
       g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float)alpha/10));
       // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
       int length = pressText.length();
       //int space = 120;
       int spaceX = font.getSize()*length;
       int spaceY = font.getSize() + 80;
       double diagonal  = Math.sqrt(height*height+width*width);
       int diagonalInt = (int)diagonal;
       for (int i = -diagonalInt; i < width; i=i+spaceX) {
         for (int j = -diagonalInt; j < diagonalInt; j=j+spaceY) {
           g.drawString(pressText, i+j, j);
         }
       }
       g.dispose();
       ImageIO.write((BufferedImage) image, "PNG", new File(destImageFile));// 输出到文件流
       log.info("文字水印花费时间:"+(System.currentTimeMillis()-startTime)/1000+"秒");
     } catch (Exception e) {
       log.info(null,e);
     }
   }
 
  public static void main(String[] args) {
    String srcImgPath = "e://test/123.jpg";     
    String iconPath = "e://test/6.jpg";     
    String targerPath = "e://test/1234.png" ; 
    String textMarkPath = "e://test/Desert.jpg";
     // 给图片添加水印     
    //markImageByIcon(iconPath, srcImgPath, targerPath , -45,1,0,0);   
    //文字水印
    markImageByText("GO2.CN星购途鞋类市场",textMarkPath,targerPath);
    //设置字体
    Font font = new Font("黑体", Font.BOLD, 30);
    //pressText("GO2.CN", textMarkPath, targerPath, 0, 0, 9, -25, Color.GRAY, font);
        
  }
  
}
