package com.cadal.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.cadal.decode.ARC4;

public class DecryptTools {

	public static void main(String[] args) {
		try {
			String openKey = decryptKey();
			decryptFile("d:", "temp.data", openKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String decryptKey() throws Exception {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://www.cadal.zju.edu.cn/page/getkey");
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();

		entity = response.getEntity();

		int Status = response.getStatusLine().getStatusCode();

		if (Status == 403)
			throw new Exception("InvalidIP");

		// get openKey
		String encryptKey = EntityUtils.toString(entity);
		System.out.println("encryptKey:" + encryptKey);

		// decrypt openKey
		String key = encryptKey;
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
		return openkey;
	}

	public static void decryptFile(String fileDir, String filename,
			String openKey) {
		boolean b = false;
		try {

			File file2 = new File(fileDir + filename);
			FileInputStream fis = new FileInputStream(file2);
			byte[] buf = new byte[(int) file2.length()];
			fis.read(buf);
			fis.close();

			OutputStream bos = new FileOutputStream(new File(fileDir + filename
					+ ".jpg"));
			ARC4 decoder = new ARC4(openKey.getBytes());
			bos.write(decoder.decrypt(buf), 0, buf.length);

			bos.flush();
			bos.close();

			b = true;
		} catch (Exception e) {
			b = false;
		}
		int[] a = new int[2];
		if (b) {
			java.io.File file = new java.io.File(fileDir + filename + ".jpg");
			BufferedImage bi = null;
			boolean imgwrong = false;
			try {

				bi = javax.imageio.ImageIO.read(file);
				try {
					int i = bi.getType();
					imgwrong = true;
				} catch (Exception e) {
					imgwrong = false;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			if (imgwrong) {
				a[0] = bi.getWidth();
				a[1] = bi.getHeight();
			} else {
				a = null;
			}

		} else {
			a = null;
		}

	}

}
