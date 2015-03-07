package com.cadal.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author haiming.yin
 */
public class NetTools {

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
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpget.addHeader("Cookie",
				"Cookie:Authorization=Basic YWRtaW46YWJjZDEyMzQ=; ChgPwdSubTag");
		httpget.addHeader(
				"Accept",
				"image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, */*");
		httpget.addHeader(
				"User-Agent",
				"ozilla/5.0 (Linux; Android 4.2.2; GT-I9505 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36");
		httpget.addHeader("Accept-Encoding", "gzip, deflate");

		httpget.addHeader("Referer", "http://192.168.1.1/userRpm/StatusRpm.htm");
		String content = null;
		try {
			// Call Default page
			HttpResponse response = httpclient.execute(httpget);

			isSucessful = response.getStatusLine().getStatusCode() == 200 ? true
					: false;

			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			System.out.println(content);
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

		NetTools switcher = new NetTools();
		// for (int i = 0; i < 100; i++)
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