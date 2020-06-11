package hoon2woon2;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.psnbtech.Tetris;

/**
 * 2020-04-28
 * @author Seungun-Park
 * if click item_login at Tetris
 * then call loginframe and start login
 */

public class LoginFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = -3742138942119483831L;
	
	JPanel pn_content = new JPanel();
	JLabel la_id = new JLabel("I  D:");
	JLabel la_pw = new JLabel("P W:");
	JTextField tf_id = new JTextField(16);
	JPasswordField pf_pw = new JPasswordField(16);
	public static JButton btn_login = new JButton("Login");
	public static JButton btn_register = new JButton("Register");
	
	Client client;
	Tetris tetris;
	
	public LoginFrame(Tetris t, Client c) {
		super("login");
		
		client = c;
		tetris = t;
		
		pn_content.setLayout(null);
		
		btn_login.addActionListener(this);
		btn_register.addActionListener(this);
		
		la_id.setBounds(13, 10, 60, 25);
		tf_id.setBounds(45, 10, 210, 25);
		btn_login.setBounds(265, 10, 70, 25);
		la_pw.setBounds(10, 40, 60, 30);
		pf_pw.setBounds(45, 40, 210, 25);
		btn_register.setBounds(265, 40, 70, 25);
		
		pn_content.add(la_id);
		pn_content.add(tf_id);
		pn_content.add(btn_login);
		pn_content.add(la_pw);
		pn_content.add(pf_pw);
		pn_content.add(btn_register);
		add(pn_content);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame)e.getWindow();
				client = null;
				tetris.loginframe = null;
				frame.dispose();
			}
		});
		
		setSize(360, 120);
		setLocation(t.getLocation().x+t.getSize().width/2-180, t.getLocation().y+t.getSize().height/2-60);
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btn_login) {
			if(client.login(tf_id.getText(), pf_pw.getPassword())) {
				JOptionPane.showMessageDialog(null, "login success");
				client = null;
				tetris.loginframe = null;
				dispose();
			}
			else
				JOptionPane.showMessageDialog(null, "login failed");
		}
		if(event.getSource() == btn_register) {
			if(client.register(tf_id.getText(), pf_pw.getPassword()))
				JOptionPane.showMessageDialog(null, "register success");
			else
				JOptionPane.showMessageDialog(null, "register failed");
		}
	}
}
