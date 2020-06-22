package org.psnbtech;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


import hoon2woon2.*;

import hoon2woon2.Items.ItemManager;


/**
 * The {@code Tetris} class is responsible for handling much of the game logic and
 * reading user input.
 * @author Brendan Jones
 *
 */
public class Tetris extends JFrame implements ActionListener{
	
	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = -4722429764792514382L;

	/**
	 * The number of milliseconds per frame.
	 */
	private static final long FRAME_TIME = 1000L / 50L;

	/**
	 * The index for game mode
	 */
	private int mode = 0;

	/**
	 * The Group of variables or instances for item mode
	 */

	/**
	 * writer : choi-gowoon
	 * The ItemManager instance
	 */
	private ItemManager itemManager;

	private MultiPlay multiPlay;

	/**
	 * writer : github.com/choi-gowoon
	 * 2020.05.16
	 * boolean flag for items
	 */
	private boolean scoreIndex;
	private long scoreTimer;

	private boolean rotationIndex;
	private long rotaionTimer;

	private boolean reverseIndex;
	private long reverseTimer;

	private static int TIME_LIMIT = 30;

	/**
	 * The number of pieces that exist.
	 */
	private static final int TYPE_COUNT = TileType.values().length-2;

	/**
	 * The BoardPanel instance.
	 */
	private BoardPanel board;
	
	/**
	 * The SidePanel instance.
	 */
	private SidePanel side;
	
	/**
	 * The RankPanel instance.
	 */
	private RankPanel rank;
	
	/**
	 * Whether or not the game is paused.
	 */
	private boolean isPaused;
	
	/**
	 * Whether or not we've played a game yet. This is set to true
	 * initially and then set to false when the game starts.
	 */
	private boolean isNewGame;
	
	/**
	 * Whether or not the game is over.
	 */
	private boolean isGameOver;
	
	/**
	 * The current level we're on.
	 */
	private int level;
	
	/**
	 * The current score.
	 */
	private int score;
	
	/**
	 * The random number generator. This is used to
	 * spit out pieces randomly.
	 */
	private Random random;
	
	/**
	 * The clock that handles the update logic.
	 */
	private Clock logicTimer;
				
	/**
	 * The current type of tile.
	 */
	private TileType currentType;
	
	/**
	 * The next type of tile.
	 */
	private TileType nextType;

	/**
	 * writer : github.com/choi-gowoon
	 * 2020.04.26
	 * The type of hold tile.
	 */
	private TileType holdType;

	/*
	 * Whether or not we can hold
	 */
	private boolean isHoldable;

	/*
	 * The current column of our tile.
	 */
	private int currentCol;
	
	/**
	 * The current row of our tile.
	 */
	private int currentRow;
	
	/**
	 * The current rotation of our tile.
	 */
	private int currentRotation;
		
	/**
	 * Ensures that a certain amount of time passes after a piece is
	 * spawned before we can drop it.
	 */
	private int dropCooldown;
	
	/**
	 * The speed of the game.
	 */
	private float gameSpeed;
	
	/**
	 * 2020-04-17 Seungun-Park
	 * Tetris bag
	 */
	private ArrayList<Integer> tetrisBag;
	
	/**
	 * 2020-04-25 Seungun-Park
	 * resize
	 */
	private Dimension d_start;
	private Dimension d_now;
	
	private int addTimer=1;
	
	public int flag = 0;
	private int gamer = 1; // chacha
	
	/** 2020-04-28 Seungun-Park
	 */
	//program menu
	JMenuBar menu = new JMenuBar();
	JMenu mn_file = new JMenu("File");
	JMenuItem item_new = new JMenuItem("New Game");
	JMenuItem item_exit = new JMenuItem("Exit");
	JMenu mn_account = new JMenu("Account");
	JMenuItem item_login = new JMenuItem("Login");	
	JMenuItem item_logout = new JMenuItem("Logout");
	JMenuItem item_register = new JMenuItem("Register");	//	cha seung hoon 2020.06.10 Register Frame
	JMenu mn_mode = new JMenu("Mode");
	JMenuItem item_basic = new JMenuItem("Basic");
	JMenuItem item_disturb = new JMenuItem("Interrupt");
	JMenuItem item_item = new JMenuItem("Item");
	JMenuItem item_multi = new JMenuItem("Multi");	// cha seung hoon 2020.06.10 Register Frame
	//socket program
	private Client client;
	
	
	public LoginFrame loginframe;

