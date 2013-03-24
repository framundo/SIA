package model;

import gps.api.GPSState;
import gps.exception.NotAppliableException;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements GPSState, Cloneable {
	
	static final int EMPTY = 0;
	private static final int[][] HASH_MATRIX = {{2,3,5,7,11,13,17,19,23,29},
		{31,37,41,43,47,53,59,61,67,71},
		{73,79,83,89,97,101,103,107,109,113},
		{127,131,137,139,149,151,157,163,167,173},
		{179,181,191,193,197,199,211,223,227,229},
		{233,239,241,251,257,263,269,271,277,281},
		{283,293,307,311,313,317,331,337,347,349},
		{353,359,367,373,379,383,389,397,401,409},
		{419,421,431,433,439,443,449,457,461,463},
		{467,479,487,491,499,503,509,521,523,541}};
	
	
	private int rows;
	private int cols;
	private int maxColors;

	private int[][] tiles;
	private int[] colorQty;
	private int movements;

	private Board(int tiles[][], int[] colorQty, int colors, int movements) {
		this.tiles = tiles;
		this.colorQty = colorQty;
		rows = tiles.length;
		cols = tiles[0].length;
		this.maxColors = colors;
		this.movements = movements;
	}
	
	public Board(int[][] tiles, int colors){
		this(tiles, new int[colors],colors, 0);
		calculateColorQty();
	}
	
	public Board(int rows, int cols, int colors){
		this.rows = rows;
		this.cols = cols;
		this.maxColors = colors;
		this.tiles = new int[rows][cols];
		this.colorQty = new int[maxColors];
		generate(rows);
	}
	
	public int getRows(){
		return this.rows;
	}
	
	public int getCols(){
		return this.cols;
	}
	
	public int getMovements(){
		return movements;
	}
	
	public void setRows(int rows) {
		if(rows <= 0) {
			throw new IllegalArgumentException("Invalid rows value: " + rows);
		}
		this.rows = rows;
	}

	public void setColumns(int cols) {
		if(cols <= 0) {
			throw new IllegalArgumentException("Invalid cols value: " + cols);
		}
		this.cols = cols;
	}

	public void setMaxColors(int colors) {
		if(colors <= 0) {
			throw new IllegalArgumentException("Invalid colors value: " + colors);
		}
		this.maxColors = colors;
	}

	@Override
	public boolean compare(GPSState state) {
		if (!(state instanceof Board)) {
			return false;
		}
		Board other = (Board) state;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (other.tiles[i][j] != tiles[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isGoal() {
		return rowIsEmpty(0);
	}

	@Override
	public Board clone() {
		int[][] clonedTiles = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				clonedTiles[i][j] = this.tiles[i][j];
			}
		}
		Board cloned = new Board(clonedTiles, Arrays.copyOf(colorQty, maxColors), maxColors, movements);
		return cloned;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				s.append(tiles[row][col] + ", ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public int shift(int row, int amount) throws NotAppliableException {
		if (rowIsEmpty(row) || isDeadEnd()) {
			throw new NotAppliableException();
		}
		int[] shifted = new int[cols];
		for (int i = 0; i < cols; i++) {
			shifted[i] = tiles[row][(i + amount) % cols];
		}
		tiles[row] = shifted;
		movements++;
		return check();
	}


	public int getTileQty() {
		int qty = 0;
		for(int i = 0; i<maxColors; i++) {
			qty+=colorQty[i];
		}
		return qty;
	}

	public int getLeftColorsQty() {
		int qty = 0;
		for(int i = 0; i<maxColors; i++) {
			if(colorQty[i] != 0) {
				qty++;
			}
		}
		return qty;
	}
	
	public int getColorQty(int color) {
		return colorQty[color-1];
	}
	
	boolean isDeadEnd() {
		for(int i = 0; i<maxColors; i++) {
			if (colorQty[i] == 1 || colorQty[i] == 2) {
				return true;
			}
		}
		return false;
	}
	
	private void calculateColorQty() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int color = tiles[i][j];
				if (color != EMPTY) {
					colorQty[color-1]++;
				}
			}
		}
	}
	
	/**
	 * Checks if there's any popping to be done after a shift.
	 */
	private int check() {
		boolean popped = true;
		int poppedQty = 0;
		while (popped) {
			popped = false;
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					if (tiles[row][col] != EMPTY) {
						Set<Point> colored = adjacentTiles(row, col);
						if (colored.size() >= 3) {
							pop(colored);
							poppedQty += colored.size();
							popped = true;
						}
					}
				}
			}
			lift();
		}
		return poppedQty;
	}

	/**
	 * Fills a set of points of adjacent tiles of the same color.
	 */
	Set<Point> adjacentTiles(int row, int col) {
		Set<Point> colored = new HashSet<Point>();
		fillColored(colored, tiles[row][col], row, col);
		return colored;
	}

	private void fillColored(Set<Point> colored, int color, int row, int col) {
		if (isValidPoint(row, col) && tiles[row][col] == color
				&& !colored.contains(new Point(row, col))) {
			colored.add(new Point(row, col));
			fillColored(colored, color, row - 1, col);
			fillColored(colored, color, row + 1, col);
			fillColored(colored, color, row, col - 1);
			fillColored(colored, color, row, col + 1);
		}
	}

	private void generate(int maxRows){
		for(int row = 0; row<maxRows; row++){
			for(int col = 0; col<cols; col++){
				generateTile(row, col);
			}
		}
	}
	
	private void generateTile(int row, int col) {
		Random rand = new Random();
		tiles[row][col] = rand.nextInt(maxColors) + 1;
		while(adjacentTiles(row, col).size()>=3){
			tiles[row][col] = rand.nextInt(maxColors) + 1;
		}
		colorQty[tiles[row][col]-1]++;
	}
	
	private void pop(Set<Point> colored) {
		for (Point point : colored) {
			colorQty[tiles[point.x][point.y]-1]--;
			tiles[point.x][point.y] = EMPTY;
		}
	}

	/**
	 * Makes the tiles float to the top if there's an empty space above them.
	 */
	private void lift() {
		for (int col = 0; col < cols; col++) {
			int row = 0;
			int emptySpace;
			while (row < rows && tiles[row][col] != EMPTY) {
				row++;
			}
			if (row != rows) { // encontre un espacio
				emptySpace = row;
				while (row < rows) {
					while (row < rows && tiles[row][col] == EMPTY) {
						row++;
					}
					if (row != rows) { // encontre una pelotita
						tiles[emptySpace][col] = tiles[row][col];
						tiles[row][col] = EMPTY;
						row++;
						emptySpace++;
					}
				}
			}
		}
	}

	private boolean isValidPoint(int row, int col) {
		return row >= 0 && col >= 0 && row < rows && col < cols;
	}

	private boolean rowIsEmpty(int row) {
		for (int col = 0; col<cols; col++) {
			if(tiles[row][col] != EMPTY) {
				return false;
			}
		}
		return true;
	}

	public int getMaxColors() {
		return maxColors;
	}

	public int getGroupQty() {
		int groups = 0;
		for(int row=0; row<rows; row++){
			for(int col=0; col<cols; col++){
				if(adjacentTiles(row, col).size()==2){
					groups++;
				}
			}
		}
		return groups/2;
	}
	
	
	@Override
	public int hashCode(){
		int hash = 1;
		for( int i = 0; i<this.rows; i++ ){
			for( int j = 0; j<this.cols; j++ ){
				hash*= Math.pow(HASH_MATRIX[i][j],tiles[i][j]);
			}
		}
		return hash;
	}
}
