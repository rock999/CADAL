package com.cadal.dal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cadal.model.ServiceItem;

public class ServiceItemDAO {

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

	public ServiceItemDAO() {
	}


	/***
	 * 获取服务项列表
	 */
	public List<ServiceItem> getAllServiceItem() {
		Connection conn = null;
		List<ServiceItem> list = new ArrayList<ServiceItem>();
		try {
			conn = getConnection();
			String sqlString = "SELECT  [serviceID],[serviceType] ,[subType] ,[features],[configInfo] ,[status]  ,[level] ,[timeStamp] "
					+ "FROM [Mosaic].[dbo].[ServiceItem] WHERE status=1";
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sqlString);
			while (rs.next()) {
				ServiceItem item = new ServiceItem();
				item.setServiceID(rs.getInt("serviceID"));
				item.setServiceType(rs.getString("serviceType"));

				item.setStatus(rs.getInt("status"));

				list.add(item);
			}
			stat.close();
			conn.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/***
	 * 获取服务项分组
	 */
	public List<String> getAllServiceGroup() {
		Connection conn = null;
		List<String> list = new ArrayList<String>();
		try {
			conn = getConnection();
			String sqlString = "SELECT [serviceType] FROM [Mosaic].[dbo].[ServiceItem] group by ServiceType";
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sqlString);
			while (rs.next()) {
				list.add(rs.getString("serviceType"));
			}
			stat.close();
			conn.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/***
	 * 添加服务项
	 * 
	 * @param item
	 */
	public void addServiceItem(ServiceItem item) {

	}

	/***
	 * 更新服务项
	 * 
	 * @param item
	 */
	public void updateServiceItem(ServiceItem item) {

	}

	/***
	 * 删除服务项
	 * 
	 * @param serviceID
	 */
	public void deleteServiceItem(String serviceID) {

	}

	public static void main(String[] args) {

		ServiceItemDAO db = new ServiceItemDAO();
		List<ServiceItem> list = db.getAllServiceItem();
		System.out.println(list.size());
	}
}
