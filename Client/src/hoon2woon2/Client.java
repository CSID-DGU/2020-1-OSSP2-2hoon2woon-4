package hoon2woon2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * 2020-04-27
 * @author Seungun-Park
 * TCP Socket Client
 */

public class Client {

	private static final long serialVersionUID = -3752491464582754341L;
	
	static Socket socket;
	static OutputStream os;
	static InputStream is;
	static byte[] buf;
	static final String inipath = "server.properties";

	private static int user = -1;
	private static String userid = "";
	private int gamecount;
	private int mode;
	
	public static Vector<String>userList = new Vector<String>();
	public static String[] ranking;
	
	Properties prop = new Properties();
	
	public Client(){
		ranking = new String[10];
		for(int i = 0; i < 10; i++)
			ranking[i] = "";
		//try {
			socket = new Socket();
		//	prop.load(new FileInputStream(inipath));
			connect();
		//} catch(IOException e) {
		//	e.printStackTrace();
		//}

	}
	
	
	public boolean connect() {
		try {
			if(!(socket.isConnected())) {
				socket.connect(new InetSocketAddress("54.180.192.185", 20204));
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
			receive();
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
			if(new String(buf).substring(0,13).equals("login success"))
			{
				user = 1;
				userid = id;
				JOptionPane.showMessageDialog(null, "login success");
				rank();
				return true;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "login failed");
				return false;
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean register(String id, char [] pw) {   //cha seung hoon_for Register Frame
		 try {
	         if(!socket.isConnected()) return false;
	         send("register");
	         receive();
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
	         
	         if(new String(buf).substring(0,16).equals("register success"))
	         {
	            return true;
	         }
	         else
	         {
	            return false;
	         }
	      }catch(Exception e) {
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
	
	public String receive() {
		try {
			if(!socket.isConnected()) return "";
			buf = new byte[1024];
			is.read(buf);
			return (new String(buf));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean isLogined() {
		if(user == -1) return false;
		else return true;
	}
	
	public boolean logout() {
		user = -1;
		userid = "";
		send("logout");
		receive();
		return true;
  }

	public String getUserid(){
		return this.userid;
	}
	
	public String[] rank() {
		send("rank");
		String receiverank = receive();
		ranking = receiverank.split("/");
		return ranking;
	}
	public String[] rankupdate(int score) {
		if(isLogined())
		{
			send("rankupdate");
			send(Integer.toString(score));
			receive();
		}
		return rank();
	}
	
	public int getGamerCount() {
		return this.gamecount;
	}
	
	public void setGamerCount(int gamecount) {
		this.gamecount = gamecount;
	}

	
	public void setUserList(Vector <String> userList) {
		for(int i = 0 ; userList.size()>i;i++)
			this.userList.add(userList.elementAt(i));
	}
}
