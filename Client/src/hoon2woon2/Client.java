package hoon2woon2;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 2020-04-27
 * @author Seungun-Park
 * TCP Socket Client
 */

public class Client {

	// 임시로..옮겨놓음..! gowoon-choi
	private static String userid = "";

	private static final long serialVersionUID = -3752491464582754341L;
	
	static Socket socket;
	static OutputStream os;
	static InputStream is;
	static byte[] buf;
	static final String inipath = "server.properties";
	
	Properties prop = new Properties();
	
	public Client(){
		try {
			socket = new Socket();
			prop.load(new FileInputStream(inipath));
			
			connect();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean connect() {
		try {
			if(!(socket.isConnected())) {
				socket.connect(new InetSocketAddress(prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port"))));
				os = socket.getOutputStream();
				is = socket.getInputStream();
		
				send("Tetris-Client Connected");
				return true;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void close() {
		try {
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean login(String id, char[] pw) {
		try {
			if(!socket.isConnected()) return false;
			send("login");
			send(id);
			buf = new byte[256];
			is.read(buf);
			
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.reset();
			sh.update((new String(pw)).getBytes("UTF-8"));
			os.write(sh.digest());
			os.flush();
			
			buf = new byte[256];
			is.read(buf);
			System.out.println(new String(buf));
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean send(String message) {
		try {
			if(!socket.isConnected()) return false;
			buf = message.getBytes("UTF-8");
			os.write(buf);
			os.flush();
			return true;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getUserid(){
		return this.userid;
	}
}
