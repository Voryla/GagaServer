package com.zwk.mysqlServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlManager {
	// mysql 管理类
	private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static String url = null;
	private String user = null;
	private String pwd = null;
	private String databaseName = null;
	private volatile Statement statement = null;
	private boolean isConnection = false;
	private Connection connection = null;
	private static MySqlManager mySqlManager;
	public MySqlManager(String mySqlIpAddress, String user, String pwd, String databaseName) {
		this.user = user;
		this.pwd = pwd;
		this.databaseName = databaseName;
		url = "jdbc:mysql://" + mySqlIpAddress + ":3306/" + databaseName
				+ "?serverTimezone=GMT&characterEncoding=UTF-8&userSSL=false";
		mySqlManager=this;
	}

	public static MySqlManager getMySqlManager(){
		return mySqlManager;
	}

	/**
	 * 连接数据库
	 */
	public void connectionMySql() {
		try {
			Class.forName(JDBC_DRIVER);
			this.connection = DriverManager.getConnection(url, user, pwd);
			this.isConnection = !connection.isClosed();
			System.out.println("数据库连接成功");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC_DRIVER加载失败");
		} catch (SQLException e2) {
			System.out.println("mysql错误");
		}
	}

	/**
	 * 创建并返回数据库声明
	 *
	 * @return
	 */
	public Statement getStatement() {
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.statement;
	}

	/**
	 * 关闭数据库连接
	 *
	 * @return
	 */
	public boolean closeMySqlConnection() {
		try {
			this.statement.close();
			this.connection.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * 数据库的连接状态
	 *
	 * @return
	 */
	public boolean isConnection() {
		return isConnection;
	}

}
