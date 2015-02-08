package com.cadal.common;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class LogTools {
	private static Logger log = Logger.getLogger(LogTools.class);

	public static DefaultHttpClient getHttpClient() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpHost target = new HttpHost("http://www.cadal.zju.edu.cn", 80,
				"http");

		return httpclient;
	}

	/***
	 * 
	 */
	public static boolean getStatus(String cookie) {

		// String url="http://www.cadal.zju.edu.cn/index";
		String url = "http://www.cadal.zju.edu.cn/page/01023329/9999";
		boolean status = false;
		DefaultHttpClient httpclient = getHttpClient();
		HttpGet httpget = new HttpGet(url);
		// add headers
		httpget.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
		httpget.addHeader("Host", "www.cadal.zju.edu.cn");

		httpget.addHeader("Cookie", cookie);
		String content = null;
		try {
			// Call Default page
			HttpResponse response = httpclient.execute(httpget);

			// 判断IP是否可用
			boolean ipAvailable = response.getStatusLine().getStatusCode() == 200 ? true
					: false;

			HttpEntity entity = response.getEntity();

			content = EntityUtils.toString(entity);

			// 判断cookie是否可用
			boolean cookAvailable = content.indexOf("data shameful") < 0 ? true
					: false;

			status = ipAvailable && cookAvailable;

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}

	

	public static void main(String[] args) {
		// log.debug("====log4j: debug");
		// log.info("====log4j: info");
		// log.warn("====log4j: warn");
		// log.error("====log4j: error");
		// log.fatal("====log4j: fatal");

	}
}
