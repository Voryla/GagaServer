package com.zwk.server;

import com.zwk.mysqlServer.MySqlManager;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 修改密码Servlet
 */
//@WebServlet("/")
public class UpdatePassword extends HttpServlet {
	private MySqlManager mySqlManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		if (MySqlManager.getMySqlManager() != null) {
			this.mySqlManager = MySqlManager.getMySqlManager();
		} else {
			this.mySqlManager = new MySqlManager("localhost", "root", "123456", "db_gaga");
		}
		super.init(config);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("user");
		String oldPwd = req.getParameter("oldPwd");
		String newPwd = req.getParameter("newPwd");
		resp.setHeader("content-type", "text/html;charset=UTF-8");
		if (!userName.isEmpty() && !newPwd.isEmpty() && !oldPwd.isEmpty()) {
			if (!mySqlManager.isConnection()) {
				mySqlManager.connectionMySql();
			}
			if (userName.length() > 9 || newPwd.length() > 9 || oldPwd.length() > 9) {
				resp.getWriter().write("<h1>昵称,账号,密码长度不可超过9位</h1>");
			}
			Statement statement = mySqlManager.getStatement();
			try {
				String sql = "SELECT * FROM tb_user WHERE userName='" + userName + "'AND userPassword='" + oldPwd + "'";
				ResultSet resultset = statement.executeQuery(sql);
				if (resultset.next()) {
					sql = "UPDATE tb_user SET userPassword=+'" + newPwd + "' WHERE userName='" + userName + "'";
					statement.execute(sql);
					resp.getWriter().write("<h1>修改成功！</h1>");
				} else {
					resp.getWriter().write("<h1>账号或用户名错误！</h1>");

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			resp.getWriter().write("<h1>请输入昵称，用户名，密码</h1>");
		}
	}
}
