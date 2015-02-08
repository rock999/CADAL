package com.cadal.common;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.cadal.dal.LoginDAO;
import com.cadal.model.LogInfo;

public class LoginTools {

	static LoginDAO loginDB = new LoginDAO();
	static FileHelper fhelper = new FileHelper();

	public static void main(String[] args) throws Exception {

		String userName = "qijiaxing123";
		String passWord = "791016194";

		activeAccountCookies("username=" + userName + "&password=" + passWord
				+ "&remember=on");

	}

	/***
	 * get valid account ticket
	 * 
	 * @return
	 */

	public static String activeAccountCookies(String postData) {
		LogInfo account = loginDB.getAccount();
		postData = "username=" + account.getUserName() + "&password="
				+ account.getPassWord() + "&remember=on";

		String cookie = "";
		try {
			HttpClient httpclient = HttpClientBuilder.create().build();

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
			// 构造最简单的字符串数据
			StringEntity reqEntity = new StringEntity(postData);
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			// 设置请求的数据
			httppost.setEntity(reqEntity);
			// 执行
			HttpResponse response = httpclient.execute(httppost);
			for (Header header : response.getAllHeaders()) {
				if (header.getName().toUpperCase().equals("SET-COOKIE")) {
					cookie = cookie + "; " + header.getValue().split(";")[0];
					System.out.println(cookie);
				}
			}
		} catch (Exception e) {
			// login error
			cookie = null;
		}
		return cookie;

	}

	/***
	 * get valid account ticket, auto fetch account info from db
	 * 
	 * @return
	 */

	public static synchronized String activeAccountCookies() {
		LogInfo account = loginDB.getAccount();
		String postData = "username=" + account.getUserName() + "&password="
				+ account.getPassWord() + "&remember=on";

		boolean accountStatus = false;

		String cookie = null;
		try {
			HttpClient httpclient = HttpClientBuilder.create().build();

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
			// 构造最简单的字符串数据
			StringEntity reqEntity = new StringEntity(postData);
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			// 设置请求的数据
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			// while pass check, new location status 302
			if (response.getStatusLine().getStatusCode() == 302)
				accountStatus = true;

			for (Header header : response.getAllHeaders()) {
				if (header.getName().toUpperCase().equals("SET-COOKIE")) {
					cookie = cookie + "; " + header.getValue().split(";")[0];
					System.out.println(cookie);
				}
			}

			// print switch opera result
			System.out.println("swith account opera#"
					+ TimeUtility.getCurrentTimeStr() + "#" + accountStatus
					+ "#" + account.getUserName() + "###"
					+ account.getPassWord());

			// record account history into file
			fhelper.appendDataToFile(
					"log",
					"switchAccount.log",
					TimeUtility.getCurrentTimeStr() + "#" + accountStatus + "#"
							+ account.getUserName() + "###"
							+ account.getPassWord(), "UTF-8", true);

			// record account history into db
			loginDB.updateAccountStatus(account.getUserName(),
					accountStatus ? LogInfo.STAT_USED : LogInfo.STAT_USED_UNACT);

		} catch (Exception e) {
			// login error
			cookie = null;
		}
		return cookie;

	}
}
