package com.cadal.dal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

import com.cadal.model.BookInfo;

public class BookDAO {

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
		BookDAO dbInstance = new BookDAO();

		List<BookInfo> bookList = dbInstance.getBookTaskList();
		System.out.println(bookList.get(0).getBookID() + "#"
				+ bookList.get(0).getBookIndex());

		// for (int i = 0; i < 100; i++) {
		// dbInstance.insertAccount("user" + i, "pass" + i);
		// }
		// FileHelper helper = new FileHelper();
		// List<String> bookList = helper.ReadFileData("cadal1.txt", "UTF-8");
		// for (String bookInfo : bookList) {
		// // dbInstance.updateAccountStatus("user1", 4);
		// String[] infos = bookInfo.split("###");
		// dbInstance.insertBook(infos[0], infos[1], infos[2]);
		// System.out.println(bookInfo);
		// }
	}

	/***
	 * 增加新账号
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public void insertBook(String bookID, String topType, String secondType) {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		Long id = null;
		try {
			// Run Runner
			int n = qRunner.update(conn,
					"INSERT INTO bookInfo (bookID,topType,secondType)  "
							+ "VALUES (?,?,?)", bookID, topType, secondType);

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
	 * 变更图书状态
	 * 
	 * @param userName
	 * @param status
	 * @return
	 */
	public void updateBookStatus(String bookID, int fileStatus, int pageSize) {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		try {
			// Run Runner
			int n = qRunner
					.update(conn,
							"UPDATE bookInfo SET fileStatus=?,pageSize=? WHERE bookID=?",
							fileStatus, pageSize, bookID);

			// Close conn
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * sync get book task
	 * 
	 * @return
	 */
	public synchronized BookInfo getBookTask() {

		BookInfo bookInfo = new BookInfo();
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		try {
			// 返回单行记录，使用Map
			// System.out.println("使用Map处理单行记录！");
			Map<String, Object> map = queryRunner
					.query(conn,
							"select bookID,pageSize,topType as catalog from bookinfo where fileStatus=0 and topType='古籍'  limit 1",
							new MapHandler(), (Object[]) null);

			for (Iterator<Entry<String, Object>> i = map.entrySet().iterator(); i
					.hasNext();) {
				Entry<String, Object> e = i.next();
				System.out.println(e.getKey() + "=" + e.getValue());

				if (e.getKey().toUpperCase().equals("BOOKID"))
					bookInfo.setBookID(e.getValue().toString());
				if (e.getKey().toUpperCase().equals("PAGESIZE"))
					bookInfo.setBookIndex(Integer.valueOf(e.getValue()
							.toString()));
				if (e.getKey().toUpperCase().equals("CATALOG"))
					bookInfo.setCatalog(e.getValue().toString());
			}

			// Close conn
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();

			bookInfo = null;
		}

		return bookInfo;

	}

	public synchronized List<BookInfo> getBookTaskList() {

		List<BookInfo> list = new ArrayList<BookInfo>();
		Connection conn = getConnection();

		// return runner.query("select * from account where name=?",
		// , "a");

		// Create QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		try {
			// 返回单行记录，使用Map
			// System.out.println("使用Map处理单行记录！");
			list = queryRunner
					.query(conn,
							"select bookID,pageSize,topType as catalog from bookinfo where fileStatus=0 and topType='古籍'  limit 5000",
							new ResultSetHandler<List<BookInfo>>() {
								public List<BookInfo> handle(ResultSet rs)
										throws SQLException {
									List<BookInfo> list = new ArrayList<BookInfo>();
									while (rs.next()) {
										BookInfo bookInfo = new BookInfo();

										bookInfo.setBookID(rs
												.getString("bookID"));
										bookInfo.setBookIndex(rs
												.getInt("pageSize"));
										bookInfo.setCatalog(rs
												.getString("catalog"));

										list.add(bookInfo);
									}

									return list;
								}
							}, (Object[]) null);

			// for (Iterator<Entry<String, Object>> i =
			// map.entrySet().iterator(); i
			// .hasNext();) {
			// BookInfo bookInfo = new BookInfo();
			// Entry<String, Object> e = i.next();
			// System.out.println(e.getKey() + "=" + e.getValue());
			//
			// if (e.getKey().toUpperCase().equals("BOOKID"))
			// bookInfo.setBookID(e.getValue().toString());
			// if (e.getKey().toUpperCase().equals("PAGESIZE"))
			// bookInfo.setBookIndex(Integer.valueOf(e.getValue()
			// .toString()));
			// if (e.getKey().toUpperCase().equals("CATALOG"))
			// bookInfo.setCatalog(e.getValue().toString());
			//
			// list.add(bookInfo);
			// }

			// Close conn
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();

			list = null;
		}

		return list;

	}

}