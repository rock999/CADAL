package com.cadal.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cadal.common.DecryptTools;
import com.cadal.common.FileHelper;
import com.cadal.common.LoginTools;

public class WholeBookDownloader {
	static FileHelper fileHelper = new FileHelper();
	static List<String> bookIDList = fileHelper.ReadFileData("list.txt",
			"UTF-8");
	static String cookies = "";

	public static void main(String[] args) throws Exception {

		String userName = "qijiaxing123";
		String passWord = "791016194";

		cookies = LoginTools.activeAccountCookies("username=" + userName
				+ "&password=" + passWord + "&remember=on");

		for (String bookID : bookIDList) {
			try {
				for (int i = 212; i < 999; i++) {
					getPage("", bookID, String.valueOf(i));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// build dir http://www.cadal.zju.edu.cn/book/getcatalog/01023486
	// invalid ddddddddddddddddddddddddddddddddddddddddddddd
	// out of bound false

	static void getPage(String baseUrl, String BookID, String pageID)
			throws Exception {
		URI uri;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpget = new HttpGet(
				"http://www.cadal.zju.edu.cn/page/getBookPic/" + BookID + "/"
						+ pageID);
		httpget.addHeader("Cookie", cookies);

		// httpget.addHeader(
		// "Cookie",
		// "Hm_lvt_49b2d9c4dcf350f42f3d125e3d293346=1422709694; Hm_lpvt_49b2d9c4dcf350f42f3d125e3d293346=1422718205;"
		// + cookies);

		httpget.addHeader("Referer", "http://www.cadal.zju.edu.cn/book/"
				+ BookID + "/" + pageID);
		httpget.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36");
		httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		// httpget.addHeader("", "");
		// httpget.addHeader("", "");
		// httpget.addHeader("", "");
		CloseableHttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			// InputStream instream = entity.getContent();
			// int byteOne = instream.read();
			// int byteTwo = instream.read();

			File fileDiction = new File("d:/fuckData/" + BookID);
			if (!fileDiction.exists())
				fileDiction.mkdirs();

			File file = new File("d:/fuckData/" + BookID + "/" + pageID);
			OutputStream os = null;
			os = new FileOutputStream(file);
			entity.writeTo(os);

			String pageContent = fileHelper.readDataFromFile(
					file.getAbsolutePath(), "UTF-8");

			// check cookie status
			if (pageContent.indexOf("Grilled data shameful") >= 0
					|| pageContent
							.indexOf("ddddddddddddddddddddddddddddddddddddddddddddd") >= 0)
				throw new Exception("InvalidAccount");

			// check page index bound
			if (pageContent.indexOf("false") >= 0) {
				throw new Exception("pageIndex 超出边界！！");
			}

			DecryptTools.decryptFile("d:/fuckData/" + BookID + "/", pageID,
					DecryptTools.decryptKey());
			// Thread.sleep(1500);
		}

		System.out.println(httpget.getURI());

	}
}
