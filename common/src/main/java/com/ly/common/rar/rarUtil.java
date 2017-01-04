package com.ly.common.rar;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;
import com.ly.common.io.FileUtil;
import com.ly.common.shell.Terminal;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * Rar 压缩解压工具类, 文件 解压.rar 文件
 * 
 * 仅仅适用于linux,采用命令解压方式
 * @author liyang
 *
 */
public class rarUtil {
  private final static Logger log = LoggerFactory.getLogger(rarUtil.class);
  
  /**
   * 将指定的rar文件 压缩到指定的目录,按照rar中的目录结构原样输出
   * 
   * @param sourceFilePath
   *          (/Users/gaofeng/Desktop/1.zip)
   * @param destinationFilePath
   *          (/Users/gaofeng/Desktop/)
   * @throws Exception
   */
  public static boolean unrar(String sourceFilePath, String destinationFilePath) throws Exception {
    long startTime = System.currentTimeMillis();
    File outDirFile = null;
    // 创建输出文件夹对象
    if (destinationFilePath == null) {
      //未指定输出路径,默认用源rar文件名作为输出目录
      outDirFile= new File(sourceFilePath.substring(0, sourceFilePath.length()-4));
    } else {
      outDirFile = new File(destinationFilePath);
    }

    if (!outDirFile.isDirectory())
      outDirFile.mkdirs();
    File sourceFile = new File(sourceFilePath);
    String extension = FileUtil.getExtension(sourceFilePath);
    if (!extension.equalsIgnoreCase(X.RAR_FILE)) {
      throw new Exception("sourceFile is not a rar");
    }
    if (!sourceFile.exists()) {
      throw new Exception("sourceFile not found!");
    }
    //开始解压
    try {
      //调用linux命令解压到outDirFile文件夹
      String[] result = Terminal.execute("unrar x "+sourceFile+" "+outDirFile, ".", true);
      if(X.SUCCESS.equals(result[0])){
        log.info(sourceFilePath+"rar解压成功!");
        long endTime = System.currentTimeMillis();
        log.debug("@@Rar decompression : {}  -----------------  duration ： {} ms", sourceFilePath, endTime - startTime);
        return true;
      }else{
        log.info("错误原因:"+result[1]);
        log.info(sourceFilePath+"rar解压失败!");
        return false;
      }
    } catch (Exception e) {
      log.info(sourceFilePath+"rar解压失败!");
      log.info(null,e);
      return false;
    }
  }
  
  
  /**
   * 将指定的rar文件 压缩到指定的目录,忽略所有rar内部的子目录, 将所有文件 按照 目录1_目录2_文件.txt 的对应方式平铺在destinationFilePath 所指定的目录下,(其下再无其他子目录). 该类用于将用户上传的不标准的压缩包 按照统一标准来解压.
   * 便于后续处理.
   * 
   * @param sourceFilePath
   *          (/Users/gaofeng/Desktop/1.zip)
   * @param destinationFilePath
   *          (/Users/gaofeng/Desktop/)
   * @throws Exception
   */
  public static boolean unrarToSingleFolder(String sourceFilePath, String destinationFilePath) throws Exception {
    long startTime = System.currentTimeMillis();
    File outDirFile = null;
    // 创建输出文件夹对象
    if (destinationFilePath == null) {
      //未指定输出路径,默认用源rar文件名作为输出目录
      outDirFile= new File(sourceFilePath.substring(0, sourceFilePath.length()-4));
    } else {
      outDirFile = new File(destinationFilePath);
    }

    if (!outDirFile.isDirectory())
      outDirFile.mkdirs();
    File sourceFile = new File(sourceFilePath);
    String extension = FileUtil.getExtension(sourceFilePath);
    if (!extension.equalsIgnoreCase(X.RAR_FILE)) {
      throw new Exception("sourceFile is not a rar");
    }
    if (!sourceFile.exists()) {
      throw new Exception("sourceFile not found!");
    }
    //开始解压
    try {
      //调用linux命令解压到outDirFile文件夹
      String[] result = Terminal.execute("unrar x "+sourceFile+" "+outDirFile, ".", true);
      if(X.SUCCESS.equals(result[0])){
      //遍历outDirFile目录,忽略子文件夹，最终删除子文件夹
        File[] picfiles = outDirFile.listFiles();
        for (int i = 0; i < picfiles.length; i++) {
          File picFile = picfiles[i];
          if(picFile.isFile()){
            continue;
          }
          copyFileToFolder(picFile, outDirFile,outDirFile.getPath());
        }
        //删除子文件夹
        for (int i = 0; i < picfiles.length; i++) {
          File dirFile = picfiles[i];
          if(dirFile.isDirectory()){
              FileUtil.deleteFolder(dirFile);
          }
        }
        long endTime = System.currentTimeMillis();
        log.debug("@@Rar decompression : {}  -----------------  duration ： {} ms", sourceFilePath, endTime - startTime);
        return true;
      }else{
        log.info(sourceFilePath+"rar解压失败!");
        return false;
      }
    } catch (Exception e) {
      log.info(sourceFilePath+"rar解压失败!");
      log.info(null,e);
      return false;
    }
  }
  
  /**
   * copy文件夹下的所有文件到目标文件夹
   * @param src
   * @param des
   */ 
  public static boolean copyFileToFolder(File src,File des,String destinationFilePath){
    if(!src.exists()){
      return false;
    }
    //遍历该文件夹下的所有文件包括文件夹
    File[] files = src.listFiles();
    for (File file : files) {
      //如果文件是文件夹，回调本函数
      if(file.isDirectory()){
        copyFileToFolder(file, des,destinationFilePath);
      }else{
        //执行copy
        File old = new File(file.getPath());
        try {
          //文件重命名
          String fileName = old.getPath();
          fileName = fileName.replaceAll("\\\\", "_").replaceAll("/", "_");
          fileName = fileName.substring(destinationFilePath.length()+1);
          File newFile = new File(old.getParent(), fileName);
          old.renameTo(newFile);
          FileUtils.copyFileToDirectory(newFile, des);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return true;
  }
  
  public static void main(String[] args) {
    try {
      unrarToSingleFolder("E:/public_test/test/publish/sdn123/288/original", "e://rarTest/rarTest/original");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
