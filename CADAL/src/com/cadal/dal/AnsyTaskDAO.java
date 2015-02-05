package com.cadal.dal;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.cadal.model.AnsyResultItem;
import com.cadal.model.AnsyStatusItem;

public class AnsyTaskDAO {

	static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=Mosaic";
	static String userName = "sa";
	static String userPwd = "perasa";
	static DBPool db = new DBPool(dbURL, userName, userPwd, driverName, 5, 100,
			30, 10000);

	Connection getConnection() {
		Connection conn = null;
		// 使用数据连接池产生链接
		conn = db.getConn();
		return conn;
	}

	public static void main(String[] args) {
		AnsyTaskDAO dbInstance = new AnsyTaskDAO();
		AnsyStatusItem item = new AnsyStatusItem();
		item.setServiceType("googleGAE");
		item.setIsAnsy(true);
		item.setConfigXML("GAEConfigXML");
		item.setUserID("999");
		item.setMaxNodeNum(9999999);
		item.setMaxLayerDepth(99);
		System.out.println(dbInstance.insertAnsyResultItem(item));
	}

	/***
	 * 提交任务
	 * 
	 * @param item
	 * @return id
	 */
	public long insertAnsyResultItem(AnsyStatusItem item) {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		Long id = null;
		try {
			// Run Runner
			int n = qRunner
					.update(conn,
							"INSERT INTO ansytaskstatus (serviceType,isAnsy,configXML,userID,maxNodeNum,maxLayerDepth) "
									+ "VALUES (?,?,?,?,?,?)",
							item.getServiceType(), item.getIsAnsy(),
							item.getConfigXML(), item.getUserID(),
							item.getMaxNodeNum(), item.getMaxLayerDepth());

			// get taskID
			id = ((BigInteger) qRunner.query(conn, "SELECT LAST_INSERT_ID()",
					new ScalarHandler(1))).longValue();

			// Close conn
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * 返回所有任务的信息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AnsyStatusItem> getStatusInfo() {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		List<AnsyStatusItem> ansyStatusItems = new ArrayList<AnsyStatusItem>();
		try {
			String sql = "select * from ansytaskstatus";
			ansyStatusItems = (List<AnsyStatusItem>) qRunner.query(conn, sql,
					new BeanListHandler(AnsyStatusItem.class));
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ansyStatusItems;
	}

	/**
	 * 根据taskId得到当前任务的所有信息
	 * 
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AnsyResultItem> getResultInfo(String taskId) {
		Connection conn = getConnection();
		// Create QueryRunner
		QueryRunner qRunner = new QueryRunner();
		List<AnsyResultItem> ansyResultItems = new ArrayList<AnsyResultItem>();
		try {
			String sql = "select * from ansytaskresult where taskID=?";
			ansyResultItems = (List<AnsyResultItem>) qRunner.query(conn, sql,
					new BeanListHandler(AnsyResultItem.class), taskId);
			DbUtils.closeQuietly(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ansyResultItems;
	}

}