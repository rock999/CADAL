package com.cadal.model;

import java.io.Serializable;

public class LogInfo implements Serializable {

	// -1:δ��֤
	// ��֤
	// 1:��֤���ã����ã�
	// 2:��֤������_δע��
	// 3:��֤������_δ����
	public static int STAT_UNUSED = -1;
	public static int STAT_USED = 1;
	public static int STAT_USED_UNREG = 2;
	public static int STAT_USED_UNACT = 3;

}
