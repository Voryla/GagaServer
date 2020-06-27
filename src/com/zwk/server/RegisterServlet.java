package com.zwk.server;

import com.mysql.cj.protocol.Resultset;
import com.zwk.mysqlServer.MySqlManager;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注册Servlet
 */
public class RegisterServlet extends HttpServlet {
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
		String name = req.getParameter("name");
		String userName = req.getParameter("user");
		String pwd = req.getParameter("pwd");
		resp.setHeader("content-type", "text/html;charset=UTF-8");
		if (!userName.isEmpty() && !pwd.isEmpty()&&!name.isEmpty()) {
			if (!mySqlManager.isConnection()) {
				mySqlManager.connectionMySql();
			}
			System.out.println(name + userName + pwd);
			if (userName.length() > 9 || pwd.length() > 9 || name.length() > 9) {
				resp.getWriter().write("<h1>昵称,账号,密码长度不可超过9位</h1>");
				return;
			}
			Statement statement = mySqlManager.getStatement();
			try {
				String sql = "SELECT * FROM tb_user WHERE userName=\"" + userName + "\"";
				String sql2 = "SELECT * FROM tb_user WHERE name=\"" + name + "\"";
				ResultSet resultset = statement.executeQuery(sql2);
				if (resultset.next()) {
					resp.getWriter().write("<h1>该昵称已存在，注册失败！</h1>");

				} else {
					resultset = statement.executeQuery(sql);
					if (resultset.next()) {
						resp.getWriter().write("<h1>该用户名已存在，注册失败！</h1>");
					} else {
						resp.getWriter().write("<h1>恭喜你，注册成功！</h1>");
						String insertString = "INSERT INTO tb_user (name,userName,userPassword) VALUES ('" + name + "','" + userName + "','" + pwd + "')";
						statement.execute(insertString);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			resp.getWriter().write("<h1>请输入昵称，用户名，密码</h1>");
		}
	}
}
