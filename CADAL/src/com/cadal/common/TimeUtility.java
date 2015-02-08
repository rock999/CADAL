package com.cadal.common;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtility {

	public static void main(String[] args) {

		try {
			String time1 = "2014-06-02 17:32:16";
			String time2="2014-06-03 19:32:16";
			getDiffMinute(time1, time2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 获得两个时间的分钟时间差
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	static long getDiffMinute(String beginDate, String endDate) {
		long diffValue = 0;
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date begin = dfs.parse(beginDate);
			java.util.Date end = dfs.parse(endDate);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			long day1 = between / (24 * 3600);
			long hour1 = between % (24 * 3600) / 3600;
			long minute1 = between % 3600 / 60;
			long second1 = between % 60 / 60;
			System.out.println("request time diff: " + day1 + "天" + hour1
					+ "小时" + minute1 + "分" + second1 + "秒");

			diffValue = minute1 + hour1 * 60 + day1 * 24 * 60;// 计算总的分钟数
			System.out.println(diffValue);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return diffValue;
	}

	/**
	 * get Current Date Str format:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTimeStr() {
		Date dt = new Date();
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dfs.format(dt);
	}

}
