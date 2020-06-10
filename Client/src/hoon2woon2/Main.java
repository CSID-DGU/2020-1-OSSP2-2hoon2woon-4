package hoon2woon2;

import javax.swing.JFrame;

import org.psnbtech.Tetris;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends JFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 5622708864988613307L;

	public static Tetris tetris;
	public static Client client;
	public static MultiPlay multi;

	public static void main(String[] args) {
		JFrame mainframe = new JFrame();


		mainframe.setTitle("tetris main");
		mainframe.setSize(500, 500);
		mainframe.setVisible(true);
		mainframe.setDefaultCloseOperation(EXIT_ON_CLOSE);


		mainframe.setTitle("tetris main");// 제목
		mainframe.setSize(500, 500); // 크기
		mainframe.setVisible(true);// 생성
		mainframe.setDefaultCloseOperation(EXIT_ON_CLOSE);


		// JButton singleplaybutton = new JButton("singleplay");
		// mainframe.add(singleplaybutton);
		// ActionListener listener = new ActionListener() {

		//    @Override
		//    public void actionPerformed(ActionListener e) {

		client = new Client();
		tetris = new Tetris(client);
		tetris.startGame();
		multi = new MultiPlay(client);
		//    }
		// }
		// singleplaybutton.addActionListener(listener);

	}
}
