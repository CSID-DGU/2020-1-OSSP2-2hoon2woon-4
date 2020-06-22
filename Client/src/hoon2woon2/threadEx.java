//package hoon2woon2;
//
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Vector;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JOptionPane;
//
//class SocketThread extends Thread{
//	Client c;
//
//	private String receivedMessage;
//	private static String sendMessage="ready";
//	static boolean master = false;
//	static Vector<String> users = new Vector<String>();
//	
//	SocketThread(Client c) {
//		super("Socket");
//		this.c = c ;
//	}
//	
//	public void run() {
//		while(true) {
//			c.send(sendMessage);
//			receivedMessage = c.receive();
//			
//			if(receivedMessage.equals("success")) {
//				sendMessage = "game";
//			}
//			
//			else if(receivedMessage.equals("notinroom")) {
//				JOptionPane.showMessageDialog(null, "error !!!");
//				break;
//			}
//			
//			else if(receivedMessage.equals("start")) {
//				
//			}
//			
//			else if(receivedMessage.contentEquals("fail")) {
//				JOptionPane.showMessageDialog(null, "Permission denied. you can't start the game.");
//				continue;
//			}
//			else {
//				users.clear();
//				// System.out.println(receivedMessage);
//				String [] tok = receivedMessage.split(",");
//				
//				if(tok[0].equals(c.getUserid()))
//					master=true;
//				
//				for(int i=0;i<tok.length;i++) {
//					// System.out.println(tok[i]);
//					users.add(tok[i]);
//				}
//				
////				notify();
////				try {
////					wait();
////				}catch(InterruptedException e) {
////					e.printStackTrace();
////				}
//				
//				}
//			}
//		}
//	}
//	
//
//public class threadEx extends JFrame implements ActionListener {
//	
//	private static final long serialVersionUID = -485952825035410402L;
//	public static JLabel la_name;
//	public static JButton btn_gameStart = new JButton("Game Start");
//	public static JList userList;
//	
//	static Client client;
//	MultiFrame multi;
//	
//	threadEx(Client c,MultiFrame m) {
//		
//		super("Waiting Room");
//		client = c;
//		
//		setLayout(null);
//		
//		la_name = new JLabel("Room name: " + m.roomName);
//		
//		
//		btn_gameStart.addActionListener(this);
//		la_name.setBounds(0,10,300,25);
//		btn_gameStart.setBounds(60,40,100,25);
//		userList = new JList(SocketThread.users);
//		userList.setBounds(30, 70, 200,200);
//		
//		add(userList);
//		add(btn_gameStart);
//		add(la_name);
//		
//		setSize(300,300);
//		setVisible(true);
//		SocketThread t1 = new SocketThread(client);
//		t1.start();
////		while(true) {
////		try {
////		wait();
////		} 
////		catch(InterruptedException e) {
////			e.printStackTrace();
////			}
////		
////		userList.setListData(SocketThread.users);
////		}
//	}
//	
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource()==btn_gameStart) {
//				dispose();
//				MultiPlay m = new MultiPlay(client);
//			}
//		}
//		
//	public static void main(String[] args) throws InterruptedException {
//		SocketThread t1 = new SocketThread(client);
//		t1.start();
//	}
//
//}
