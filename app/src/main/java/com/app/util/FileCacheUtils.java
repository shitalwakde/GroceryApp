package com.app.util;

import android.graphics.Bitmap;

import com.app.constant.AppConstant;
import com.app.controller.AppController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import androidx.core.content.ContextCompat;

public class FileCacheUtils {

    public static boolean isImageDownloaded(String path) {
        File file = AppController.getInstance().getCacheDir();
        if(file.exists() && file.isDirectory() && file.list()!=null){
            return (file.list().length>0 && Arrays.asList(file.list()).contains(getImageFileName(path)));
        }
        return false;
    }

    public static File storeImage(String imageUri, Bitmap loadedImage) {

        File file=new File(AppController.getInstance().getCacheDir(),getImageFileName(imageUri));
        try (FileOutputStream out = new FileOutputStream(file)) {
            loadedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
            file=null;
        }
        return file;
    }

    private static String getImageFileName(String imageUri) {
        //return imageUri.substring(imageUri.lastIndexOf("/")+1,imageUri.length());
        return imageUri.substring(imageUri.lastIndexOf("/"),imageUri.length());
    }

    public static File retriveImage(String imgPath) {
        File imageFile=null;
        try {
            File file = AppController.getInstance().getCacheDir();
            if (file.exists() && file.isDirectory() && file.list()!=null) {
                for (String f : file.list()) {
                    if (f.equalsIgnoreCase(getImageFileName(imgPath)))
                        imageFile = new File(f);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageFile;
    }
}
