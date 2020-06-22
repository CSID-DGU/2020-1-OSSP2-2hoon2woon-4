package hoon2woon2;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.psnbtech.Tetris;

/**
 * 2020-06-10
 * @author Cha-Seung-Hoon
 * if click item_login at Tetris
 * then call loginframe and start login
 */

public class MultiFrame extends JFrame implements ActionListener, ListSelectionListener{
	
	//private static final long serialVersionUID = -3742138942119483831L;
	Vector <String> vec = new Vector<String>();
	private JList lstRoom;
	private String roomInfo;
	public static String roomName;
	private String message;
	private int memberNum;
	private boolean isEmpty=false;
	public static JLabel la_roomName = new JLabel("Room Name:");
	public static JTextField tf_roomName = new JTextField(12);
	public static JButton btn_reload = new JButton("Reload");
	public static JButton btn_MakeRoom = new JButton("Make Room");
	public static JScrollPane sp;
	Client client;
	public static Tetris tetris ;
	
	public MultiFrame(Tetris t, Client c) {
		super("Tetris.net");
		
		client = c;
		tetris = t;
		
		setLayout(null);
		client.send("getroominfo");
		
		try {
		roomInfo = str_refining(client.receive());
		}
		catch(Exception e) {
		e.printStackTrace();	
		}
		
		if(roomInfo.equals("noroominfo")) {
		vec.add("There is no room");
		}
		else {
			updateVector(roomInfo);	
		}
		
		lstRoom = new JList(vec);	
		
		btn_reload.addActionListener(this);
		btn_MakeRoom.addActionListener(this);
		btn_reload.setBounds(265, 70, 80, 20);
		la_roomName.setBounds(10,10,90,25);
		tf_roomName.setBounds(90,10,110,25);
		btn_MakeRoom.setBounds(210,10,140,25);
		// lstRoom.setBounds(10,40,250,200);
		lstRoom.setVisibleRowCount(5);
		lstRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstRoom.addListSelectionListener(this);
		//	lstRoom.setVisible(true);
		
		add(la_roomName);
		add(tf_roomName);
		add(btn_MakeRoom);
		// add(lstRoom);
		sp = new JScrollPane(lstRoom);
		sp.setBounds(10,40,250,100);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(sp);
		//add(new JScrollPane(lstRoom));
		add(btn_reload);
		lstRoom.setVisible(true);
			
		if(!roomInfo.equals("noroominfo"))
			isEmpty = true;
				
		setSize(400, 400);
		setLocation(t.getLocation().x+t.getSize().width/2-180, t.getLocation().y+t.getSize().height/2-60);
		setResizable(false);
		setVisible(true);
	}
	
	public int getMemberNum() {
		return this.memberNum;
	}
	
	public String getRoomName() {
		return this.roomName;
	}
	
	public void updateVector(String str) {
		vec.clear();

		String col;
		StringTokenizer tokCol = new StringTokenizer(str,"\n");
		StringTokenizer tokRow;
		
		while(tokCol.hasMoreTokens()) {
			tokRow = new StringTokenizer(tokCol.nextToken(),",");
			col = "Room Name: "+tokRow.nextToken()+" ("+tokRow.nextToken()+"/4)";
			vec.add(col);
		}
	}
	
	public void reload() { 
		client.send("getroominfo"); // buffer 1024
		roomInfo=str_refining(client.receive());
		if(roomInfo.equals("noroominfo")) {
			vec.add("There is no room");
		}
		else
			updateVector(roomInfo);
		
		lstRoom.setListData(vec);
	}
	
	public void enterRoom(String roomName) {
		client.send("enterroom");
		System.out.println("after enterroom:"+roomName);
		this.roomName = roomName;
		client.send(roomName);
		message = client.receive();
		System.out.println("after receive message:"+message);
		
		if(message.equals("fail")) {
			JOptionPane.showMessageDialog(null, "Room is full !!!");
			reload();
		}
		else{
			memberNum=Integer.parseInt("1"); // TODO: "1로 되는 이유
			System.out.println(memberNum);
			dispose();
			// ReadyFrame r = new ReadyFrame(this,client);
	//		threadEx t = new threadEx(client,this);
			waiting w = new waiting(client,this);
			}
		
	}
	
	public void createRoom(String roomname) {
//		ReadyFrame ready = new ReadyFrame(this,client);
		dispose();
//		threadEx t = new threadEx(client,this);
		waiting w = new waiting(client,this);
	}
	
	public String str_refining(String message) {
		String result=message.replaceAll("\0","");
		return result;
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btn_reload) {
			reload();
		}
		
		if(event.getSource()==btn_MakeRoom) {
			client.send("createroom"); // buffer 128
			client.send(tf_roomName.getText());

			
			roomName = str_refining(client.receive());
			
			if(roomName.equals("fail")) { 	// TODO : if room aleady exist?
				JOptionPane.showMessageDialog(null, "Room already Exists!!!");
				reload();
			}
			else {
				createRoom(roomName);
			}
		}
		
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int ind = lstRoom.getSelectedIndex();
		roomName = vec.elementAt(ind).substring(11,vec.elementAt(ind).indexOf("(")-1);
		enterRoom(roomName);
	}
	
	public void windowClosing(WindowEvent e) {
		client.send("exit");
	}
}
 