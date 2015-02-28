package com.cadal.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author haiming.yin
 */
public class IPTools {

	public static DefaultHttpClient getHttpClient() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		return httpclient;
	}

	public static long lastTime = System.currentTimeMillis() - 100000000;
	static FileHelper fhelper = new FileHelper();

	/***
	 * sync switch ip address, interval must less than 60sec
	 */
	public static synchronized boolean switchIP(String url) {

		// 计算变更间隔，如果六十秒之间则不变更
		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastTime) < 90000)
			return true;

		lastTime = currentTime;

		boolean isSucessful = false;
		DefaultHttpClient httpclient = getHttpClient();
		HttpGet httpget = new HttpGet(url);
		// add headers
		httpget.addHeader("Accept-Language",
				"zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
		httpget.addHeader("Authorization", "Basic YWRtaW46bG92ZWFubmU=");
		httpget.addHeader(
				"Accept",
				"image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, */*");
		httpget.addHeader(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; WOW64; Trident/6.0; .NET4.0E; .NET4.0C; .NET CLR 3.5.30729; .NET CLR 2.0.50727; .NET CLR 3.0.30729)");
		httpget.addHeader("Accept-Encoding", "gzip, deflate");
		String content = null;
		try {
			// Call Default page
			HttpResponse response = httpclient.execute(httpget);

			isSucessful = response.getStatusLine().getStatusCode() == 200 ? true
					: false;

			// HttpEntity entity = response.getEntity();
			// content = EntityUtils.toString(entity);
			// System.out.println(content);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print switch opera result
		System.out.println("swith ip opera#" + TimeUtility.getCurrentTimeStr()
				+ "#" + isSucessful);

		// record swith ip result
		fhelper.appendDataToFile(
				"log",
				"switchIP.log",
				TimeUtility.getCurrentTimeStr() + " operaStatus#" + isSucessful,
				"UTF-8", true);
		return isSucessful;
	}

	public static void main(String[] args) {

		IPTools switcher = new IPTools();
		for (int i = 0; i < 100; i++)
			switcher.switchIP("http://192.168.1.1/userRpm/StatusRpm.htm?Disconnect=%B6%CF%20%CF%DF&wan=1");
		// System.out.println(switcher.switchCookie(""));

		// int i = 5;
		// while (--i > 0) {
		// System.out
		// .println(switchProxy("http://192.168.1.1/userRpm/StatusRpm.htm?Disconnect=%B6%CF%20%CF%DF&wan=1"));
		// try {
		// Thread.sleep(60000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

	}

}