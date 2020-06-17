package hoon2woon2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

import java.io.FileWriter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;

/**
 * 2020-05-18
 * @author Seungun-Park
 *
 */

public class RankPanel extends JPanel{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1365431727548231263L;
	
	/**
	 * The number of pixels used on a small insets (generally used for categories).
	 */
	private static final int SMALLEST_INSET = 10;
	
	/**
	 * The number of pixels used on a small insets (generally used for categories).
	 */
	private static final int SMALL_INSET = 20;
	
	/**
	 * The number of pixels used on a large insets.
	 */
	private static final int LARGE_INSET = 40;
	
	/**
	 * The y coordinate of the local high score category.
	 */
	private static final int LOCAL_INSET = 20;
	
	/**
	 * The y coordinate of the local high score category.
	 */
	private static final int ONLINE_INSET = 80;
	
	/**
	 * The y coordinate of the controls category.
	 */
	private static final int CONTROLS_INSET = 200;
	
	/**
	 * The number of pixels to offset between each string.
	 */
	private static final int TEXT_STRIDE = 25;
	
	/**
	 * The small font.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
	
	/**
	 * The large font.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);
	
	/**
	 * The color to draw the text and preview box in.
	 */
	private static final Color DRAW_COLOR = new Color(128, 192, 128);
	//private int[] high_score;//0:basic 1:item 2:interrupt
	private int high_score;
	
	/**
	 * Encryption
	 */
	private static final String encryptionKey = "2hoon2woontetris";
	private Cipher cipher;
	private SecretKeySpec secretKeySpec;
	
	/**
	 * The Tetris instance
	 */
	private Tetris tetris;
	
	/**
	 * score save & load
	 */
	private static final File file = new File(System.getProperty("user.dir"));

	private static Dimension d_start;
	
	public RankPanel(Tetris tetris) {
		this.tetris = tetris;
		//high_score = new int[3];
		System.out.println(System.getProperty("user.dir") );
		try {
			FileInputStream fileInputStream = new FileInputStream("Bscore");
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			
			byte[] encr = new byte[16];
			for(int i = 0; i<16; i++) {
				bufferedInputStream.read(encr);
			}
			
			cipher = Cipher.getInstance("AES");
			secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptBytes = cipher.doFinal(encr);
				
			high_score = Integer.parseInt(new String(decryptBytes, "UTF-8"));
			bufferedInputStream.read(encr);
			
			bufferedInputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		d_start = new Dimension(200, BoardPanel.PANEL_HEIGHT);
		setSize(d_start);
		//setPreferredSize(d_start);
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(tetris.getGamer()==1) {	// chacha
		super.paintComponent(g);
		
		g.setColor(DRAW_COLOR);
		
		int offset;
		
		g.setFont(LARGE_FONT);
		g.drawString("Local High Score", SMALL_INSET, offset = LOCAL_INSET);
		g.drawString(Integer.toString(high_score), LARGE_INSET, offset += TEXT_STRIDE);
		
		g.drawString("Online Ranking", SMALL_INSET, offset = ONLINE_INSET);
			}
		}
	
	private void updateScore() {
		if(tetris.getScore() > high_score)
			high_score = tetris.getScore();
	}
	
	public void uploadScore() {
		try {			
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			
			byte[] encryptBytes = cipher.doFinal(Integer.toString(high_score).getBytes("UTF-8"));
			
			FileOutputStream writer = new FileOutputStream(file);
			writer.write(encryptBytes);
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		updateScore();
	}
}
