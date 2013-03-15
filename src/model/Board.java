package model;

import gps.api.GPSState;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements GPSState, Cloneable {

	private static final int VISIBLE_ROWS = 10;
	private static final int TOTAL_ROWS = 30;
	private static final int INITIAL_ROW = 20;
	private static final int COLS = 8;
	private static final int MAX_MOVEMENTS = 20;
	private static final int MAX_COLORS = 8;
	private static final int EMPTY = 0;

	private int initialRow;
	private int[][] tiles;
	private int movements;
	private int points;

	private Board(int[][] tiles, int movements, int points) {
		this.initialRow = INITIAL_ROW;
		this.tiles = tiles;
		this.movements = movements;
		this.points = points;
	}

	public Board() {
		tiles = new int[TOTAL_ROWS][COLS];
		generateRows(TOTAL_ROWS);
	}

	@Override
	public boolean compare(GPSState state) {
		if (!(state instanceof Board)) {
			return false;
		}
		Board other = (Board) state;
		if (other.movements != movements || other.points != points) {
			return false;
		}
		for (int i = initialRow; i < initialRow + VISIBLE_ROWS; i++) {
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
		// TODO
		return false;
	}

	@Override
	public Board clone() {
		int[][] clonedTiles = new int[VISIBLE_ROWS][COLS];
		for (int i = initialRow; i < initialRow + VISIBLE_ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				clonedTiles[i][j] = this.tiles[i][j];
			}
		}
		return new Board(clonedTiles, movements, points);
	}

	public void shift(int row, int amount) {
		int[] shifted = new int[COLS];
		for (int i = 0; i < COLS; i++) {
			shifted[i] = tiles[initialRow+row][(i + amount) % COLS];
		}
		tiles[initialRow+row] = shifted;
		check();
	}

	@Override
	public String toString() {
		String s = "";
		for (int row = initialRow; row < initialRow + VISIBLE_ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				s += tiles[row][col] + ", ";
			}
			s += "\n";
		}
		s += "------------\n";
		return s;
	}

	private void check() {
		boolean popped = true;
		while (popped) {
			popped = false;
			for (int row = initialRow; row < initialRow + VISIBLE_ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (tiles[row][col] != EMPTY) {
						Set<Point> colored = adjacentTiles(row, col);
						if (colored.size() >= 3) {
							pop(colored);
							popped = true;
						}
					}
				}
			}
			if (popped) {
				lift();
				fill();
			}
		}
	}

	private Set<Point> adjacentTiles(int row, int col) {
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

	private void pop(Set<Point> colored) {
		for (Point point : colored) {
			tiles[point.x][point.y] = EMPTY;
		}
	}

	private void lift() {
		for (int col = 0; col < COLS; col++) {
			int row = 0;
			int emptySpace;
			while (row < initialRow + VISIBLE_ROWS && tiles[row][col] != EMPTY) {
				row++;
			}
			if (row != initialRow + VISIBLE_ROWS) { // encontre un espacio
				emptySpace = row;
				while (row < initialRow + VISIBLE_ROWS) {
					while (row < initialRow + VISIBLE_ROWS && tiles[row][col] == EMPTY) {
						row++;
					}
					if (row != initialRow + VISIBLE_ROWS) { // encontre una pelotita
						tiles[emptySpace][col] = tiles[row][col];
						tiles[row][col] = EMPTY;
						row++;
						emptySpace++;
					}
				}
			}
		}
	}

	private void fill() {
		boolean found = false;
		int count = 0;
		for (int row = initialRow + VISIBLE_ROWS - 1; row >= 0 && !found; row--) {
			for (int col = 0; col < COLS && !found; col++) {
				found = tiles[row][col] != EMPTY;
			}
			if (!found) {
				count++;
			}
		}
		if (count != 0) {
			dropDown(count);
		}
	}

//	private void dropDown(int amount) {
//		for (int row = VISIBLE_ROWS - 1; row >= amount; row--) {
//			for (int col = 0; col < COLS; col++) {
//				tiles[row][col] = tiles[row - amount][col];
//			}
//		}
//	}
	
	private void dropDown(int amount){
		this.initialRow -= amount;
	}

	private void generateRows(int amount) {
		for (int row = 0; row < amount; row++) {
			for (int col = 0; col < COLS; col++) {
				Random rand = new Random();
				tiles[row][col] = rand.nextInt(MAX_COLORS) + 1;
			}
		}
	}

	private boolean isValidPoint(int row, int col) {
		return row >= initialRow && col >= 0 && row < initialRow + VISIBLE_ROWS && col < COLS;
	}

	public static void main(String[] args) {
		Board board = generateTestBoard();
		System.out.println(board);
		board.shift(0, 2);
		board.shift(1, 1);
		System.out.println(board);
	}
	
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
		Board board = new Board(tiles, 10, 10);
		return board;
	}
}
