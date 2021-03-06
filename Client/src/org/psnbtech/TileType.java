package org.psnbtech;

import java.awt.Color;

/**
 * The {@code PieceType} enum describes the properties of the various pieces that can be used in the game.
 * @author Brendan Jones
 *
 */
public enum TileType {

	/**
	 * Piece TypeI.
	 */
	TypeI(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX), 4, 4, 1, new int[][] {
		{
			0,	0,	0,	0,
			1,	1,	1,	1,
			0,	0,	0,	0,
			0,	0,	0,	0,
		},
		{
			0,	0,	1,	0,
			0,	0,	1,	0,
			0,	0,	1,	0,
			0,	0,	1,	0,
		},
		{
			0,	0,	0,	0,
			0,	0,	0,	0,
			1,	1,	1,	1,
			0,	0,	0,	0,
		},
		{
			0,	1,	0,	0,
			0,	1,	0,	0,
			0,	1,	0,	0,
			0,	1,	0,	0,
		}
	}),
	
	/**
	 * Piece TypeJ.
	 */
	TypeJ(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX), 3, 3, 2, new int[][] {
		{
			1,	0,	0,
			1,	1,	1,
			0,	0,	0,
		},
		{
			0,	1,	1,
			0,	1,	0,
			0,	1,	0,
		},
		{
			0,	0,	0,
			1,	1,	1,
			0,	0,	1,
		},
		{
			0,	1,	0,
			0,	1,	0,
			1,	1,	0,
		}
	}),
	
	/**
	 * Piece TypeL.
	 */
	TypeL(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), 3, 3, 2, new int[][] {
		{
			0,	0,	1,
			1,	1,	1,
			0,	0,	0,
		},
		{
			0,	1,	0,
			0,	1,	0,
			0,	1,	1,
		},
		{
			0,	0,	0,
			1,	1,	1,
			1,	0,	0,
		},
		{
			1,	1,	0,
			0,	1,	0,
			0,	1,	0,
		}
	}),
	
	/**

	 *
	 * Piece TypeO.
	 */
	TypeO(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 2, 2, 2, new int[][] {
		{
			1,	1,
			1,	1,
		},
		{
			1,	1,
			1,	1,
		},
		{	
			1,	1,
			1,	1,
		},
		{
			1,	1,
			1,	1,
		}
	}),
	
	/**
	 * Piece TypeS.
	 */
	TypeS(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 3, 3, 2, new int[][] {
		{
			0,	1,	1,
			1,	1,	0,
			0,	0,	0,
		},
		{
			0,	1,	0,
			0,	1,	1,
			0,	0,	1,
		},
		{
			0,	0,	0,
			0,	1,	1,
			1,	1,	0,
		},
		{
			1,	0,	0,
			1,	1,	0,
			0,	1,	0,
		}
	}),
	
	/**
	 * Piece TypeT.
	 */
	TypeT(new Color(128, BoardPanel.COLOR_MIN, 128), 3, 3, 2, new int[][] {
		{
			0,	1,	0,
			1,	1,	1,
			0,	0,	0,
		},
		{
			0,	1,	0,
			0,	1,	1,
			0,	1,	0,
		},
		{
			0,	0,	0,
			1,	1,	1,
			0,	1,	0,
		},
		{
			0,	1,	0,
			1,	1,	0,
			0,	1,	0,
		}
	}),
	
	/**
	 * Piece TypeZ.
	 */
	TypeZ(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN), 3, 3, 2, new int[][] {
		{
			1,	1,	0,
			0,	1,	1,
			0,	0,	0,
		},
		{
			0,	0,	1,
			0,	1,	1,
			0,	1,	0,
		},
		{
			0,	0,	0,
			1,	1,	0,
			0,	1,	1,
		},
		{
			0,	1,	0,
			1,	1,	0,
			1,	0,	0,
		}
	}),

	UnTypeX(new Color(116, 116, 116), 1, 1, 1, new int[][] {
			{
					-1
			},
			{
					-1
			},
			{
					-1
			},
			{
					-1
			}
	}),

	TypeX(new Color(116, 116, 116), 1, 1, 1, new int[][] {
			{
					1
			},
			{
					1
			},
			{
					1
			},
			{
					1
			}
	})
	;
		
	/**
	 * The base color of tiles of this type.
	 */
	private Color baseColor;
	
	/**
	 * The light shading color of tiles of this type.
	 */
	private Color lightColor;
	
	/**
	 * The dark shading color of tiles of this type.
	 */
	private Color darkColor;
	
	/**
	 * The column that this type spawns in.
	 */
	private int spawnCol;
	
	/**
	 * The row that this type spawns in.
	 */
	private int spawnRow;
	
	/**
	 * The dimensions of the array for this piece.
	 */
	private int dimension=0;
	
	/**
	 * The number of rows in this piece. (Only valid when rotation is 0 or 2,
	 * but it's fine since we're only using it for displaying the next piece
	 * preview, which uses rotation 0).
	 */
	private int rows;
	
	/**
	 * The number of columns in this piece. (Only valid when rotation is 0 or 2,
	 * but it's fine since we're only using it for displaying the next piece
	 * preview, which uses rotation 0).
	 */
	private int cols;
	
	/**
	 * The tiles for this piece. Each piece has an array of tiles for each rotation.
	 */
	private int[][] tiles;
	
	/**
	 * Creates a new TileType.
	 * @param color The base color of the tile.
	 * @param dimension The dimensions of the tiles array.
	 * @param cols The number of columns.
	 * @param rows The number of rows.
	 * @param tiles The tiles.
	 */
	private TileType(Color color, int dimension, int cols, int rows, int[][] tiles) {
		this.baseColor = color;
		this.lightColor = color.brighter();
		this.darkColor = color.darker();
		this.dimension = dimension;
		this.tiles = tiles;
		this.cols = cols;
		this.rows = rows;
		
		this.spawnCol = 3;
		this.spawnRow = getTopInset(0);
	}
	
	/**
	 * Gets the base color of this type.
	 * @return The base color.
	 */
	public Color getBaseColor() {
		return baseColor;
	}
	
	/**
	 * Gets the light shading color of this type.
	 * @return The light color.
	 */
	public Color getLightColor() {
		return lightColor;
	}
	
	/**
	 * Gets the dark shading color of this type.
	 * @return The dark color.
	 */
	public Color getDarkColor() {
		return darkColor;
	}
	
	/**
	 * Gets the dimension of this type.
	 * @return The dimension.
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Gets the spawn column of this type.
	 * @return The spawn column.
	 */
	public int getSpawnColumn() {
		return spawnCol;
	}
	
	/**
	 * Gets the spawn row of this type.
	 * @return The spawn row.
	 */
	public int getSpawnRow() {
		return spawnRow;
	}
	
	/**
	 * Gets the number of rows in this piece. (Only valid when rotation is 0 or 2,
	 * but it's fine since this is only used for the preview which uses rotation 0).
	 * @return The number of rows.
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Gets the number of columns in this piece. (Only valid when rotation is 0 or 2,
	 * but it's fine since this is only used for the preview which uses rotation 0).
	 * @return The number of columns.
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * Checks to see if the given coordinates and rotation contain a tile.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param rotation The rotation to check in.
	 * @return Whether or not a tile resides there.
	 */
	public int isTile(int x, int y, int rotation) {
		return tiles[rotation][y * dimension + x];
	}
	
	/**
	 * The left inset is represented by the number of empty columns on the left
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The left inset.
	 */
	public int getLeftInset(int rotation) {
		/*
		 * Loop through from left to right until we find a tile then return
		 * the column.
		 */
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation) == 1|| isTile(x, y, rotation) == -1) {
					return x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The right inset is represented by the number of empty columns on the left
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The right inset.
	 */
	public int getRightInset(int rotation) {
		/*
		 * Loop through from right to left until we find a tile then return
		 * the column.
		 */
		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation) == 1|| isTile(x, y, rotation) == -1) {
					return dimension - x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The left inset is represented by the number of empty rows on the top
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The top inset.
	 */
	public int getTopInset(int rotation) {
		/*
		 * Loop through from top to bottom until we find a tile then return
		 * the row.
		 */
		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation) == 1|| isTile(x, y, rotation) == -1) {
					return y;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The botom inset is represented by the number of empty rows on the bottom
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The bottom inset.
	 */
	public int getBottomInset(int rotation) {
		/*
		 * Loop through from bottom to top until we find a tile then return
		 * the row.
		 */
		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation) == 1 || isTile(x, y, rotation) == -1) {
					return dimension - y;
				}
			}
		}
		return -1;
	}


	/**
	 * valueof 안되면 사용.. 되면 지우기 !
	 * gowoon-choi
	 * TODO ! comment
	 */
	public TileType string2Tile(String str){
		TileType tileType;
		switch (str){
			case "TypeI":
				tileType = TypeI;
				break;
			case "TypeJ":
				tileType = TypeJ;
				break;
			case "TypeL":
				tileType = TypeL;
				break;
			case "TypeO":
				tileType = TypeO;
				break;
			case "TypeS":
				tileType = TypeS;
				break;
			case "TypeT":
				tileType = TypeT;
				break;
			case "TypeZ":
				tileType = TypeZ;
				break;
			default :
				tileType = null;
				break;
		}
		return tileType;
	}
	
}
