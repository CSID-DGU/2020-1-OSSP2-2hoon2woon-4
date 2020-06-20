package hoon2woon2;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private String myRoomInfo;
	private String message;
	private boolean isEmpty=false;
	public static JLabel la_roomName = new JLabel("Room Name:");
	public static JTextField tf_roomName = new JTextField(12);
	public static JButton btn_reload = new JButton("Reload");
	public static JButton btn_MakeRoom = new JButton("Make Room");
	Client client;
	Tetris tetris;
	
	public MultiFrame(Tetris t, Client c) {
		super("Tetris.net");
		
		client = c;
		tetris = t;
		
		setLayout(new FlowLayout());
		client.send("getroominfo");
		
		try {
		
		System.out.println("1.");
		roomInfo = client.receive();
		System.out.println(roomInfo);
		}
		catch(Exception e) {
		e.printStackTrace();	
		}
		
		System.out.print	("roominfo: "+roomInfo);
		System.out.println("aa");
		System.out.println(roomInfo.equals("noroomin"));
		
		if(roomInfo.equals("noroominfo")) {
			updateVector(roomInfo);
			lstRoom = new JList(vec);	
		}
		
		btn_reload.addActionListener(this);
		btn_MakeRoom.addActionListener(this);
		btn_reload.setBounds(265, 70, 80, 20);
		la_roomName.setBounds(10,10,90,25);
		tf_roomName.setBounds(90,10,110,25);
		btn_MakeRoom.setBounds(210,10,140,25);
		
		if(roomInfo.equals("noroominfo")) {
		lstRoom.setBounds(10,40,250,200);
		lstRoom.setVisibleRowCount(5);
		lstRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstRoom.addListSelectionListener(this);
		}
		
		add(la_roomName);
		add(tf_roomName);
		add(btn_MakeRoom);
		
		if(roomInfo.equals("noroominfo"))
		{	
			add(lstRoom);
			add(new JScrollPane(lstRoom));
		}
			add(btn_reload);
		
			
		if(roomInfo.equals("noroominfo"))
			isEmpty = true;
//		addWindowListener(new WindowAdapter(){
//			public void windowClosing(WindowEvent e) {
//				JFrame frame = (JFrame)e.getWindow();
//				client = null;
//				tetris.loginframe = null;
//				frame.dispose();
//			}
//		});

				
		setSize(450, 200);
		setLocation(t.getLocation().x+t.getSize().width/2-180, t.getLocation().y+t.getSize().height/2-60);
		setResizable(false);
		setVisible(true);
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
		client.send("getroominfo"); 
		roomInfo = client.receive();
		System.out.println("reload:"+roomInfo);
		updateVector(roomInfo);
		lstRoom = new JList(vec);
		if(isEmpty) {
			lstRoom.setBounds(10,40,250,200);
			lstRoom.setVisibleRowCount(5);
			lstRoom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lstRoom.addListSelectionListener(this);
			this.add(lstRoom);
			this.add(new JScrollPane(lstRoom));
			isEmpty=false;
		}
		
		add(lstRoom); // TODO: Changing immediately after reload clicked 
		lstRoom.setVisible(true);
	}
	
	public void enterRoom(String roomName) {
		client.send(roomName);
		message = client.receive();
		
		if(message.equals("full")) {  // enter the existing room
			JOptionPane.showMessageDialog(null, "Room is full !!!");
			reload();
		}
		else if (message.equals("enter")){
			client.send(client.getUserid());
			message = client.receive();
			int memberNum=Integer.parseInt(message);
			
			ReadyFrame r = new ReadyFrame(memberNum,roomName);
		}
		
	}
	
	public void createRoom(String roomname) {
		System.out.println(myRoomInfo);
		ReadyFrame ready = new ReadyFrame(1,roomname);
		System.out.println(myRoomInfo);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btn_reload) {
			reload();
		}
		
		if(event.getSource()==btn_MakeRoom) {
			System.out.println("make room clicked");
			client.send("createroom");
			client.send(tf_roomName.getText());
			
			myRoomInfo = client.receive();
			System.out.println(myRoomInfo);
			
			if(myRoomInfo.equals("fail")) { 	// TODO : if room aleady exist?
				JOptionPane.showMessageDialog(null, "Room already Exists!!!");
				reload();
			}
			else {
				createRoom(myRoomInfo);
				System.out.println("create room");
			}
			//			enterRoom(roomName);
			//			 dispose();
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int ind = lstRoom.getSelectedIndex();
		myRoomInfo = vec.elementAt(ind).substring(vec.elementAt(ind).lastIndexOf("/")+1);
		enterRoom(myRoomInfo);
	}
}
 