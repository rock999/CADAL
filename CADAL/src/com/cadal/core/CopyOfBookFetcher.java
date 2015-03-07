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

import com.cadal.common.DecryptTools;
import com.cadal.common.FileHelper;
import com.cadal.common.NetTools;
import com.cadal.common.LoginTools;
import com.cadal.model.OperationStatus;

public class CopyOfBookFetcher {
	static FileHelper fileHelper = new FileHelper();
	static List<String> bookIDList = fileHelper.ReadFileData("list.txt",
			"UTF-8");
	public String cookies = "";
	public static String BASEDIR = "d:/fuckData";

	public boolean cookieAvailable = false;

	public static void main(String[] args) throws Exception {

		String userName = "poiu832";
		String passWord = "8831075";
		CopyOfBookFetcher fetcher = new CopyOfBookFetcher();

		fetcher.cookies = LoginTools.activeAccountCookies("username="
				+ userName + "&password=" + passWord + "&remember=on");

		for (String bookID : bookIDList) {
			try {
				// exeService.execute(new Runnable());
				fetcher.fetchBookPages("", bookID);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public OperationStatus execute() {

		String userName = "poiu832";
		String passWord = "8831075";

		cookies = LoginTools.activeAccountCookies("username=" + userName
				+ "&password=" + passWord + "&remember=on");

		while (true) {

			int flagNum = 0;

			if (OperationStatus.OUTOFBOUND == flagNum) {

				// update database
				// download ok, pageindex

			} else {
				// update database
				// current pageindex
			}

			// check ip status
			if (OperationStatus.INVALIDIP == flagNum) {

				// sync switch ip
				NetTools.switchIP("http://192.168.1.1/userRpm/StatusRpm.htm?Disconnect=%B6%CF%20%CF%DF&wan=1");

				// sleep 20 sec current thread
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// check account cookies
			if (OperationStatus.INVALIDACCOUNT == flagNum) {
				userName = "qijiaxing123";
				passWord = "791016194";
				cookies = LoginTools.activeAccountCookies("username="
						+ userName + "&password=" + passWord + "&remember=on");
			}
		}
	}

	// build dir http://www.cadal.zju.edu.cn/book/getcatalog/01023486
	// invalid ddddddddddddddddddddddddddddddddddddddddddddd
	// out of bound false

	public OperationStatus fetchBookPages(String baseUrl, String BookID) {
		URI uri;
		CloseableHttpClient httpclient = ConnPool.getHttpClient();
		OperationStatus operaStatus = new OperationStatus();
		operaStatus.setStatus(OperationStatus.SUCESSED);

		int pageID = 0;// initial start page index
		String openKey;
		try {
			openKey = DecryptTools.decryptKey();
		} catch (Exception e) {
			operaStatus.setStatus(OperationStatus.INVALIDIP);
			operaStatus.result = pageID;
			return operaStatus;
		}

		// init openkey
		boolean outOfBound = false; // init out of bound status
		while (!outOfBound) {
			pageID++;
			operaStatus.result = pageID;

			HttpGet httpget = new HttpGet(
					"http://www.cadal.zju.edu.cn/page/getBookPic/" + BookID
							+ "/" + pageID);
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
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httpget);
			} catch (Exception e) {
				e.printStackTrace();
				operaStatus.setStatus(OperationStatus.COMMONFAILED);
				operaStatus.setMessage(e.getMessage());
				return operaStatus;
			}

			HttpEntity entity = response.getEntity();

			// check ip status 1
			if (response.getStatusLine().getStatusCode() == 403) {
				cookieAvailable = false;
				operaStatus.setStatus(OperationStatus.INVALIDIP);
				operaStatus.setMessage("invalid ip status!");
				return operaStatus;
			}

			if (entity != null) {

				File fileDiction = new File(BASEDIR + "/" + BookID);
				if (!fileDiction.exists())
					fileDiction.mkdirs();

				File file = new File(BASEDIR + "/" + BookID + "/" + pageID);
				OutputStream os = null;
				try {
					os = new FileOutputStream(file);
					entity.writeTo(os);

				} catch (Exception e) {
					e.printStackTrace();
					operaStatus.setStatus(OperationStatus.COMMONFAILED);
					operaStatus.setMessage(e.getMessage());

				}

				String pageContent = fileHelper.readDataFromFile(
						file.getAbsolutePath(), "UTF-8");

				// check ip status 2
				if (pageContent
						.indexOf("ddddddddddddddddddddddddddddddddddddddddddddd") >= 0) {
					cookieAvailable = false;
					operaStatus.setStatus(OperationStatus.INVALIDIP);
					operaStatus.setMessage("invalid ip status!");
					return operaStatus;
				}

				// check cookie status
				if (pageContent.indexOf("Grilled data shameful") >= 0) {
					operaStatus.setStatus(OperationStatus.INVALIDACCOUNT);
					operaStatus.setMessage("invalid account status!");
					cookieAvailable = false;
					return operaStatus;
				}

				// check whether out of page index
				if (pageContent.indexOf("false") >= 0) {
					outOfBound = true;
					operaStatus.setStatus(OperationStatus.OUTOFBOUND);
					return operaStatus;
				}

				// decrypt book page
				DecryptTools.decryptFile(BASEDIR + "/" + BookID + "/",
						String.valueOf(pageID), openKey);

				// Thread.sleep(1500);
			}
		}

		return operaStatus;

	}
}
