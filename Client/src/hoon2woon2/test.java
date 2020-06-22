package hoon2woon2;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		c.setGamerCount(2);
		MultiPlay p = new MultiPlay(c);
		
		Tetris t = new Tetris(c,p);
		p.start();
		BoardPanel b = new BoardPanel(t);
		
	}

}
