package test;

import java.util.Arrays;

public class Word {
	Tile[] tiles;
	int row, col;
	boolean vertical; //true - Word written up to down
	
	public Word(Tile[] tiles, int row, int col, boolean vertical) {
		super();
		this.tiles = tiles;
		this.row = row;
		this.col = col;
		this.vertical = vertical;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[] tiles) {
		this.tiles = tiles;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		//this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean isVertical() {
		return vertical;
	}
	
	public void setVertical(boolean vertical) {
			this.vertical = vertical;
		}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		return col == other.col && row == other.row && Arrays.equals(tiles, other.tiles) && vertical == other.vertical;
	}
}
