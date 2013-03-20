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

	private static int ROWS = 4;
	private static int COLS = 4;
	private static int MAX_COLORS = 4;

	private int[][] tiles;
	private int[] colorQty = new int[MAX_COLORS]; 

	private Board(int tiles[][], int[] colorQty) {
		this.tiles = tiles;
		this.colorQty = colorQty;
	}
	
	public Board(){
		this.tiles = new int[ROWS][COLS];
		generate(ROWS);
	}
	
	public static int getRows(){
		return ROWS;
	}
	
	public static int getCols(){
		return COLS;
	}
	
	public static void setRows(int rows) {
		if(rows <= 0) {
			throw new IllegalArgumentException("Invalid rows value: " + rows);
		}
		ROWS = rows;
	}

	public static void setColumns(int cols) {
		if(cols <= 0) {
			throw new IllegalArgumentException("Invalid cols value: " + cols);
		}
		COLS = cols;
	}

	public static void setMaxColors(int colors) {
		if(colors <= 0) {
			throw new IllegalArgumentException("Invalid colors value: " + colors);
		}
		MAX_COLORS = colors;
	}

	@Override
	public boolean compare(GPSState state) {
		if (!(state instanceof Board)) {
			return false;
		}
		Board other = (Board) state;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
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
		int[][] clonedTiles = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				clonedTiles[i][j] = this.tiles[i][j];
			}
		}
		return new Board(clonedTiles, Arrays.copyOf(colorQty, MAX_COLORS));
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
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
		int[] shifted = new int[COLS];
		for (int i = 0; i < COLS; i++) {
			shifted[i] = tiles[row][(i + amount) % COLS];
		}
		tiles[row] = shifted;
		return check();
	}

	boolean isDeadEnd() {
		for(int i = 0; i<MAX_COLORS; i++) {
			if (colorQty[i] == 1 || colorQty[i] == 2) {
				return true;
			}
		}
		return false;
	}
	
	private void calculateColorQty() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
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
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
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
			for(int col = 0; col<COLS; col++){
				generateTile(row, col);
			}
		}
	}
	
	private void generateTile(int row, int col) {
		Random rand = new Random();
		tiles[row][col] = rand.nextInt(MAX_COLORS) + 1;
		while(adjacentTiles(row, col).size()>=3){
			tiles[row][col] = rand.nextInt(MAX_COLORS) + 1;
			colorQty[tiles[row][col]-1]++;
		}
	}
	
	private void pop(Set<Point> colored) {
		for (Point point : colored) {
			colorQty[tiles[point.x][point.y]]--;
			tiles[point.x][point.y] = EMPTY;
		}
	}

	/**
	 * Makes the tiles float to the top if there's an empty space above them.
	 */
	private void lift() {
		for (int col = 0; col < COLS; col++) {
			int row = 0;
			int emptySpace;
			while (row < ROWS && tiles[row][col] != EMPTY) {
				row++;
			}
			if (row != ROWS) { // encontre un espacio
				emptySpace = row;
				while (row < ROWS) {
					while (row < ROWS && tiles[row][col] == EMPTY) {
						row++;
					}
					if (row != ROWS) { // encontre una pelotita
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
		return row >= 0 && col >= 0 && row < ROWS && col < COLS;
	}

//	public static void main(String[] args) throws NotAppliableException {
////		Board board = generateTestBoard();
//		System.out.println(board);
//		board.shift(1, 4);
//		board.shift(2, 3);
//		board.tiles[4][0] = 0;
//		board.tiles[4][1] = 0;
////		board.fill();
//		System.out.println(board);
//	}
	
	public static Board generateTestBoard(){
		int[][] tiles = {
				{1,2,3,4,5,6,7,8},
				{2,3,4,5,6,7,8,1},
				{3,4,5,6,7,8,1,2},
				{4,5,6,7,8,1,2,3},
				{5,6,7,8,1,2,3,4},
				{6,7,8,1,2,3,4,5},
				{7,8,1,2,3,4,5,6},
				{8,1,2,3,4,5,6,7},
				{1,2,3,4,5,6,7,8},
				{2,3,4,5,6,7,8,1},
				{3,4,5,6,7,8,1,2},
				{4,5,6,7,8,1,2,3},
				{5,6,7,8,1,2,3,4},
				{6,7,8,1,2,3,4,5},
				{7,8,1,2,3,4,5,6},
				{8,1,2,3,4,5,6,7},
				{1,2,3,4,5,6,7,8},
				{2,3,4,5,6,7,8,1},
				{3,4,5,6,7,8,1,2},
				{4,5,6,7,8,1,2,3},
				{5,6,7,8,1,2,3,4},
				{6,7,8,1,2,3,4,5},
				{7,8,1,2,3,4,5,6},
				{8,1,2,3,4,5,6,7},
				{3,4,5,6,7,8,1,2},
				{4,5,6,7,8,1,2,3},
				{5,6,7,8,1,2,3,4},
				{6,7,8,1,2,3,4,5},
				{7,8,1,2,3,4,5,6},
				{8,1,2,3,4,5,6,7}};
		int[][] realTiles = new int[ROWS][COLS];
		for (int i =0; i<ROWS; i++) {
			for (int j =0; j<COLS; j++) {
				realTiles[i][j] = tiles[i][j];
			}
		}
		Board board;// = new Board(realTiles);//, MAX_MOVEMENTS, 0);
		int[][] depth7board = {{2, 3, 2, 3}, {4, 2, 1, 4}, {2, 3, 1, 3}, {4, 3, 4, 1}};
		int[] colors = {3, 4, 5, 4};
		board = new Board(depth7board, colors);
		return board;
	}
	
	private boolean rowIsEmpty(int row) {
		for (int col = 0; col<COLS; col++) {
			if(tiles[row][col] != EMPTY) {
				return false;
			}
		}
		return true;
	}

	int getTile(int i, int j) {
		return tiles[i][j];
	}
}
