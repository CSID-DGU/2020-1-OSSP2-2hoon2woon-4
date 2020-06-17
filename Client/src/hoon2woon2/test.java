package hoon2woon2;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		Tetris t = new Tetris(c);
		t.setGamer(1);
		t.startGame();
		BoardPanel b = new BoardPanel(t);
		
	}

}
