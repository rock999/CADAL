package com.cadal.common;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

public class FileHelper {

	@SuppressWarnings("unused")
	public void writeDataToFile(String fileDic, String fileName,
			String content, String charSet) {
		File file = null;
		try {
			File fileDiction = new File(fileDic);
			if (!fileDiction.exists())
				fileDiction.mkdirs();
			file = new File(fileDiction.getAbsoluteFile() + "\\" + fileName);
			if (!file.exists())
				file.createNewFile();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		OutputStreamWriter write = null;
		try {
			write = new OutputStreamWriter(new FileOutputStream(file), charSet);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	@SuppressWarnings("unused")
	public void appendDataToFile(String fileDic, String fileName,
			String content, String charSet, boolean changedRow) {
		File file = null;
		try {
			File fileDiction = new File(fileDic);
			if (!fileDiction.exists())
				fileDiction.mkdirs();
			file = new File(fileDiction.getAbsolutePath() + "\\" + fileName);
			if (!file.exists())
				file.createNewFile();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		OutputStreamWriter write = null;
		try {
			write = new OutputStreamWriter(new FileOutputStream(file, true),
					charSet);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			if (changedRow)
				writer.newLine();

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readDataFromFile(String fileName, String code) {
		StringBuilder sb = new StringBuilder();
		File file = new File(fileName);
		Reader reader = null;
		try {

			reader = new InputStreamReader(new FileInputStream(file), code);
			int tempchar;
			while ((tempchar = reader.read()) != -1) {

				if (((char) tempchar) != '\r') {
					sb.append((char) tempchar);
				}
			}
			reader.close();

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public ArrayList<String> ReadFileData(String fileName,
			ArrayList<String> strList) {

		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 0;

			while ((tempString = reader.readLine()) != null) {
				line++;
				System.out.println("line " + line + ": " + tempString);
				if (!tempString.equalsIgnoreCase(""))
					strList.add(tempString);
			}
			reader.close();
			return strList;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

	public ArrayList<String> ReadFileData(String fileName, String code) {
		ArrayList<String> strList = new ArrayList<String>();

		BufferedReader reader = null;
		try {
			FileInputStream fInputStream = new FileInputStream(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fInputStream, code);
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			int line = 0;

			while ((tempString = reader.readLine()) != null) {
				line++;
				System.out.println("line " + line + ": " + tempString);
				if (!tempString.equalsIgnoreCase(""))
					strList.add(tempString);
			}
			reader.close();
			return strList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strList;
	}

	public static void main(String[] args) {
	}

}
