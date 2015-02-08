package com.cadal.common;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * auto register
 */
public class AutoLogin {
	public static void main(String[] args) throws Exception {

		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		// localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		// // before 4.3

		// 存储Cookie
		// httpclient.setCookieStore(cookieStore);
		// 目标地址
		HttpPost httppost = new HttpPost(
				"http://www.cadal.zju.edu.cn/account/signincheck//");

		httppost.addHeader("Host", "www.cadal.zju.edu.cn");
		httppost.addHeader("Origin", "http://www.cadal.zju.edu.cn");
		httppost.addHeader("Referer",
				"http://www.cadal.zju.edu.cn/account/signin");
		httppost.addHeader(
				"User-Agent",
				"ser-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");

		System.out.println("请求: " + httppost.getRequestLine());

		// username:robin1235
		// email:robin1235@qq.com
		// password:111111
		// password_confirm:111111
		// occupation:数学
		// gender:男
		// birthday:
		// school:西安交通大学
		// province:江苏
		// city:南通
		// interest: 电影

		// 构造最简单的字符串数据
		StringEntity reqEntity = new StringEntity(
				"username=rock999&password=rock111111&remember=on");
		// 设置类型
		reqEntity.setContentType("application/x-www-form-urlencoded");
		// 设置请求的数据
		httppost.setEntity(reqEntity);
		// 执行
		HttpResponse response = httpclient.execute(httppost, localContext);
		for (Header header : response.getAllHeaders()) {

		}

		HttpGet httpGet = new HttpGet(
				"http://www.cadal.zju.edu.cn/page/getBookPic/01023486/1");
		response = httpclient.execute(httpGet, localContext);
		HttpEntity entity = response.getEntity();

		entity = response.getEntity();
		System.out.println("Login form get: " + response.getStatusLine());
		System.out.println("Entity:" + EntityUtils.toString(entity));
		EntityUtils.consume(entity);

		System.out.println("--------------------------------");

		if (cookieStore.getCookies().size() == 0) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookieStore.getCookies().size(); i++) {
				System.out.println("- "
						+ cookieStore.getCookies().get(i).toString());
			}
		}

	}
}