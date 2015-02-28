package com.cadal.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.cadal.dal.BookDAO;
import com.cadal.model.BookInfo;

public class TaskTools {
	static BookDAO bookDB = new BookDAO();
	static Queue<BookInfo> bookQueue = new LinkedList<BookInfo>();
	static boolean firstFlag = true;

	public static synchronized BookInfo getTask() {

		if (bookQueue.size() == 0) {
			// 判断是否为初次执行，
			if (firstFlag)
				firstFlag = false;
			else
				try {
					// 非初次执行 需要暂停600秒，等待未完成下载任务的线程
					Thread.currentThread().sleep(600000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			List<BookInfo> bookList = bookDB.getBookTaskList();
			if (bookList == null || bookList.size() == 0)
				return null;
			bookQueue.addAll(bookList);
		}
		return bookQueue.poll();

	}

	public static void main(String[] args) {

	}

}
