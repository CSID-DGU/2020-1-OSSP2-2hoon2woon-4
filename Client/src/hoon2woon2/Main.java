package hoon2woon2;

import org.psnbtech.Tetris;

public class Main {
	public static Tetris tetris;
	public static Client client;
	public static MultiPlay multi;
	
	public static void main(String[] args) {


		tetris = new Tetris(client);
		tetris.startGame();
		multi = new MultiPlay(client);
	}
}
