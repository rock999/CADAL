package com.cadal.decode;

public interface ICipher {
	
	byte[] encrypt(byte[] args);
	int getBlockSize();
	String toString();
	byte[] decrypt(byte[] args);
	void dispose();
}
