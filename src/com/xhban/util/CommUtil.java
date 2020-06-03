package com.xhban.util;

import com.xhban.constant.FileConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CommUtil {
	public static int getMax(int[] array) {
		Arrays.sort(array);
		return array[array.length - 1];
	}

	/**
	 * 日志记录
	 */
	public static void saveLog(int state, String message) {
		FileOutputStream out = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String name = sdf.format(date);
		try {
			File saveDir = null;
			if (state == 1) {
				saveDir = new File(FileConstant.IMG_PATH + "\\logs");
			} else if (state == 0) {
				saveDir = new File(FileConstant.IMG_PATH + "\\errors");
			}
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			File file = new File(saveDir + "\\" + name + ".txt");
			out = new FileOutputStream(file);
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(date);
			String log = "时间:" + time + ",  " + message;
			out.write(log.getBytes());
		} catch (Exception e) {
		}finally {
			//删除非今日的日志
			deleteOtherLog(name);
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 删除非今日的日志
	 * @param todayFileName
	 */
	private static void deleteOtherLog(String todayFileName) {
		String dir = FileConstant.IMG_PATH + "\\logs";
		File saveDir = new File(dir);
		File[] allFiles = saveDir.listFiles();
		for (File file : allFiles) {
			if(file.getName().equals(todayFileName)){
				continue;
			}
			file.delete();
		}
	}
}
