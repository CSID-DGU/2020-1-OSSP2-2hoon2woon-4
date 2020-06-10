package hoon2woon2;

import javax.swing.JFrame;

import org.psnbtech.Tetris;

public class Main extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public static Tetris tetris;
	public static Client client;
	
	public static void main(String[] args) {
		client = new Client();
		tetris = new Tetris(client);
		tetris.startGame();
	}
}
