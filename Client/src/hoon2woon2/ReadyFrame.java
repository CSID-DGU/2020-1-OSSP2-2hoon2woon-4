package hoon2woon2;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReadyFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -485952825035410402L;
	private int  memberNum;
	private int  order;
	private String roomName;
	private String message;
	
	JPanel pn_ready = new JPanel();
	JLabel la_name = new JLabel(this.roomName);
	public static JButton btn_gameStart = new JButton("Game Start");
	
	Client client;
	
	ReadyFrame(){
		
	};
	
	ReadyFrame(int memberNum,String roomName){
		super("Waiting Room");
		
		this.memberNum = memberNum+1;
		this.roomName = roomName;
		this.order = memberNum +1;
		makeRoom(this.memberNum,this.roomName);
		
		String message;
		
		while(true) {
			client.send("readyroom");
			message = client.receive();
			
			if(message.equals("readyroom")) {
				continue;
			}
			else if(client.equals("interrupt")) {
				client.send("update");
				message = client.receive();
				update(message);
				
				makeRoom(this.memberNum,this.roomName);
			}
		}
	};
	
	public void makeRoom(int memberNum,String roomName) {
		pn_ready.setLayout(null);
		la_name.setBounds(13,10,60,25);
		
		client.send("getroominfo");
		message = client.receive();
		
		if(order == 1) { // if master
			btn_gameStart.addActionListener(this);
		}
		else {
			
		}
		
		
	}
	
	public void update(String message) {
		this.memberNum = Integer.parseInt(message);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btn_gameStart) {
			MultiPlay m = new MultiPlay(client);
		}
	}
	
	public void paint(Graphics g) {
		
	}
	
	public void repaint(Graphics g) {
		
	}
}
