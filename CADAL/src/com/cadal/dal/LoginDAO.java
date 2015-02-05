package com.cadal.dal;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import com.cadal.common.FileHelper;

public class LoginDAO {

	static String driverName = "com.mysql.jdbc.Driver";
	static String dbURL = "jdbc:mysql://localhost:3306/cadaldb";
	static String userName = "root";
	static String userPwd = "sa";
	static DBPool db = new DBPool(dbURL, userName, userPwd, driverName, 3, 50,
			30, 10000);

	Connection getConnection() {
		// 使用数据连接池产生链接
		return db.getConn();
	}

	public static void main(String[] args) {
		LoginDAO dbInstance = new LoginDAO();

		// for (int i = 0; i < 100; i++) {
		// dbInstance.insertAccount("user" + i, "pass" + i);
		// }

//		FileHelper helper = new FileHelper();
//		List<String> bookList = helper.ReadFileData("reg.txt", "UTF-8");
//		for (String account : bookList) {
//			// dbInstance.updateAccountStatus("user1", 4);
//			String[] infos = account.split("###");
//			dbInstance.insertAccount(infos[0], infos[1]);
//			System.out.println(account);
//		}

		// dbInstance.updateAccountStatus("user1", 4);
	}

	/***
	 * 增加新账号
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public void insertAccount(String userName, String passWord) {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		Long id = null;
		try {
			// Run Runner
			int n = qRunner.update(conn,
					"INSERT INTO account (username,password)  "
							+ "VALUES (?,?)", userName, passWord);

			// // get taskID
			// id = ((BigInteger) qRunner.query(conn, "SELECT LAST_INSERT_ID()",
			// new ScalarHandler(1))).longValue();

			// Close conn
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 变更账号状态
	 * 
	 * @param userName
	 * @param status
	 * @return
	 */
	public void updateAccountStatus(String userName, int status) {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		try {
			// Run Runner
			int n = qRunner.update(conn,
					"UPDATE account SET status=? WHERE username=?", status,
					userName);

			// Close conn
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}