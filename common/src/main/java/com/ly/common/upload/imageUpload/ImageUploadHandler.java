package com.ly.common.upload.imageUpload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.ly.common.upload.UploadFileFailedResult;
import com.ly.common.upload.UploadFileResult;
import com.ly.common.upload.UploadFileSuccessFulResult;

public class ImageUploadHandler {
  
  public UploadFileResult uploadImage(MultipartFile file, String dirPath, String imageName) {
    if (file.isEmpty()) {
      return new UploadFileFailedResult("文件是空的，上传失败...");
    }
    else {
      try {
        byte[] bytes = file.getBytes();
        String rootPathString = System.getProperty("catalina.home");
        File dirFile = new File(dirPath);
        
        if (!dirFile.exists()) {
          dirFile.mkdirs();
        }
        
        File imageFile = new File(dirFile.getPath() + File.separator + imageName);
        
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(imageFile));
        stream.write(bytes);
        stream.close();
        
        return new UploadFileSuccessFulResult("上传成功", imageFile.getPath());
      } catch (Exception e) {
        return new UploadFileFailedResult("上传失败");
      }
    }
  }
}
