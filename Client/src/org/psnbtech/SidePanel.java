package org.psnbtech;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The {@code SidePanel} class is responsible for displaying various information
 * on the game such as the next piece, the score and current level, and controls.
 * @author Brendan Jones
 *
 */
public class SidePanel extends JPanel {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 2181495598854992747L;

	/**
	 * The dimensions of each tile on the next piece preview.
	 */
	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1;
	
	/**
	 * The width of the shading on each tile on the next piece preview.
	 */
	private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1;
	
	/**
	 * The number of rows and columns in the preview window. Set to
	 * 5 because we can show any piece with some sort of padding.
	 */
	private static final int TILE_COUNT = 5;
	
	/**
	 * The center x of the next piece preview box.
	 */
	private static final int SQUARE_CENTER_X = 70;
	
	/**
	 * The center y of the next piece preview box.
	 */
	private static final int SQUARE_CENTER_Y = 65;
	
	/**
	 * The size of the next piece preview box.
	 */
	private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);

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
	 * The y coordinate of the stats category.
	 */
	private static final int STATS_INSET = 120;
	
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
	
	/**
	 * The Tetris instance.
	 */
	private Tetris tetris;
	
	/**
	 * 2020-04-22 Seungun-Park
	 * panel resize
	 */
	private static Dimension d_start;
	
	/**
	 * Creates a new SidePanel and sets it's display properties.
	 * @param tetris The Tetris instance to use.
	 */
	public SidePanel(Tetris tetris) {
		this.tetris = tetris;
		
		d_start = new Dimension(200, BoardPanel.PANEL_HEIGHT);
		setSize(d_start);
		//setPreferredSize(d_start);
		setBackground(Color.BLACK);
	}
	
	/**
	 * 2020-04-22 Seungun-Park
	 * panel resize
	 */
	public Dimension resize() {
		return d_start;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Set the color for drawing.
		g.setColor(DRAW_COLOR);
		
		/*
		 * This variable stores the current y coordinate of the string.
		 * This way we can re-order, add, or remove new strings if necessary
		 * without needing to change the other strings.
		 */
		int offset;
		
		/*
		 * Draw the "Stats" category.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Stats", SMALL_INSET, offset = STATS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("Level: " + tetris.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Score: " + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
		
		/*
		 * Draw the "Controls" category.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Controls", SMALL_INSET, offset = CONTROLS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("← - Move Left", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("→ - Move Right", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("↓ - Drop", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Z - Rotate Anticlockwise", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("X - Rotate Clockwise", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Shift - Hold", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Space - Hard Drop", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("P - Pause Game", LARGE_INSET, offset += TEXT_STRIDE);
		
		/*
		 * Draw the next piece preview box.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("NEXT", SQUARE_CENTER_X - ( SQUARE_SIZE + SMALLEST_INSET ), SQUARE_CENTER_Y - ( SQUARE_SIZE + SMALLEST_INSET ));
		g.drawRect(SQUARE_CENTER_X - ( SQUARE_SIZE + SMALLEST_INSET ) , SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);

		/*
		 * Draw the hold piece box
		 */
		g.setFont(LARGE_FONT);
		g.drawString("HOLD", SQUARE_CENTER_X + ( SQUARE_SIZE + SMALLEST_INSET ), SQUARE_CENTER_Y - ( SQUARE_SIZE + SMALLEST_INSET ));
		g.drawRect(SQUARE_CENTER_X + ( SQUARE_SIZE + SMALLEST_INSET ), SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		/*
		 * Draw a preview of the next piece that will be spawned. The code is pretty much
		 * identical to the drawing code on the board, just smaller and centered, rather
		 * than constrained to a grid.
		 */


		TileType nextType = tetris.getNextPieceType();
		if(!tetris.isGameOver() && nextType != null) {
			/*
			 * Get the size properties of the current piece.
			 */
			int cols = nextType.getCols();
			int rows = nextType.getRows();
			int dimension = nextType.getDimension();
		
			/*
			 * Calculate the top left corner (origin) of the piece.
			 */
			int startX = (SQUARE_CENTER_X - SMALLEST_INSET - (cols * TILE_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));
		
			/*
			 * Get the insets for the preview. The default
			 * rotation is used for the preview, so we just use 0.
			 */
			int top = nextType.getTopInset(0);
			int left = nextType.getLeftInset(0);
		
			/*
			 * Loop through the piece and draw it's tiles onto the preview.
			 */
			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(nextType.isTile(col, row, 0) == 1) {
						drawNextTile(nextType, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}

		/*
		 * writer : github.com/choi-gowoon
		 * 2020.04.26
		 * Draw a preview of the hold piece that will be spawned. The code is pretty much
		 * identical to the drawing code on the board, just smaller and centered, rather
		 * than constrained to a grid.
		 */


		TileType holdType = tetris.getHoldPieceType();
		if(!tetris.isGameOver() && holdType != null) {
			/*
			 * Get the size properties of the current piece.
			 */
			int cols = holdType.getCols();
			int rows = holdType.getRows();
			int dimension = holdType.getDimension();

			/*
			 * Calculate the top left corner (origin) of the piece.
			 */
			int startX = (SQUARE_CENTER_X + 5 + SQUARE_SIZE + (cols * TILE_SIZE / 2));
			// TODO : GoWoon - 5�씪�뒗 �닽�옄濡� �븯�뱶�븯寃� 議곗젙�빐���뒗�뜲 諛붽씀湲� !
			int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));

			/*
			 * Get the insets for the preview. The default
			 * rotation is used for the preview, so we just use 0.
			 */
			int top = holdType.getTopInset(0);
			int left = holdType.getLeftInset(0);

			/*
			 * Loop through the piece and draw it's tiles onto the preview.
			 */
			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(holdType.isTile(col, row, 0) == 1) {
						drawHoldTile(holdType, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
	}


	/*
	 * writer : github.com/choi-gowoon
	 * 2020.04.26
	 * Draws a tile onto the hold window.
	 * @param holdType The holdType of tile to draw.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param g The graphics object.
	 */
	private void drawHoldTile(TileType holdType, int x, int y, Graphics g) {
		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(holdType.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

		/*
		 * Fill the bottom and right edges of the tile with the dark shading color.
		 */
		g.setColor(holdType.getDarkColor());
		g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);

		/*
		 * Fill the top and left edges with the light shading. We draw a single line
		 * for each row or column rather than a rectangle so that we can draw a nice
		 * looking diagonal where the light and dark shading meet.
		 */
		g.setColor(holdType.getLightColor());
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
		}
	}

	/*
	 * Draws a tile onto the preview window.
	 * @param nextType The nextType of tile to draw.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param g The graphics object.
	 */
	private void drawNextTile(TileType nextType, int x, int y, Graphics g) {
		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(nextType.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

		/*
		 * Fill the bottom and right edges of the tile with the dark shading color.
		 */
		g.setColor(nextType.getDarkColor());
		g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);

		/*
		 * Fill the top and left edges with the light shading. We draw a single line
		 * for each row or column rather than a rectangle so that we can draw a nice
		 * looking diagonal where the light and dark shading meet.
		 */
		g.setColor(nextType.getLightColor());
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
		}
	}
	
	public Dimension getDim() {
		return d_start;
	}
}