	/*
	 * writer: cha seung hoon
	 * hard drop
	 * 2020.04.28
	 * */
	private boolean isHardDrop = false;
	private boolean beforeVal = false;
	
   /*
    * writer: Jihoon Kim
    * media
    * 2020.06.09
	* */
	String getpath = Paths.get("").toUri().toString();
	Mediaplay player = new Mediaplay();


	/**
	 * Creates a new Tetris instance. Sets up the window's properties,
	 * and adds a controller listener.
	 */
	
	public Tetris(Client c) {
		/*
		 * Set the basic properties of the window.
		 */
		super("Tetris");
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);

		/*
		 * play music
		 */
		player.play_music("backgroundmusic_long.wav", -10.0f);

		client = c;
		
		//loginframe = new LoginFrame(this, client);
		
		/*
		 * Initialize the BoardPanel and SidePanel instances.
		 */
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		
	
		this.rank = new RankPanel(this, client);
		
		this.tetrisBag = new ArrayList<Integer>();
		
		/**2020-04-28 Seungun-Park
		 * Menu control
		 */
		
		item_new.addActionListener(this);
		item_exit.addActionListener(this);
		item_login.addActionListener(this);
		item_logout.addActionListener(this);
		item_register.addActionListener(this); // cha seung hoon 2020.06.10 Register Frame
		item_basic.addActionListener(this);
		item_disturb.addActionListener(this);
		item_item.addActionListener(this);
		item_multi.addActionListener(this);	// cha seung hoon 2020.06.10 Register Frame
		
		mn_file.add(item_new);
		mn_file.add(item_exit);
		mn_account.add(item_login);
		mn_account.add(item_logout);
		mn_account.add(item_register);
		mn_mode.add(item_basic);
		mn_mode.add(item_disturb);
		mn_mode.add(item_item);
		mn_mode.add(item_multi);	//	cha seung hoon 2020.06.10 Register Frame
		
		menu.add(mn_file);
		menu.add(mn_account);
		menu.add(mn_mode);
		
		setJMenuBar(menu);
		
		/*
		 * Add the BoardPanel and SidePanel instances to the window.
		 */
		
		add(board);
		add(side);	
		add(rank);
		rank.rankup();
		rank.repaint();

		/*
		 * Adds a custom anonymous KeyListener to the frame.
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(isHardDrop) {
					beforeVal=true;
					isHardDrop=false;
				}
				else
					beforeVal=false;
				
				switch(e.getKeyCode()) {
				
				/*
				 * Drop - When pressed, we check to see that the game is not
				 * paused and that there is no drop cooldown, then set the
				 * logic timer to run at a speed of 25 cycles per second.
				 */
				case KeyEvent.VK_DOWN:
					if(!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(25.0f);
						player.play_music("t_move.wav", 0);
					}
					break;

