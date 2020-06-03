package com.xhban.util;

import com.xhban.constant.FileConstant;
import com.xhban.listener.DownloadFinishListener;
import com.xhban.listener.RequestFinishListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownUtil {
	private final static int FAILED = 0;
	private final static int SUCCEED = 1;
	private final static String dir = FileConstant.IMG_PATH + "\\pictures";
	private static ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

	/**
	 * 检查当天的下载是否已经执行，防止重复下载
	 * 
	 * @return
	 */
	private static boolean check() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String name = sdf.format(date);
		File file1 = new File(FileConstant.IMG_PATH + "\\logs\\" + name + ".txt");
		if (file1.exists()) {
			return true;
		}
		return false;
	}



	public static void downWallPaper() {
		if (!check()) {
			String requestAddress = "http://guolin.tech/api/bing_pic";
			HttpUtil.doGet(requestAddress, new RequestFinishListener() {

				@Override
				public void finish(String response) {
					singleExecutor.execute(()-> {
						downPhoto(response);
					});
				}

				@Override
				public void error(Exception ex) {
					CommUtil.saveLog(FAILED, "请求图片网址时异常: " + ex.toString());
				}
			});
			singleExecutor.execute(()-> {
				//从本地获取
				FileUtil.download(dir, getFileName());
			});
		}
	}


	// 下载图片
	public static void downPhoto(String picAddress) {
		//是从Web端获取
		HttpUtil.download(picAddress, dir, getFileName(), new DownloadFinishListener() {

			@Override
			public void finish() {
				CommUtil.saveLog(SUCCEED, "下载成功");
			}

			@Override
			public void error(Exception ex) {
				CommUtil.saveLog(FAILED, "下载图片时异常： " + ex.toString());
			}
		});
	}

	/**
	 * 获取文件名
	 * @return
	 */
	private static String getFileName(){
		File saveDir = new File(dir);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File[] allFiles = saveDir.listFiles();
		if(allFiles == null){
			return "1.jpg";
		}
		int fileCount = allFiles.length;
		// 文件夹存在图片,获取最大名称，将其加1作为新图片的名称
		int names[] = new int[fileCount];
		for (int i = 0; i < fileCount; i++) {
			String fileName = allFiles[i].getName();
			names[i] = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf(".")));
		}
		String name = CommUtil.getMax(names) + 1 + ".jpg";
		return name;
	}



}
