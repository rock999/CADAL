package com.cadal.decode;
public class Test {

	/***
	 * http://www.cadal.zju.edu.cn/page/getkey
	 * get origial encypted key
	 * @param args
	 */
	public static void main(String[] args) {

		//decypt key
		String key = "faoqjffdfsdfasfasdf2";
		char loc2 = key.charAt((key.length() - 1));
		int loc3 = ((int) loc2) % 2;
		String loc4 = "";
		int loc5 = 0;
		while (loc5 < key.length()) {
			if (loc5 % 2 == loc3) {
				loc4 = loc4 + key.charAt(loc5);
			}
			++loc5;
		}
		String openkey = loc4;

		System.out.println(openkey);
	}

}