				/*
				 * Move Left - When pressed, we check to see that the game is
				 * not paused and that the position to the left of the current
				 * position is valid. If so, we decrement the current column by 1.
				 */
				/**
				 * writer: choi gowoon
				 * Move Left and Move Right
				 * add flag for key reversing item
				 */
				case KeyEvent.VK_LEFT:
					if(reverseIndex){
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol++;
							player.play_music("t_move.wav", 0);
						}
					}
					else{
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol--;
							player.play_music("t_move.wav", 0);
						}
					}
					break;

				/*
				 * Move Right - When pressed, we check to see that the game is
				 * not paused and that the position to the right of the current
				 * position is valid. If so, we increment the current column by 1.
				 */
				case KeyEvent.VK_RIGHT:
					if(reverseIndex){
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol--;
							player.play_music("t_move.wav", 0);
						}
					}
					else{
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol++;
							player.play_music("t_move.wav", 0);
						}
					}
					break;

				/*
				 * Rotate Anticlockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece anticlockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to clockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
				/**
				 * writer: choi gowoon
				 * Rotate Anticlockwise and clockwise
				 * add flag for key nonRotation item
				 */
				case KeyEvent.VK_Z:
					if(rotationIndex){
						if(!isPaused) {
							rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
							player.play_music("t_rotate.wav", 0);
						}
					}
					break;
				
				/*
			     * Rotate Clockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece clockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to anticlockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
				case KeyEvent.VK_X:
					if(rotationIndex){
						if(!isPaused) {
							rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
							player.play_music("t_rotate.wav", 0);
						}
					}
					break;
					
				/*
				 * Pause Game - When pressed, check to see that we're currently playing a game.
				 * If so, toggle the pause variable and update the logic timer to reflect this
				 * change, otherwise the game will execute a huge number of updates and essentially
				 * cause an instant game over when we unpause if we stay paused for more than a
				 * minute or so.
				 */
				case KeyEvent.VK_P:
					if(!isGameOver && !isNewGame) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
					}
					break;
				
				/*
				 * Start Game - When pressed, check to see that we're in either a game over or new
				 * game state. If so, reset the game.
				 */
				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame) {
						resetGame();
					}
					break;

				/*
				 * writer : github.com/choi-gowoon
				 * 2020.04.26
				 * hold function
				 */
				case KeyEvent.VK_SHIFT:
					holdTile();
					player.play_music("hold.wav", 0);
					break;
				
				/*
				 * writer : cha seung hoon
				 * 2020. 04 .28
				 * Hard Drop
				 */
				case KeyEvent.VK_SPACE:
					isHardDrop=true;
					addTimer = 0;
					int cnt=0;
					while(board.isValidAndEmpty(currentType, currentCol, currentRow+cnt, currentRotation)) {
						cnt++;
					}
					currentRow+=cnt-1;
					updateGame();
					player.play_music("t_harddrop.wav", 0);
					break;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				/*
				 * Drop - When released, we set the speed of the logic timer
				 * back to whatever the current game speed is and clear out
				 * any cycles that might still be elapsed.
				 */
				case KeyEvent.VK_DOWN:
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				}
				
			}
			
		});
		
		
		/*
		 * Here we resize the frame to hold the BoardPanel and SidePanel instances,
		 * center the window on the screen, and show it to the user.
		 */
		getContentPane().setBackground(Color.BLACK);
		setSize(board.getWidth() + side.getWidth()*2, board.getHeight()+67);
		d_start = getSize();
		setMinimumSize(d_start);
		setLocationRelativeTo(null);
		setVisible(true);
		board.setVisible(true);
	
	
	}

	public Tetris(Client c, MultiPlay m) {
		/*
		 * Set the basic properties of the window.
		 */
		super("Tetris");
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		flag = 1;
		/*
		 * play music
		 */
		player.play_music("backgroundmusic_long.wav", -10.0f);

		client = c;
		
		//loginframe = new LoginFrame(this, client);
		
		/*
		 * Initialize the BoardPanel and SidePanel instances.
		 */
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		
		this.tetrisBag = new ArrayList<Integer>();
		
		/**2020-04-28 Seungun-Park
		 * Menu control
		 */
		
		item_new.addActionListener(this);
		item_exit.addActionListener(this);
		item_login.addActionListener(this);
		item_logout.addActionListener(this);
		item_register.addActionListener(this); // cha seung hoon 2020.06.10 Register Frame
		item_basic.addActionListener(this);
		item_disturb.addActionListener(this);
		item_item.addActionListener(this);
		item_multi.addActionListener(this);	// cha seung hoon 2020.06.10 Register Frame
		
		mn_file.add(item_new);
		mn_file.add(item_exit);
		mn_account.add(item_login);
		mn_account.add(item_logout);
		mn_account.add(item_register);
		mn_mode.add(item_basic);
		mn_mode.add(item_disturb);
		mn_mode.add(item_item);
		mn_mode.add(item_multi);	//	cha seung hoon 2020.06.10 Register Frame
		
		menu.add(mn_file);
		menu.add(mn_account);
		menu.add(mn_mode);
		
		setJMenuBar(menu);
		
		/*
		 * Add the BoardPanel and SidePanel instances to the window.
		 */
		
		add(board);
		add(side);	

		/*
		 * Adds a custom anonymous KeyListener to the frame.
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(isHardDrop) {
					beforeVal=true;
					isHardDrop=false;
				}
				else
					beforeVal=false;
				
				switch(e.getKeyCode()) {
				
				/*
				 * Drop - When pressed, we check to see that the game is not
				 * paused and that there is no drop cooldown, then set the
				 * logic timer to run at a speed of 25 cycles per second.
				 */
				case KeyEvent.VK_DOWN:
					if(!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(25.0f);
						player.play_music("t_move.wav", 0);
					}
					break;

				/*
				 * Move Left - When pressed, we check to see that the game is
				 * not paused and that the position to the left of the current
				 * position is valid. If so, we decrement the current column by 1.
				 */
				/**
				 * writer: choi gowoon
				 * Move Left and Move Right
				 * add flag for key reversing item
				 */
				case KeyEvent.VK_LEFT:
					if(reverseIndex){
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol++;
							player.play_music("t_move.wav", 0);
						}
					}
					else{
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol--;
							player.play_music("t_move.wav", 0);
						}
					}
					break;

				/*
				 * Move Right - When pressed, we check to see that the game is
				 * not paused and that the position to the right of the current
				 * position is valid. If so, we increment the current column by 1.
				 */
				case KeyEvent.VK_RIGHT:
					if(reverseIndex){
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol--;
							player.play_music("t_move.wav", 0);
						}
					}
					else{
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)&&!beforeVal) {
							currentCol++;
							player.play_music("t_move.wav", 0);
						}
					}
					break;

				/*
				 * Rotate Anticlockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece anticlockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to clockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
				/**
				 * writer: choi gowoon
				 * Rotate Anticlockwise and clockwise
				 * add flag for key nonRotation item
				 */
				case KeyEvent.VK_Z:
					if(rotationIndex){
						if(!isPaused) {
							rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
							player.play_music("t_rotate.wav", 0);
						}
					}
					break;
				
				/*
			     * Rotate Clockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece clockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to anticlockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
				case KeyEvent.VK_X:
					if(rotationIndex){
						if(!isPaused) {
							rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
							player.play_music("t_rotate.wav", 0);
						}
					}
					break;
					
				/*
				 * Pause Game - When pressed, check to see that we're currently playing a game.
				 * If so, toggle the pause variable and update the logic timer to reflect this
				 * change, otherwise the game will execute a huge number of updates and essentially
				 * cause an instant game over when we unpause if we stay paused for more than a
				 * minute or so.
				 */
				case KeyEvent.VK_P:
					if(!isGameOver && !isNewGame) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
					}
					break;
				
				/*
				 * Start Game - When pressed, check to see that we're in either a game over or new
				 * game state. If so, reset the game.
				 */
				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame) {
						resetGame();
					}
					break;

				/*
				 * writer : github.com/choi-gowoon
				 * 2020.04.26
				 * hold function
				 */
				case KeyEvent.VK_SHIFT:
					holdTile();
					player.play_music("hold.wav", 0);
					break;
				
				/*
				 * writer : cha seung hoon
				 * 2020. 04 .28
				 * Hard Drop
				 */
				case KeyEvent.VK_SPACE:
					isHardDrop=true;
					addTimer = 0;
					int cnt=0;
					while(board.isValidAndEmpty(currentType, currentCol, currentRow+cnt, currentRotation)) {
						cnt++;
					}
					currentRow+=cnt-1;
					updateGame();
					player.play_music("t_harddrop.wav", 0);
					break;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				/*
				 * Drop - When released, we set the speed of the logic timer
				 * back to whatever the current game speed is and clear out
				 * any cycles that might still be elapsed.
				 */
				case KeyEvent.VK_DOWN:
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				}
				
			}
			
		});
				
		/*
		 * Here we resize the frame to hold the BoardPanel and SidePanel instances,
		 * center the window on the screen, and show it to the user.
		 */
		getContentPane().setBackground(Color.BLACK);
		
		switch(m.getGamerCount()) {
		case 2:
			//setSize(board.getWidth() + side.getWidth()*2, board.getHeight()+67+side.getHeight());
			 setSize(650,557);
			board.setBounds(0,0,board.getWidth(),board.getHeight());
			side.setBounds(490, 0, 200, 123);
			 BoardPanel user1 = m.getBoard(0);
			 user1.setBounds(490,200,200,392);
			 add(user1);
			// user1.setVisible(true);
			break;
			
		case 3:
			setSize(board.getWidth() + side.getWidth()*2, board.getHeight()+67+side.getHeight());
			
			break;
			
		case 4:
			setSize(board.getWidth() + side.getWidth()*2, board.getHeight()+67+side.getHeight());
			
			break;
		}
		
		d_start = getSize();
		System.out.println(d_start);
		setMinimumSize(d_start);
