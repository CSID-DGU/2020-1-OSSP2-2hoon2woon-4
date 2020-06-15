package hoon2woon2;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

import javax.swing.JOptionPane;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	
	Properties prop = new Properties();
	
	public Client(){
		socket = new Socket();
		connect();
//		try {
//			System.out.println(System.getProperty("user.dir"));
//			socket = new Socket();
//
//			prop.load(new FileInputStream(inipath));
//
//			connect();
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
	}
	
	   public boolean regist(String id, char[] pw) {
		      try {
		         if(!socket.isConnected()) return false;
		         send("register");
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
				return true;
			}
			else
			{
				JOptionPane.showMessageDialog(null, new String(buf).substring(0,16));
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
		}return false;
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
			buf = new byte[256];
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
		return true;
  }

	public String getUserid(){
		return this.userid;
	}
}
