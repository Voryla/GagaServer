package com.zwk.log4j;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String log4jInitFilePath = Log4jInit.class.getResource("/com/zwk/log4j/log4j.properties").toString()
				.replace("%20", " ").replace("file:/", "");
		// 获取初始化参数得到配置文件在项目中的路径
		if (log4jInitFilePath == null) {
			System.err.println("*** 没有 log4j-properties-location 初始化的文件, 所以使用 BasicConfigurator初始化");
			BasicConfigurator.configure();
		} else {
			System.out.println("log4j正在加载。。。");
			File log4jFile = new File(log4jInitFilePath);
			if (log4jFile.exists()) {
				System.out.println("使用: " + log4jInitFilePath + "初始化日志设置信息");
				PropertyConfigurator.configure(log4jInitFilePath);
				System.out.println("log4j加载成功。。。");
			} else {
				System.err.println("*** " + log4jInitFilePath + " 文件没有找到， 所以使用 BasicConfigurator初始化");
				BasicConfigurator.configure();
			}
		}

	}
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	super.doGet(req, resp);
}
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	super.doPost(req, resp);
}


}