//                         		setLocationRelativeTo(null);
		setVisible(true);
		board.setVisible(true);
	}
	
	/**
	 * writer : github.com/choi-gowoon
	 * hold function
	 */
	
	public void holdTile(){
		if(!isPaused && isHoldable) {
			TileType temp = currentType;
			if(holdType == null){
				currentType = getNextPieceType();
				nextType = TileType.values()[nextTetromino()];
			}
			else{
				currentType = holdType;
			}
			currentCol = currentType.getSpawnColumn();
			currentRow = currentType.getSpawnRow();
			currentRotation = 0;
			holdType = temp;
			isHoldable = false;
		}
	}
	
	/**
	 * Starts the game running. Initializes everything and enters the game loop.
	 */
	public void startGame() {
		/*
		 * Initialize our random number generator, logic timer, and new game variables.
		 */
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;
		this.itemManager = new ItemManager(this, board);
		this.board.clear();
		scoreIndex = false;
		rotationIndex = true;
		reverseIndex = false;
		
		if(mode!=3)
			rank.rankup();
		
		/*
		 * Setup the timer to keep the game from running before the user presses enter
		 * to start it.
		 */
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);
		
		while(true) {
			//Get the time that the frame started.
			long start = System.nanoTime();
			
			//Update the logic timer.
			logicTimer.update();
			
			/*
			 * If a cycle has elapsed on the timer, we can update the game and
			 * move our current piece down.
			 * 
			 * modified by cha seung hoon, for hard drop
			 * 
			 */
			if(logicTimer.hasElapsedCycle() && !beforeVal) {
				updateGame();
				rank.update();
			}
		
			//Decrement the drop cool down if necessary.
			if(dropCooldown > 0) {
				dropCooldown--;
			}


			/**
			 * writer : gowoon-choi
			 * The code for item mode in startGame
			 */
			if(mode == 1){
				if(!rotationIndex){
					if(System.currentTimeMillis() - rotaionTimer >= TIME_LIMIT*1000){
						rotationIndex = true;
					}
				}

				if(scoreIndex){
					if(System.currentTimeMillis() - scoreTimer >= TIME_LIMIT*1000){
						scoreIndex = false;
					}
				}

				if(reverseIndex){
					if(System.currentTimeMillis() - reverseTimer >= TIME_LIMIT*1000){
						reverseIndex = false;
					}
				}
			}

			//Display the window to the user.
			renderGame(this.gamer);

			/*
			 * Sleep to cap the framerate.
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Updates the game and handles the bulk of it's logic.
	 */
	private void updateGame() {
		/*
		 * Check to see if the piece's position can move down to the next row.
		 */

		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			//Increment the current row if it's safe to do so.
			currentRow++;
		} 
		
		else if(addTimer == 0){
			/*
			 * We've either reached the bottom of the board, or landed on another piece, so
			 * we need to add the piece to the board.
			 */
			board.addPiece(currentType, currentCol, currentRow, currentRotation);

			/*
			 * Check to see if adding the new piece resulted in any cleared lines. If so,
			 * increase the player's score. (Up to 4 lines can be cleared in a single go;
			 * [1 = 100pts, 2 = 67pts, 3 = 400pts, 4 = 800pts]).
			 */
			int cleared = board.checkLines();
			if(cleared > 0) {
				player.play_music("t_break.wav", 0);
				if(scoreIndex){
					score += (50 << cleared)*2;
				}
				else{
					score += 50 << cleared;
				}
				if(mode == 3){
					multiPlay.attack(cleared);
				}
			}
			
			/*
			 * Increase the speed slightly for the next piece and update the game's timer
			 * to reflect the increase.
			 */
			if (level < 5) {
				gameSpeed += 0.1f;
			}
			else {
				gameSpeed += 0.035f;
			}
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			
			/*
			 * Set the drop cooldown so the next piece doesn't automatically come flying
			 * in from the heavens immediately after this piece hits if we've not reacted
			 * yet. (~0.5 second buffer).
			 */
			dropCooldown = 25;
			
			/*
			 * Update the difficulty level. This has no effect on the game, and is only
			 * used in the "Level" string in the SidePanel.
			 */
			level = (int)(gameSpeed * 1.70f);

			/*
			 * Spawn a new piece to control.
			 */
			spawnPiece();
		
			switch(mode) {
				/**
				 * the code for item mode in update
				 * writer: choi gowoon
				 * item action
				 */
			case 1:
				itemManager.generateItem();
				itemManager.manageBadItem();

				board.checkLines();
				break;
			
			/**
			 * Updates the game and handles the bulk of it's logic.
			 * fixed by cha seung hoon on 2020.05.19
			 * because of adding interrupt block
			 */
			case 2:
				if(cleared>0)
					makeInterrupt();
				break;
			}
			
			addTimer = 2;
		}
		else {
			addTimer--;
		}
	}
	
	/**
	 * Forces the BoardPanel and SidePanel to repaint.
	 */
	private void renderGame(int gamer) {	//chacha
		if(gamer == 1) {
		d_now = getSize();
		board.resize((d_now.getHeight() / d_start.getHeight()) < (d_now.getWidth() / d_start.getWidth()) ? (d_now.getHeight()/ d_start.getHeight()) : (d_now.getWidth() / d_start.getWidth()));
		int left = (d_now.width - board.getWidth()) / 2;
		int top = ((d_now.height - 67) - board.getHeight()) / 2;
		board.setBounds(left,top,board.getWidth(), board.getHeight());
		board.repaint();
		side.setBounds(left + board.getWidth(), top, side.getWidth(), side.getHeight());
		side.repaint();	
		rank.repaint();
		}
		else {
			d_now = getSize();
			board.resize((d_now.getHeight() / d_start.getHeight()) < (d_now.getWidth() / d_start.getWidth()) ? (d_now.getHeight()/ d_start.getHeight()) : (d_now.getWidth() / d_start.getWidth()));
			int left = (d_now.width - board.getWidth()) / 2;
			int top = ((d_now.height - 67) - board.getHeight()) / 2;
			board.setBounds(left,top,board.getWidth(), board.getHeight());
			board.repaint();
		}
	}

	
	/**
	 * Resets the game variables to their default values at the start
	 * of a new game.
	 */
	private void resetGame() {
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.isNewGame = false;
		this.isGameOver = false;	
		this.tetrisBag.clear();
		this.nextType = TileType.values()[nextTetromino()];
		this.holdType = null;
		this.isHoldable = true;
		itemManager.clear();
		scoreIndex = false;
		rotationIndex = true;
		reverseIndex = false;
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
	
	/**
	 * 2020-04-17 Seungun-Park
	 * tetris bag
	 * The nextType is a Type that has not been selected in the last seven times.
	 */
	private int nextTetromino() {
		/*
		 * ArrayList has elements of Type
		 * select a random element and remove the element
		 */
		if(tetrisBag.isEmpty())
			for(int i = 0; i < TYPE_COUNT; i++)
				tetrisBag.add(i);
		
		int nextIdx = random.nextInt(tetrisBag.size());
		int nextType = tetrisBag.get(nextIdx);
		tetrisBag.remove(nextIdx);
		
		return nextType;
	}
		
	/**
	 * Spawns a new piece and resets our piece's variables to their default
	 * values.
	 */
	private void spawnPiece() {
		/*
		 * Poll the last piece and reset our position and rotation to
		 * their default variables, then pick the next piece to use.
		 */

		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = TileType.values()[nextTetromino()];
		this.isHoldable = true;
		
		/*
		 * If the spawn point is invalid, we need to pause the game and flag that we've lost
		 * because it means that the pieces on the board have gotten too high.
		 */
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			rank.uploadScore();
			rank.rankup(score);
			this.isGameOver = true;
			logicTimer.setPaused(true);
			player.play_music("gameover.wav", 0);
			if(mode == 3){
				multiPlay.finishGame();
				multiPlay.afterFinishGame();
			}
		}		
	}

	/**
	 * Attempts to set the rotation of the current piece to newRotation.
	 * @param newRotation The rotation of the new peice.
	 */
	private void rotatePiece(int newRotation) {
		/*
		 * Sometimes pieces will need to be moved when rotated to avoid clipping
		 * out of the board (the I piece is a good example of this). Here we store
		 * a temporary row and column in case we need to move the tile as well.
		 */
		int newColumn = currentCol;
		int newRow = currentRow;
		
		/*
		 * Get the insets for each of the sides. These are used to determine how
		 * many empty rows or columns there are on a given side.
		 */
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);
		
		/*
		 * If the current piece is too far to the left or right, move the piece away from the edges
		 * so that the piece doesn't clip out of the map and automatically become invalid.
		 */
		if(currentCol < -left) {
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}
		
		/*
		 * If the current piece is too far to the top or bottom, move the piece away from the edges
		 * so that the piece doesn't clip out of the map and automatically become invalid.
		 */
		if(currentRow < -top) {
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}
		
		/*
		 * Check to see if the new position is acceptable. If it is, update the rotation and
		 * position of the piece.
		 */
		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}
	
	/**
	 * Checks to see whether or not the game is paused.
	 * @return Whether or not the game is paused.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Checks to see whether or not the game is over.
	 * @return Whether or not the game is over.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Checks to see whether or not we're on a new game.
	 * @return Whether or not this is a new game.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Gets the current score.
	 * @return The score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Gets the current level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Gets the current type of piece we're using.
	 * @return The piece type.
	 */
	public TileType getPieceType() {
		return currentType;
	}
	
	/**
	 * Gets the next type of piece we're using.
	 * @return The next piece.
	 */
	public TileType getNextPieceType() {
		return nextType;
	}

	/**
	 * writer : github.com/choi-gowoon
	 * 2020.04.26
	 * Gets the hold type of piece we're using.
	 * @return The hold piece.
	 */
	public TileType getHoldPieceType() {
		return holdType;
	}
	
	/**
	 * Gets the column of the current piece.
	 * @return The column.
	 */
	public int getPieceCol() {
		return currentCol;
	}
	
	/**
	 * Gets the row of the current piece.
	 * @return The row.
	 */
	public int getPieceRow() {
		return currentRow;
	}
	
	/**
	 * Gets the rotation of the current piece.
	 * @return The rotation.
	 */
	public int getPieceRotation() {
		return currentRotation;
	}

	/**
	 * writer : choi gowoon
	 * Gets the itemManager
	 * @return The itemManager.
	 */
	public ItemManager getItemManager() { return itemManager; }

	/**
	 * writer : choi gowoon
	 * Sets the scoreIndex
	 */
	public void setScoreIndex(boolean scoreIndex) {
		this.scoreIndex = scoreIndex;
	}

	/**
	 * writer : choi gowoon
	 * Sets the rotationIndex
	 */
	public void setRotationIndex(boolean rotationIndex) {
		this.rotationIndex = rotationIndex;
	}

	/**
	 * writer : choi gowoon
	 * Sets the reverseIndex
	 */
	public void setReverseIndex(boolean reverseIndex) {
		this.reverseIndex = reverseIndex;
	}

	public void setRotaionTimer(long rationTimer) {
		this.rotaionTimer = rationTimer;
	}

	public void setScoreTimer(long scoreTimer) {
		this.scoreTimer = scoreTimer;
	}

	public void setReverseTimer(long reverseTimer) {
		this.reverseTimer = reverseTimer;
	}
	
	public int getMode() {
		return this.mode;
	}

	public void setMode(int mode){ this.mode = mode; }

	public void setMultiPlay(MultiPlay multiPlay){ this.multiPlay = multiPlay; }
	/**
	 * writer : cha seung hoon
	 * 2020.05.19
	 * creating interrupt block
	 */
	public void makeInterrupt() {
		int randCol;
		int randRow;
		int randRot;

		while(true) {
			randCol=5+random.nextInt(10);
			randRow=random.nextInt(10);
			randRot=random.nextInt(1);

			if(board.isValidAndEmpty(currentType, randCol, randRow, randRot))
				break;
		}

		board.addPiece(currentType, randCol, randRow, randRot);
	}
	
	public int getGamer() {	//chacha
		return gamer;
	}
	
	public void setGamer(int num) { //chacha
		this.gamer = num;
	}

	/**
	 * 2020-04-28 Seungun-Park
	 * menu action listener
	 */
	public void actionPerformed(ActionEvent event) {
		/*
		 * if click new game menu
		 * then start new game
		 */
		if(event.getSource() == item_new) {
			resetGame(); // 2020.06.10 cha seung hoon _ complete mew Game button
			isGameOver = true;
			isNewGame = true;
		}
		/*
		 * if click exit menu
		 * then program exit
		 */
		if(event.getSource() == item_exit) {
			System.exit(0);
		}
		/*
		 * if click login menu
		 * then start login with client
		 */
		if(event.getSource() == item_login) {
			if(!client.isLogined()) {
				if(!isGameOver && !isNewGame) {
					isPaused = !isPaused;
					logicTimer.setPaused(isPaused);
				}
				
				if(loginframe == null)
					loginframe = new LoginFrame(this, client);
				//loginframe.setVisible(true);
				//loginframe.setLocation(getLocation().x+getSize().width/2-180, getLocation().y+getSize().height/2-60);
			}
		}
		if(event.getSource() == item_logout) {
			client.logout();
		}
		if(event.getSource() == item_basic) {
			isPaused = false;
			isGameOver = true;
			isNewGame = true;
			mode = 0;
		}
		if(event.getSource() == item_disturb) {
			isPaused = false;
			isGameOver = true;
			isNewGame = true;
			mode = 2;
		}
		if(event.getSource() == item_item) {
			isPaused = false;
			isGameOver = true;
			isNewGame = true;
			mode = 1;
		}
		if(event.getSource()==item_register) { 	// cha seung hoon 2020.06.10 Register Frame
				if(!isGameOver && !isNewGame) {
					isPaused = !isPaused;
					logicTimer.setPaused(isPaused);
				}
			RegisterFrame r = new RegisterFrame(this,client);
		}
		if(event.getSource() == item_basic) {
			isPaused = false;
			isGameOver = true;
			isNewGame = true;
			mode = 0;
		}
		if(event.getSource() == item_disturb) {
			isPaused = false;
			isGameOver = true;
			isNewGame = true;
			mode = 2;
		}
		if(event.getSource() == item_item) {
			isPaused = false;
			isGameOver = true;
			isNewGame = true;
			mode = 1;
		}
		if(event.getSource()==item_multi) {
			if(client.isLogined()) {
				isPaused = false;
				isGameOver = true;
				isNewGame = true;
				mode = 3;
				MultiFrame m = new MultiFrame(this,client);
			}
			else {
				JOptionPane.showMessageDialog(null, "You does not login yet. Please login your account first.");
			}
		}
	}

}
