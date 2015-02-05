package com.cadal.model;

import java.io.Serializable;

public class LogInfo implements Serializable {

	// -1:未验证
	// 验证
	// 1:验证可用（已用）
	// 2:验证不可用_未注册
	// 3:验证不可用_未激活
	public static int STAT_UNUSED = -1;
	public static int STAT_USED = 1;
	public static int STAT_USED_UNREG = 2;
	public static int STAT_USED_UNACT = 3;

}
