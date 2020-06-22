package org.psnbtech;

import hoon2woon2.Items.ItemManager;
import hoon2woon2.MultiPlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * The {@code BoardPanel} class is responsible for displaying the game grid and
 * handling things related to the game board.
 * @author Brendan Jones
 *
 */
public class BoardPanel extends JPanel {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 5055679736784226108L;

	/**
	 * Minimum color component values for tiles. This is required if we
	 * want to show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MIN = 35;

	/**
	 * Maximum color component values for tiles. This is required if we
	 * want to show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MAX = 255 - COLOR_MIN;

	/**
	 * The width of the border around the game board.
	 */
	private static final int BORDER_WIDTH = 5;

	/**
	 * The number of columns on the board.
	 */
	public static final int COL_COUNT = 10;

	/**
	 * The number of visible rows on the board.
	 */
	public static final int VISIBLE_ROW_COUNT = 20;

	/**
	 * The number of rows that are hidden from view.
	 */
	private static final int HIDDEN_ROW_COUNT = 2;

	/**
	 * The total number of rows that the board contains.
	 */
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;

	/**
	 * The number of pixels that a tile takes up.
	 */
	public static int TILE_SIZE = 24;

	/**
	 * The width of the shading on the tiles.
	 */
	public static int SHADE_WIDTH = TILE_SIZE / 6;

	/**
	 * The central x coordinate on the game board.
	 */
	private static int CENTER_X = COL_COUNT * TILE_SIZE / 2;

	/**
	 * The central y coordinate on the game board.
	 */
	private static int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;

	/**
	 * The total width of the panel.
	 */
	public static int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;

	/**
	 * The total height of the panel.
	 */
	public static int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;

	/**
	 * The larger font to display.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	/**
	 * The smaller font to display.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

	/**
	 * The Tetris instance.
	 */
	private Tetris tetris;

	private ItemManager itemManager;

	/**
	 * gowoon -choi
	 * TODO add comment
	 */
	private int lineInOnce = 0;

	/**
	 * The tiles that make up the board.
	 */
	private TileType[][] tiles;

	/**
	 * 2020-04-22 Seungun-Park
	 * panel resize
	 */
	private static Dimension d_start;
	private static Dimension d_now;

	private static MultiPlay m;
	private static String userId;

	TileType type;
	int pieceRow;
	int pieceCol;
	int rotation;

	/**
	 * Crates a new GameBoard instance.
	 * @param tetris The Tetris instance to use.
	 */
	public BoardPanel(Tetris tetris) {
		this.tetris = tetris;
		this.tiles = new TileType[ROW_COUNT][COL_COUNT];

		d_start = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		setSize(d_start);
		//setPreferredSize(d_start);
		setBackground(Color.BLACK);
	}

	/**
	 * 2020-04-22 Seungun-Park
	 * panel resize
	 */
	public Dimension resize(double ratio) {
		TILE_SIZE = (int)(24.0 * ratio);
		SHADE_WIDTH = TILE_SIZE / 6;
		CENTER_X = COL_COUNT * TILE_SIZE / 2;
		CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
		PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
		setSize(PANEL_WIDTH, PANEL_HEIGHT);
		return d_now;
	}

