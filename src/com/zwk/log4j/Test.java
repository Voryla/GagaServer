package com.zwk.log4j;

import org.apache.log4j.Logger;

public class Test {

	public static void main(String[] args) {
		Log4jInit j=new Log4jInit();
		Logger logger=Logger.getLogger(Test.class);
		logger.info("ddddd");
	}
}
