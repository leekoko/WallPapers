package com.xhban.util;

import com.xhban.constant.FileConstant;
import com.xhban.listener.DownloadFinishListener;

import java.io.*;

public class FileUtil {

    /*
        public static void main(String[] args) {
        String dir = FileConstant.IMG_PATH + "\\pictures";
        download(dir, "96.jpg");
    }*/

    /**
     * 复制锁屏图片
     */
    public static void download(String dir, String name){
        try (InputStream is = new FileInputStream(FileConstant.FILE_PATH);
             OutputStream os = new FileOutputStream(dir + "\\" + name)) {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
