package com.cadal.decode;
public interface IPRNG {
	void init(byte[] para);

	int next();

	int getPoolSize();

	String toString();

	void dispose();

}
