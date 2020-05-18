package org.psnbtech;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.cert.TrustAnchor;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.util.ArrayList;

import hoon2woon2.Client;
import hoon2woon2.LoginFrame;
import hoon2woon2.RankPanel;


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
	 * The number of pieces that exist.
	 */
	private static final int TYPE_COUNT = TileType.values().length;
		
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

	/*
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
	
	/** 2020-04-28 Seungun-Park
	 */
	//program menu
	JMenuBar menu = new JMenuBar();
	JMenu mn_file = new JMenu("File");
	JMenuItem item_new = new JMenuItem("NewGame");
	JMenuItem item_exit = new JMenuItem("Exit");
	JMenu mn_account = new JMenu("Account");
	JMenuItem item_login = new JMenuItem("Login");
	JMenuItem item_logout = new JMenuItem("Logout");
	//socket program
	private Client client;
	private static int user = -1;
	private static String userid = "";

	/*
	 * writer: cha seung hoon
	 * hard drop
	 * 2020.04.28
	 * */
	private boolean isHardDrop = false;
	private boolean beforeVal = false;
	
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
		
		client = c;
		
		/*
		 * Initialize the BoardPanel and SidePanel instances.
		 */
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		this.rank = new RankPanel(this);
		this.tetrisBag = new ArrayList<Integer>();
		
		/**2020-04-28 Seungun-Park
		 * Menu control
		 */
		item_new.addActionListener(this);
		item_exit.addActionListener(this);
		item_login.addActionListener(this);
		item_logout.addActionListener(this);
		
		mn_file.add(item_new);
		mn_file.add(item_exit);
		mn_account.add(item_login);
		mn_account.add(item_logout);
		
		menu.add(mn_file);
		menu.add(mn_account);
		
		setJMenuBar(menu);
		
		/*
		 * Add the BoardPanel and SidePanel instances to the window.
		 */
		add(rank);
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
					}
					break;
					
				/*
				 * Move Left - When pressed, we check to see that the game is
				 * not paused and that the position to the left of the current
				 * position is valid. If so, we decrement the current column by 1.
				 */
				case KeyEvent.VK_LEFT:		
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)&&!beforeVal) {
						currentCol--;
					}
					
					
					break;
					
				/*
				 * Move Right - When pressed, we check to see that the game is
				 * not paused and that the position to the right of the current
				 * position is valid. If so, we increment the current column by 1.
				 */
				case KeyEvent.VK_RIGHT:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)&&!beforeVal) {
						currentCol++;
					}
					break;
					
				/*
				 * Rotate Anticlockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece anticlockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to clockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
				case KeyEvent.VK_Z:
					if(!isPaused) {
						rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
					}
					break;
				
				/*
			     * Rotate Clockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece clockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to anticlockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
				case KeyEvent.VK_X:
					if(!isPaused) {
						rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
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
				case KeyEvent.VK_C:
					if(!isPaused && isHoldable) {
						TileType temp = currentType;
						if(holdType == null){
							currentType = getNextPieceType();
							nextType = TileType.values()[nextTetromino()];
						}
						else{
							currentType = holdType;
						}
						holdType = temp;
						isHoldable = false;
					}
					break;
				
				/*
				 * writer : cha seung hoon
				 * 2020. 04 .28
				 * Hard Drop
				 */
				case KeyEvent.VK_SPACE:
					isHardDrop=true;
					int cnt=0;
					while(board.isValidAndEmpty(currentType, currentCol, currentRow+cnt, currentRotation)) {
						cnt++;
					}
					currentRow+=cnt-1;
					updateGame();
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
			
			//Display the window to the user.
			renderGame();
			
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
		
		else {
			/*
			 * We've either reached the bottom of the board, or landed on another piece, so
			 * we need to add the piece to the board.
			 */
			board.addPiece(currentType, currentCol, currentRow, currentRotation);
			
			/*
			 * Check to see if adding the new piece resulted in any cleared lines. If so,
			 * increase the player's score. (Up to 4 lines can be cleared in a single go;
			 * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]).
			 */
			int cleared = board.checkLines();
			if(cleared > 0) {
				score += 50 << cleared;
			}
			
			/*
			 * Increase the speed slightly for the next piece and update the game's timer
			 * to reflect the increase.
			 */
			gameSpeed += 0.035f;
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
			
		}		
	}
	
	/**
	 * Forces the BoardPanel and SidePanel to repaint.
	 */
	private void renderGame() {
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
			//rank.uploadScore();
			this.isGameOver = true;
			logicTimer.setPaused(true);
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

	/*
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
	 * 2020-04-28 Seungun-Park
	 * menu action listener
	 */
	public void actionPerformed(ActionEvent event) {
		/*
		 * if click new game menu
		 * then start new game
		 */
		if(event.getSource() == item_new) {

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
			if(user == -1) {
				if(!isGameOver && !isNewGame) {
					isPaused = !isPaused;
					logicTimer.setPaused(isPaused);
				}
				LoginFrame l = new LoginFrame(this, client);
			}
		}
		if(event.getSource() == item_logout) {
			if(user != -1) {
				user = -1;
				userid = "";
			}
		}
	}
}