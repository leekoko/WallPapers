package ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.xhban.util.DownUtil;

public class Main {

	//每隔60分钟重复执行一次（毫秒单位）
	private static int PEROID = 60 * 60 * 1000;


	public static void main(String[] args) {
		downloadTimer();
	}

	public static void downloadTimer() {
		// 设置执行时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);// 每天
		Date date = calendar.getTime();
		calendar.set(year, month, day, 9, 00, 00);
		Timer timer = new Timer();
		// 第一次执行
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				DownUtil.downWallPaper();
			}
		};
		//定时执行
		timer.schedule(task, date, PEROID);
	}
}
