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

import org.psnbtech.Tetris;

/**
 * 2020-06-10
 * @author Cha-Seung-Hoon
 * if click item_login at Tetris
 * then call loginframe and start login
 */

public class MultiFrame extends JFrame implements ActionListener{
	
	//private static final long serialVersionUID = -3742138942119483831L;
	Vector <String> vec = new Vector<String>();
	private JList lstRoom;
	private String roomInfo;
	public static JLabel la_roomName = new JLabel("Room Name:");
	public static JTextField tf_roomName = new JTextField(12);
	public static JButton btn_reload = new JButton("Reload");
	public static JButton btn_MakeorEnter = new JButton("Make Room / Enter");
	Client client;
	Tetris tetris;
	
	public MultiFrame(Tetris t, Client c) {
		super("Tetris.net");
		
		client = c;
		tetris = t;
		
		//String roomInfo = c.receive();
		
		setLayout(new FlowLayout());
		
		String roomInfo = "let's play,2\nbattle,4\ntetris Gosu Please,2\naaaa,1\nbbbbbb,2\nacacacaca,1\na,1\nb,2\nc,3";
		
		updateVector(roomInfo);
		lstRoom = new JList(vec);
		
		
		btn_reload.addActionListener(this);
		btn_MakeorEnter.addActionListener(this);
		btn_reload.setBounds(265, 70, 80, 20);
		la_roomName.setBounds(10,10,90,25);
		tf_roomName.setBounds(90,10,110,25);
		btn_MakeorEnter.setBounds(210,10,140,25);
		lstRoom.setBounds(10,40,250,200);
		lstRoom.setVisibleRowCount(5);
		
		add(la_roomName);
		add(tf_roomName);
		add(btn_MakeorEnter);
		add(lstRoom);
		add(new JScrollPane(lstRoom));
		add(btn_reload);
		
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
	
	public void reload() { // chacha
		
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btn_reload) {
			// client.send("reload");
			//roomInfo = client.receive();
			roomInfo="s,1\nx,2\ny,3\nz,3\nb,2\ncc,1\nbb,1\ncdcd,1";
			updateVector(roomInfo);
			
			lstRoom= new JList(vec);
			
			add(lstRoom); // should fixed >>> don't change immediately after reload clicked 
		}
		if(event.getSource()==btn_MakeorEnter) {
			client.send(roomInfo);
			String roomName;
			boolean isExist=false;
			
			if(isExist) {  // enter the existing room
				
			}
			else { // make the new room
				JOptionPane.showMessageDialog(null, "Room Made Successfully!");
				dispose();
				// 멅티 플레이 화면 부르기
			}
			dispose();
		}
	}
}
