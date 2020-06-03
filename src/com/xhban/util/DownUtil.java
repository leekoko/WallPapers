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
	 * ��鵱��������Ƿ��Ѿ�ִ�У���ֹ�ظ�����
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
					CommUtil.saveLog(FAILED, "����ͼƬ��ַʱ�쳣: " + ex.toString());
				}
			});
			singleExecutor.execute(()-> {
				//�ӱ��ػ�ȡ
				FileUtil.download(dir, getFileName());
			});
		}
	}


	// ����ͼƬ
	public static void downPhoto(String picAddress) {
		//�Ǵ�Web�˻�ȡ
		HttpUtil.download(picAddress, dir, getFileName(), new DownloadFinishListener() {

			@Override
			public void finish() {
				CommUtil.saveLog(SUCCEED, "���سɹ�");
			}

			@Override
			public void error(Exception ex) {
				CommUtil.saveLog(FAILED, "����ͼƬʱ�쳣�� " + ex.toString());
			}
		});
	}

	/**
	 * ��ȡ�ļ���
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
		// �ļ��д���ͼƬ,��ȡ������ƣ������1��Ϊ��ͼƬ������
		int names[] = new int[fileCount];
		for (int i = 0; i < fileCount; i++) {
			String fileName = allFiles[i].getName();
			names[i] = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf(".")));
		}
		String name = CommUtil.getMax(names) + 1 + ".jpg";
		return name;
	}



}
