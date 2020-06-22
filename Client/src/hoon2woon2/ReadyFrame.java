//package hoon2woon2;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowEvent;
//import java.util.Vector;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.ListSelectionModel;
//import javax.swing.event.ListSelectionListener;
//
//class NetworkThread extends Thread {
//	private Client c;
//	private int  memberNum;
//	private static String roomName;
//	private String message;
//	private String sendMessage="ready";
//	private Vector <String> users = new Vector<String>();
//	private boolean master = false;
//	
//	public NetworkThread(Client c) {
//		super("Network");
//		this.c = c;
//	}
//	
//	public void run() {
//		while(true) {
//			
//			
//		}
//		
//	}
//	
//	public void makeRoom(int memberNum,String roomName) {
//
//	}
//	
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource()==btn_gameStart) {
//			if(master) {
//			setSendMessage("gamestart");
//			}
//			else {
//				JOptionPane.showMessageDialog(null, "Permission denied. You can't start the game.");
//			}
//		// exit 처리
//		}	
//	}
//	
//
//	public void windowClosing(WindowEvent e) {
//		client.send("exit");
//	}
//	
//}
//
//
//public class ReadyFrame extends JFrame implements ActionListener {
//
//	private static final long serialVersionUID = -85952825035410402L;
//	private int  memberNum;
//	private static String roomName;
//	private String message;
//	private String sendMessage="ready";
//	private Vector <String> users = new Vector<String>();
//	private boolean master = false;
//	
//	public static JLabel la_name = new JLabel(getRoomName());
//	public static JButton btn_gameStart = new JButton("Game Start");
//	public static JList userList;
//	
//	Client client;
//	MultiFrame multi;
//	
//	ReadyFrame(){	
//	};
//	
//	ReadyFrame(MultiFrame m, Client c){
//
//		super("Waiting Room");
//		client = c;
//		this.memberNum = m.getMemberNum()+1;
//		this.setRoomName(m.getRoomName());
//		
//		setLayout(null);
//		
//		btn_gameStart.addActionListener(this);
//		la_name.setBounds(13,10,60,25);
//		btn_gameStart.setBounds(80,10,50,25);
//		
//		userList = new JList(getUsers());
//		
//		// add(userList);
//		add(btn_gameStart);
//		add(la_name);
//		
//		setSize(400,400);
//		setVisible(true);
//		
//		while(true) {
//			client.send(getSendMessage());
//			message = client.receive();
//			
//			if(message.equals("success")) {
//				setSendMessage("game");
//				continue;
//			}
//			else if(client.equals("notinroom")) {
//				JOptionPane.showMessageDialog(null, "error !!!");
//				dispose();
//			}
//			else if(client.equals("start")){
//				MultiPlay multiplay = new MultiPlay(client);
//			}
//			else if(client.equals("fail")) {
//				JOptionPane.showMessageDialog(null, "Permission denied. you can't start the game.");
//				continue;
//			}
//			else {
//				String [] tok = message.split(",");
//				
//				if(tok[0].equals(client.getUserid()))
//					setMaster(true);
//				
//				for(int i=0;i<tok.length;i++) {
//					getUsers().add(tok[i]);
//				}
//				
//				makeRoom(memberNum,getRoomName());	
//		  }
//		}
//	};
//	
//	public static void main(String[] args) {
//		NetworkThread t1 = new NetworkThread(client);
//		t1.start();
//	}
//}
