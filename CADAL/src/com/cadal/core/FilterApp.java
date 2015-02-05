package com.cadal.core;

import java.io.File;

import com.cadal.dal.BookDAO;

public class FilterApp {

	static BookDAO dao = new BookDAO();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File root = new File("Z:/ZJCrawlerResult/ZJCrawlerResult/Books/�ż�");
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.println(file.getName());
				long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��

				int size = file.listFiles().length;

				long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��

				System.out.println("size ��������ʱ�䣺" + (endTime - startTime)
						+ "ms"); // �����������ʱ��
				System.out.println(size);

				startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��

				dao.updateBookStatus(file.getName(), 1, size);
				endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
				System.out.println(" insert ��������ʱ�䣺" + (endTime - startTime)
						+ "ms"); // �����������ʱ��

			}
		}
	}

}
