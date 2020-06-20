
package hoon2woon2;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.psnbtech.*;

import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Main extends JFrame{
	/**
	 * Kim Jihoon
	 * Main UI
	 * 2020.06.11
	 */
	private static final long serialVersionUID = 5622708864988613307L;
	
	public static Tetris tetris;
	public static Client client;
	public static MultiPlay multi;
	public static BoardPanel board;
	public static SidePanel side;

	public static int num = 0;
	
	Image img = Toolkit.getDefaultToolkit().createImage(new File("").getAbsolutePath() + File.separator+ "resources" + File.separator+"Images"+ File.separator+ "Main_Image.gif");
    ImageIcon start1 = new ImageIcon(new File("").getAbsolutePath() + File.separator+ "resources" + File.separator+"Images"+ File.separator+ "start1.jpg");
    ImageIcon start2 = new ImageIcon(new File("").getAbsolutePath() + File.separator+ "resources" + File.separator+"Images"+ File.separator+ "start2.jpg");

    Image img_size = new ImageIcon(new File("").getAbsolutePath() + File.separator+ "resources" + File.separator+"Images"+ File.separator+ "Main_Image.gif").getImage();
    int img_width = img_size.getWidth(null);
    int img_height = img_size.getHeight(null);

    public Main(){
        this.setTitle("Tetris main");
        this.setSize(img_width,img_height+30);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color(0,0,0));

        myPanel panel = new myPanel();
        panel.setBounds(0, 0, img_width, img_height);

        JButton singleplaybutton = new JButton("");
        MyMouseListener listener = new MyMouseListener();
        singleplaybutton.setHorizontalAlignment(JButton.CENTER);
        singleplaybutton.setIcon(start1);
        singleplaybutton.setVisible(true);
        singleplaybutton.addMouseListener(listener);
        singleplaybutton.setBorderPainted(false);
        singleplaybutton.setFocusPainted(false);
        singleplaybutton.setContentAreaFilled(false);

        singleplaybutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                num = 1;
                dispose();
                System.out.println(num);
            }
        });

        this.setLayout( new BorderLayout() );
        panel.setLayout( new FlowLayout() );
        panel.add(singleplaybutton);
        this.add(panel);
        this.setVisible(true);
        panel.setVisible(true);
    }

    class MyMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setIcon(start1);
            b.setVisible(true);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setIcon(start1);
            b.setVisible(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setIcon(start2);
            b.setVisible(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setIcon(start1);
            b.setVisible(true);
        }

    }

    class myPanel extends JPanel{
        public void paint(Graphics g){
            g.drawImage(img, this.getX() + this.getWidth()/2 - img_width/2, this.getY() + this.getHeight()/2 - img_height/2, this);
        }
    }

    public static void main(String[] args) {
        new Main();
        while(true){
            System.out.println(num);
            if(num==1){
                client = new Client();
                tetris = new Tetris(client);
                tetris.startGame();
                multi = new MultiPlay(client);
            }
        }
    }
}
