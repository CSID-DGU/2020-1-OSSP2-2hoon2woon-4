package hoon2woon2;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.psnbtech.Tetris;

class Socket extends Thread{
	Client c;
	Tetris t;
	private String receivedMessage;
	 static String sendMessage="ready";
	static boolean master = false;
	static Vector<String> users = new Vector<String>();
	
	
	Socket(Client c,Tetris t) {
		super("Socket");
		this.c = c ;
		this.t = t;
		users.add(c.getUserid());
	}
	
	public void run() {
		while(true) {
			c.send(sendMessage);
			receivedMessage = c.receive();
			receivedMessage = receivedMessage.replaceAll("\0", "");
			
			if(receivedMessage.equals("success")) {
				sendMessage = "ready";
			}
			
			else if(receivedMessage.equals("gamestart")) {
				sendMessage = "game";
			}
			else if(receivedMessage.equals("notinroom")) {
				JOptionPane.showMessageDialog(null, "error !!!");
				break;
			}
			
			else if(receivedMessage.equals("start")) {
				c.setGamerCount(users.size());
				c.setUserList(users);
				MultiPlay m = new MultiPlay(c,t);
				break;
			}
			
			else if(receivedMessage.contentEquals("fail")) {
				JOptionPane.showMessageDialog(null, "Permission denied. you can't start the game.");
				continue;
			}
			else {
				users.clear();
				String [] tok = receivedMessage.split(",");
				
				if(tok[0].equals(c.getUserid()))
					master=true;
				
				for(int i=0;i<tok.length;i++) {
					users.add(tok[i]);
				}
			
				}
			}
		}
	}
	

class GUI extends JFrame implements Runnable,ActionListener{
	private static final long serialVersionUID = -485952825035410402L;
	public static JLabel la_name;
	public static JButton btn_gameStart = new JButton("Game Start");
	public static JList userList;
	
	static Client client;
	MultiFrame multi;
	
	GUI(Client c,MultiFrame m) {
		
		super("Waiting Room");
		client = c;
		multi = m;
	}

	public void run() {
setLayout(null);
		
		la_name = new JLabel("Room name: " + multi.roomName);
		
		
		btn_gameStart.addActionListener(this);
		la_name.setBounds(0,10,300,25);
		btn_gameStart.setBounds(60,40,100,25);
		userList = new JList(Socket.users);
		userList.setBounds(30, 70, 200,200);
		
		add(userList);
		add(btn_gameStart);
		add(la_name);
		
		setSize(300,300);
		setVisible(true);
		
		while(true) {
		userList.setListData(Socket.users);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btn_gameStart) {
//				dispose();
//				MultiPlay m = new MultiPlay(client);
		if(Socket.master)	
			Socket.sendMessage = "gamestart";
		else
			JOptionPane.showMessageDialog(null, "Permission denied. you can't start the game.");
			}
		}
}


public class waiting {
	
	waiting(Client c,MultiFrame m) {
		Socket s = new Socket(c,m.tetris);
	
		Runnable r = new GUI(c,m);
		Thread g = new Thread(r);
		
		s.start();
		g.start();
	}
	
}
