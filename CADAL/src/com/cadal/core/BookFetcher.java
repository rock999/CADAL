package com.cadal.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.cadal.common.DecryptTools;
import com.cadal.common.FileHelper;
import com.cadal.common.IPTools;
import com.cadal.common.LoginTools;
import com.cadal.common.TaskTools;
import com.cadal.common.TimeUtility;
import com.cadal.dal.BookDAO;
import com.cadal.model.BookInfo;
import com.cadal.model.OperationStatus;

public class BookFetcher {
	static FileHelper fileHelper = new FileHelper();
	static BookDAO bookDB = new BookDAO();

//	static List<String> bookIDList = fileHelper.ReadFileData("list.txt",
//			"UTF-8");
	public String cookies = "";
	public static String BASEDIR = "H:/fuckData";

	public boolean cookieAvailable = false;

	public static void main(String[] args) throws Exception {

		BookFetcher fetcher = new BookFetcher();
		fetcher.execute();

		// BookInfo book = bookDB.getBookTask();
		// String userName = "00000000000110";
		// String passWord = "13159569327";
		// BookFetcher fetcher = new BookFetcher();
		//
		// fetcher.cookies = LoginTools.activeAccountCookies("username="
		// + userName + "&password=" + passWord + "&remember=on");
		//
		// // for (String bookID : bookIDList) {
		// try {
		// // exeService.execute(new Runnable());
		// fetcher.fetchBookPages("", book.getBookID(), 0);
		//
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }
	}

	public OperationStatus execute() {
		this.cookies = LoginTools.activeAccountCookies();
		while (true) {
			BookInfo book = TaskTools.getTask();
			if (book == null) {
				System.out.println("未接到合法任务，退出！");
				break;
			}

			OperationStatus result = fetchBookPages(book.getCatalog(),
					book.getBookID(), book.getBookIndex());

			int flagNum = result.getStatus();

			if (OperationStatus.OUTOFBOUND == flagNum) {
				// update database
				// download ok, pageindex
				bookDB.updateBookStatus(book.getBookID(), 1,
						Integer.valueOf(result.result.toString()));

			} else {
				// update database
				// current pageindex
				bookDB.updateBookStatus(book.getBookID(), 0,
						Integer.valueOf(result.result.toString()));
			}

			// record download info
			fileHelper.appendDataToFile("log", "download.log",
					TimeUtility.getCurrentTimeStr() + "#" + book.getBookID()
							+ "#size:" + result.result.toString() + "#"
							+ flagNum + "#" + result.getMessage(), "UTF-8",
					true);

			// record download info

			// check ip status
			if (OperationStatus.INVALIDIP == flagNum) {

				// sync switch ip,break out of loop if failed
				if (!IPTools
						.switchIP("http://192.168.1.1/userRpm/StatusRpm.htm?Disconnect=%B6%CF%20%CF%DF&wan=1"))
					break;

				// sleep 30 sec current thread
				try {
					Thread.sleep(15000);
					//变更ip后变更用户登陆 
					cookies = LoginTools.activeAccountCookies();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// check account cookies
			if (OperationStatus.INVALIDACCOUNT == flagNum) {
				cookies = LoginTools.activeAccountCookies();
			}
		}

		return null;
	}

	// build dir http://www.cadal.zju.edu.cn/book/getcatalog/01023486
	// invalid ddddddddddddddddddddddddddddddddddddddddddddd
	// out of bound false

	OperationStatus fetchBookPages(String catelog, String bookID, int pageSize) {
		URI uri;
		CloseableHttpClient httpclient = ConnPool.getHttpClient();
		OperationStatus operaStatus = new OperationStatus();
		operaStatus.setStatus(OperationStatus.COMMONFAILED);

		int pageID = pageSize == 0 ? 0 : pageSize - 1;// initial start page
		// index
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

			// http://www.cadal.zju.edu.cn/page/getBookPic/01023486/1
			// http://www.cadal.zju.edu.cn/page/getBookPic/﻿01023486/1
			// http://www.cadal.zju.edu.cn/page/getBookPic/01023486/1
			// http://www.cadal.zju.edu.cn/page/getBookPic/%EF%BB%BF01023486/1
			// HttpGet("http://www.cadal.zju.edu.cn/page/getBookPic/01023486/1");
			String target = null;
			try {
				target = "http://www.cadal.zju.edu.cn/page/getBookPic/"
						+ java.net.URLEncoder.encode(bookID, "utf-8").replace(
								"%EF%BB%BF", "") + "/" + pageID;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpGet httpget = new HttpGet(target);

			httpget.addHeader("Cookie", cookies);

			// httpget.addHeader(
			// "Cookie",
			// "Hm_lvt_49b2d9c4dcf350f42f3d125e3d293346=1422709694; Hm_lpvt_49b2d9c4dcf350f42f3d125e3d293346=1422718205;"
			// + cookies);

			// httpget.addHeader("Referer", "http://www.cadal.zju.edu.cn/book/"
			// + BookID + "/" + pageID);

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

				File fileDiction = new File(BASEDIR + "/" + catelog + "/"
						+ bookID);
				if (!fileDiction.exists())
					fileDiction.mkdirs();

				File file = new File(BASEDIR + "/" + catelog + "/" + bookID
						+ "/" + pageID);
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

				// check page status
				if (response.getStatusLine().getStatusCode() != 200) {
					operaStatus.setStatus(OperationStatus.COMMONFAILED);
					operaStatus.setMessage(response.getStatusLine().toString());
					return operaStatus;
				}

				// check "false"prefix error page
				if (pageContent.indexOf("false") >= 0
						&& !pageContent.trim().equals("false")) {
					operaStatus.setStatus(OperationStatus.COMMONFAILED);
					return operaStatus;
				}

				// check whether out of page index
				if (pageContent.trim().equals("false")) {
					outOfBound = true;
					operaStatus.setStatus(OperationStatus.OUTOFBOUND);
					return operaStatus;
				}

				// print status
				System.out.println(TimeUtility.getCurrentTimeStr() + "#"
						+ bookID + "#" + pageID + "  ok!");

				// decrypt book page
				DecryptTools.decryptFile(BASEDIR + "/" + catelog + "/" + bookID
						+ "/", String.valueOf(pageID), openKey);

				// Thread.sleep(1500);
			}
		}

		return operaStatus;

	}
}
