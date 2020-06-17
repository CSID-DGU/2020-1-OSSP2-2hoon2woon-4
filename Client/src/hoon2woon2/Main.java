package hoon2woon2;

import java.io.File;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.psnbtech.*;

import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
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
	Image img = Toolkit.getDefaultToolkit().createImage("Client/resources/Images/Main_Image.gif");
	ImageIcon start1 = new ImageIcon("Client/resources/Images/start1.jpg");
	ImageIcon start2 = new ImageIcon("Client/resources/Images/start2.jpg");

	Image img_size = new ImageIcon("Client/resources/Images/Main_Image.gif").getImage();
	int img_width = img_size.getWidth(null);
	int img_height = img_size.getHeight(null);

	public static void main(String[] args) {
		//new Main();
		//while(true){
		//	System.out.println(num);
		//	if(num==1){
				client = new Client();
				tetris = new Tetris(client);
				tetris.startGame();
				multi = new MultiPlay(client);
		//	}
		//}
	}
}
