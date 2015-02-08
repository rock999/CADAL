package com.cadal.core;

public class Test {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis(); // 获取开始时间
		try {
			Thread.currentThread().sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis(); // 获取结束时间

		System.out.println("size 程序运行时间：" + (endTime - startTime));
	}

}
