package com.cadal.core;

import java.io.File;

import com.cadal.dal.BookDAO;

public class FilterApp {

	static BookDAO dao = new BookDAO();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File root = new File("Z:/ZJCrawlerResult/ZJCrawlerResult/Books/古籍");
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.println(file.getName());
				long startTime = System.currentTimeMillis(); // 获取开始时间

				int size = file.listFiles().length;

				long endTime = System.currentTimeMillis(); // 获取结束时间

				System.out.println("size 程序运行时间：" + (endTime - startTime)
						+ "ms"); // 输出程序运行时间
				System.out.println(size);

				startTime = System.currentTimeMillis(); // 获取开始时间

				dao.updateBookStatus(file.getName(), 1, size);
				endTime = System.currentTimeMillis(); // 获取结束时间
				System.out.println(" insert 程序运行时间：" + (endTime - startTime)
						+ "ms"); // 输出程序运行时间

			}
		}
	}

}
