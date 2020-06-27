package com.zwk.server;

import com.zwk.mysqlServer.MySqlManager;
import com.zwk.util.SocketManager;
import com.zwk.util.User;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import org.apache.log4j.Logger;

/**
 * 账号登陆检测
 *
 * @author ZWK
 */
public class GameLoginServer {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private static volatile Map<String, Socket> clients = new ConcurrentHashMap<>();
	private static Logger log4j = Logger.getLogger(GameLoginServer.class);
	public static MySqlManager mySqlManager = new MySqlManager("localhost", "root", "123456", "db_gaga");
	private static SocketManager socketManager;

	public GameLoginServer() throws IOException {
		if (!mySqlManager.isConnection()) {
			serverSocket = new ServerSocket(8019);
			log4j.info("登陆SocketServer创建");
			if (!mySqlManager.isConnection()) {
				mySqlManager.connectionMySql();
				log4j.info("登陆服务连接成功连接mysql");
			}
		}
		this.maintenanceClient();
	}

	public void acceptClient() {
		new Thread(() -> {
			try {
				while (true) {
					/**
					 * 用object读取
					 */
					clientSocket = serverSocket.accept();
					log4j.info("检测到客户端链接，正在检测账号。。。");

					if (checkLogin(clientSocket)) {
						SocketManager.send(clientSocket, "true");

					} else {
						SocketManager.send(clientSocket, "false");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private boolean checkLogin(Socket client) {
		boolean state = false;
		User user = null;
		user = SocketManager.readObject(client);
		if (clients.containsKey(user.getUserName())) {
			log4j.error("账号已登陆！");
			return false;
		}
		String sql = "SELECT * FROM tb_user WHERE userName=\"" + user.getUserName() + "\" AND userPassword=\""
				+ user.getPassword() + "\"";
		try {
			Statement statement = mySqlManager.getStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				state = true;
				user.setName(resultSet.getString("name"));
				log4j.info("用户尝试使用 userName=" + user.getUserName() + "登陆时，登录成功！");
				clients.put(user.getUserName(), client);
			} else {
				state = false;
				log4j.error("用户尝试使用 userName=" + user.getUserName() + "登陆时，登录失败！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	// 维护客户端map
	private void maintenanceClient() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					if (!clients.isEmpty()) {
						clients.forEach(new BiConsumer<String, Socket>() {
							@Override
							public void accept(String s, Socket socket) {
								if (SocketManager.isServerClose(socket)) {
									clients.remove(s, socket);
									log4j.info("检测到有客户端断开连接，已移除该客户端");
								}
							}
						});
					}
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// 用于其他类获取连接中的客户端
	public static Map<String, Socket> getClients() {
		return clients;
	}
}