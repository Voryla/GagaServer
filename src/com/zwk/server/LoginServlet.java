package com.zwk.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 登陆服务器默认加载
 */
public class LoginServlet extends HttpServlet{
	Logger log4j=Logger.getLogger(this.getClass());
	@Override
	public void init() throws ServletException {
		try {
			GameLoginServer loginServer=new GameLoginServer();
			loginServer.acceptClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.init();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
