package hoon2woon2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.psnbtech.Tetris;

/**2020.06.10
 * Seperation of Register frame from login frame
 * cha seung hoon
 * */

public class RegisterFrame extends JFrame implements ActionListener{
	// private static final long serialVersionUID = -3742138942119483831L;
	
	JPanel pn_content = new JPanel();
	JLabel la_id = new JLabel("I  D:");
	JLabel la_pw = new JLabel("P W:");
	JLabel la_guideId = new JLabel("# Your ID should ~ ");
	JLabel la_guidePw = new JLabel("# Your Password should ~ ");
	JTextField tf_id = new JTextField(16);
	JPasswordField pf_pw = new JPasswordField(16);
	public static JButton btn_register = new JButton("Register");
	
	Client client;
	
	public RegisterFrame(Tetris t, Client c) {
		super("Register");
		client = c;
		
		pn_content.setLayout(null);
		
		btn_register.addActionListener(this);
		
		la_id.setBounds(13, 10, 60, 25);
		tf_id.setBounds(45, 10, 210, 25);
		btn_register.setBounds(265, 10, 85, 25);
		la_pw.setBounds(10, 40, 60, 30);
		pf_pw.setBounds(45, 40, 210, 25);
		la_guideId.setBounds(45,65,210,25);
		la_guidePw.setBounds(45,85,210,25);
		
		pn_content.add(la_id);
		pn_content.add(tf_id);
		pn_content.add(btn_register);
		pn_content.add(la_pw);
		pn_content.add(pf_pw);
		pn_content.add(la_guideId);
		pn_content.add(la_guidePw);
		add(pn_content);
		
		setSize(360, 150);
		setLocation(t.getLocation().x+t.getSize().width/2-180, t.getLocation().y+t.getSize().height/2-60);
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btn_register) {
			client.register(tf_id.getText(), pf_pw.getPassword());
		}
	}
}
