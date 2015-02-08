package com.cadal.decode;
public class ARC4 implements IPRNG, IStreamCipher {

	private static int psize = 256;
	private byte[] S = new byte[256];;
	private int i = 0;
	private int j = 0;

	public ARC4(byte[] arg1) {

		if (arg1 != null) {
			init(arg1);
		}
	}

	public byte[] decrypt(byte[] arg1) {
		return encrypt(arg1);
	}

	public void init(byte[] arg1) {
		int loc1 = 0;
		int loc2 = 0;
		int loc3 = 0;
		loc1 = 0;
		while (loc1 < 256) {
			S[loc1] = (byte) loc1;
			++loc1;
		}
		loc2 = 0;
		loc1 = 0;
		while (loc1 < 256) {
			loc2 = loc2 + S[loc1] + arg1[loc1 % arg1.length] & 255;
			loc3 = S[loc1];
			S[loc1] = S[loc2];
			S[loc2] = (byte) loc3;
			++loc1;
		}
		this.i = 0;
		this.j = 0;
	}

	public int next() {
		byte loc1 = 0;
		i = i + 1 & 255;
		j = j + S[i] & 255;
		loc1 = S[i];
		S[i] = S[j];
		S[j] = loc1;
		return S[loc1 + S[i] & 255];
	}

	public byte[] encrypt(byte[] arg1) {
		int loc1 = 0;
		while (loc1 < arg1.length) {
			int loc2 = loc1++;
			arg1[loc2] =(byte) (arg1[loc2] ^ next());
		}
		return arg1;
	}

	public void dispose() {
		this.S = null;
		this.i = 0;
		this.j = 0;

	}

	public int getBlockSize() {
		return 1;
	}

	public int getPoolSize() {
		return psize;
	}

	public String toString() {
		return "rc4";
	}

}