	/**
	 * Resets the board and clears away any tiles.
	 */
	public void clear() {
		/*
		 * Loop through every tile index and set it's value
		 * to null to clear the board.
		 */
		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
			}
		}
	}

	/**
	 * Determines whether or not a piece can be placed at the coordinates.
	 * @param type THe type of piece to use.
	 * @param x The x coordinate of the piece.
	 * @param y The y coordinate of the piece.
	 * @param rotation The rotation of the piece.
	 * @return Whether or not the position is valid.
	 */
	public boolean isValidAndEmpty(TileType type, int x, int y, int rotation) {

		//Ensure the piece is in a valid column.
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}

		//Ensure the piece is in a valid row.
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}

		/*
		 * Loop through every tile in the piece and see if it conflicts with an existing tile.
		 *
		 * Note: It's fine to do this even though it allows for wrapping because we've already
		 * checked to make sure the piece is in a valid location.
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) == 1 && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Adds a piece to the game board. Note: Doesn't check for existing pieces,
	 * and will overwrite them if they exist.
	 * @param type The type of piece to place.
	 * @param x The x coordinate of the piece.
	 * @param y The y coordinate of the piece.
	 * @param rotation The rotation of the piece.
	 */
	public void addPiece(TileType type, int x, int y, int rotation) {
		/*
		 * Loop through every tile within the piece and add it
		 * to the board only if the boolean that represents that
		 * tile is set to true.
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) == 1) {
					setTile(col + x, row + y, type);
				}
			}
		}
	}

	/**
	 * Checks the board to see if any lines have been cleared, and
	 * removes them from the game.
	 * @return The number of lines that were cleared.
	 */
	public int checkLines() {
		int completedLines = 0;

		/*
		 * Here we loop through every line and check it to see if
		 * it's been cleared or not. If it has, we increment the
		 * number of completed lines and check the next row.
		 *
		 * The checkLine function handles clearing the line and
		 * shifting the rest of the board down for us.
		 */
		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}

	/**
	 * Checks whether or not {@code row} is full.
	 * @param line The row to check.
	 * @return Whether or not this row is full.
	 */
	private boolean checkLine(int line) {
		/*
		 * Iterate through every column in this row. If any of them are
		 * empty, then the row is not full.
		 */
		for(int col = 0; col < COL_COUNT; col++) {
			if(tiles[line][col] == TileType.UnTypeX){
				return false;
			}
			if(!isOccupied(col, line)) {
				return false;
			}
		}

		/*
		 * Since the line is filled, we need to 'remove' it from the game.
		 * To do this, we simply shift every row above it down by one.
		 */

		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTile(col, row + 1, getTile(col, row));
			}
		}

		if(tetris.getMode() == 2){
			if(tiles[line][0] != TileType.TypeX ){
				itemManager.actionItem(line);

				for(int i=0; i<itemManager.getItems().size(); i++){
					if(itemManager.getItems().get(i).getY() < line){
						itemManager.getItems().get(i).setY(itemManager.getItems().get(i).getY()+1);
					}
				}
			}
			else{
				itemManager.deleteItem(line);
			}
		}
		return true;
	}


	/**
	 * Checks to see if the tile is already occupied.
	 * @param x The x coordinate to check.
	 * @param y The y coordinate to check.
	 * @return Whether or not the tile is occupied.
	 */
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;
	}

	/**
	 * Sets a tile located at the desired column and row.
	 * @param x The column.
	 * @param y The row.
	 * @param
	 *  type The value to set to the tile to.
	 */
	public void setTile(int  x, int y, TileType type) {
		tiles[y][x] = type;
	}

	/**
	 * Gets a tile by it's column and row.
	 * @param x The column.
	 * @param y The row.
	 * @return The tile.
	 */
	public TileType getTile(int x, int y) {
		return tiles[y][x];
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//This helps simplify the positioning of things.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);

		/*
		 * Draw the board differently depending on the current game state.
		 */

		if(tetris.getMode()!=3) {
			if(tetris.isPaused()) {
				g.setFont(LARGE_FONT);
				g.setColor(Color.WHITE);
				String msg = "PAUSED";
				g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
			} else if(tetris.isNewGame() || tetris.isGameOver()) {
				g.setFont(LARGE_FONT);
				g.setColor(Color.WHITE);

				/*
				 * Because both the game over and new game screens are nearly identical,
				 * we can handle them together and just use a ternary operator to change
				 * the messages that are displayed.
				 */
				String msg = tetris.isNewGame() ? "TETRIS" : "GAME OVER";
				if(tetris.getMode()==2)
				{
					if(tetris.isGameOver()) itemManager.clear();
				}
				g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
				g.setFont(SMALL_FONT);
				msg = "Press Enter to Play" + (tetris.isNewGame() ? "" : " Again");
				g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
			}

			else {

				/*
				 * Draw the tiles onto the board.
				 */
				for(int x = 0; x < COL_COUNT; x++) {
					for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
						TileType tile = getTile(x, y);
						if(tile != null) {
							drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}


				/*
				 * Draw the current piece. This cannot be drawn like the rest of the
				 * pieces because it's still not part of the game board. If it were
				 * part of the board, it would need to be removed every frame which
				 * would just be slow and confusing.
				 */

				TileType type = tetris.getPieceType();
				int pieceCol = tetris.getPieceCol();
				int pieceRow = tetris.getPieceRow();
				int rotation = tetris.getPieceRotation();

				//Draw the piece onto the board.
				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(pieceRow + row >= 2 && type.isTile(col, row, rotation) == 1) {
							drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}

				/*
				 * Draw the ghost (semi-transparent piece that shows where the current piece will land). I couldn't think of
				 * a better way to implement this so it'll have to do for now. We simply take the current position and move
				 * down until we hit a row that would cause a collision.
				 */
				Color base = type.getBaseColor();
				base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
				for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
					//If no collision is detected, try the next row.
					if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {
						continue;
					}

					//Draw the ghost one row higher than the one the collision took place at.
					lowest--;

					//Draw the ghost piece.
					for(int col = 0; col < type.getDimension(); col++) {
						for(int row = 0; row < type.getDimension(); row++) {
							if(lowest + row >= 2 && type.isTile(col, row, rotation) == 1) {
								drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
							}
						}
					}

					break;
				}

				/*
				 * Draw the background grid above the pieces (serves as a useful visual
				 * for players, and makes the pieces look nicer by breaking them up.
				 */
				g.setColor(Color.DARK_GRAY);
				for(int x = 0; x < COL_COUNT; x++) {
					for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
						g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
						g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
					}
				}

				itemManager = tetris.getItemManager();
				for(int i=0; i<itemManager.getItems().size(); i++){
					drawItem(itemManager.getItems().get(i).getX()*TILE_SIZE + TILE_SIZE/4,(itemManager.getItems().get(i).getY()-HIDDEN_ROW_COUNT)*TILE_SIZE + TILE_SIZE/2,itemManager.getItems().get(i).getItemIndex(),g);

				}
			}

			/*
			 * Draw the outline.
			 */
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
		}
		else {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);

			/*
			 * Because both the game over and new game screens are nearly identical,
			 * we can handle them together and just use a ternary operator to change
			 * the messages that are displayed.
			 */
			String msg;

			if(tetris.flag==0)
				msg = (boolean) tetris.isNewGame() ? "TETRIS" : "GAME OVER";
			else
				msg = (boolean) m.getStatus().get(userId) ? "TETRIS" : "GAME OVER";

			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Press Enter to Play" + (tetris.isNewGame() ? "" : " Again");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);

			/*
			 * Draw the tiles onto the board.
			 */
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					TileType tile = getTile(x, y);
					if(tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}


			/*
			 * Draw the current piece. This cannot be drawn like the rest of the
			 * pieces because it's still not part of the game board. If it were
			 * part of the board, it would need to be removed every frame which
			 * would just be slow and confusing.
			 */

			/*TileType type = tetris.getPieceType();
			int pieceCol = tetris.getPieceCol();
			int pieceRow = tetris.getPieceRow();
			int rotation = tetris.getPieceRotation();*/

			//Draw the piece onto the board.
			try {
				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(pieceRow + row >= 2 && type.isTile(col, row, rotation) == 1) {
							drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
			}catch(Exception e) {}
			/*
			 * Draw the ghost (semi-transparent piece that shows where the current piece will land). I couldn't think of
			 * a better way to implement this so it'll have to do for now. We simply take the current position and move
			 * down until we hit a row that would cause a collision.
			 */
			try {
				Color base = type.getBaseColor();
				base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
				for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
					//If no collision is detected, try the next row.
					if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {
						continue;
					}

					//Draw the ghost one row higher than the one the collision took place at.
					lowest--;

					//Draw the ghost piece.
					for(int col = 0; col < type.getDimension(); col++) {
						for(int row = 0; row < type.getDimension(); row++) {
							if(lowest + row >= 2 && type.isTile(col, row, rotation) == 1) {
								drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
							}
						}
					}

					break;
				}
			}catch(Exception e) {}
			/*
			 * Draw the background grid above the pieces (serves as a useful visual
			 * for players, and makes the pieces look nicer by breaking them up.
			 */
			g.setColor(Color.DARK_GRAY);
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}

			if(tetris.getMode()!= 3) {
				itemManager = tetris.getItemManager();
				for(int i=0; i<itemManager.getItems().size(); i++){
					drawItem(itemManager.getItems().get(i).getX()*TILE_SIZE + TILE_SIZE/4,(itemManager.getItems().get(i).getY()-HIDDEN_ROW_COUNT)*TILE_SIZE + TILE_SIZE/2,itemManager.getItems().get(i).getItemIndex(),g);
				}
			}
		}

		/*
		 * Draw the outline.
		 */
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);

	}

	/**
	 * Draws a tile onto the board.
	 * @param type The type of tile to draw.
	 * @param x The column.
	 * @param y The row.
	 * @param g The graphics object.
	 */
	private void drawTile(TileType type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
	}

	/**
	 * Draws a tile onto the board.
	 * @param base The base color of tile.
	 * @param light The light color of the tile.
	 * @param dark The dark color of the tile.
	 * @param x The column.
	 * @param y The row.
	 * @param g The graphics object.
	 */
	private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {

		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(base);
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

		/*
		 * Fill the bottom and right edges of the tile with the dark shading color.
		 */
		g.setColor(dark);
		g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);

		/*
		 * Fill the top and left edges with the light shading. We draw a single line
		 * for each row or column rather than a rectangle so that we can draw a nice
		 * looking diagonal where the light and dark shading meet.
		 */
		g.setColor(light);
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
		}
	}


	//TODO comment
	public void drawItem(int x, int y, int num, Graphics g){
		g.setColor(Color.white);
		g.drawString(Integer.toString(num),x, y);
	}

	public void addUnTypeX(){
		for(int row = 1; row < ROW_COUNT; row++) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTile(col, row - 1, getTile(col, row));
			}
		}

		itemManager.replaceItem(1);

		for(int col=0; col<COL_COUNT; col++){
			tiles[ROW_COUNT-1][col] = TileType.values()[7];
		}
	}

	public void removeUnTypeX(){
		int row;
		for(row = ROW_COUNT-1; row >= 0; row--){
			if(getTile(0,row) != TileType.UnTypeX) break;
		}
		if(row != ROW_COUNT - 1){
			for(int col=0; col<COL_COUNT; col++){
				tiles[row+1][col] = TileType.values()[8];
			}
		}
	}

	public void removeLine(){
		int row;
		for(row = ROW_COUNT-1; row >= 0; row--){
			if(getTile(0,row) != TileType.UnTypeX) break;
		}

		for(int col=0; col<COL_COUNT; col++){
			tiles[row][col] = TileType.values()[8];
		}
	}

	public TileType[][] getTiles(){ return this.tiles; }


	public Dimension getDim() {
		return d_start;
	}

	public void setMultiplay(MultiPlay m) {
		this.m = m;
	}

	public void setUserId(String str) {
		this.userId = str;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public void setPieceCol(int pieceCol) {
		this.pieceCol = pieceCol;
	}

	public void setPieceRow(int pieceRow) {
		this.pieceRow = pieceRow;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
}
