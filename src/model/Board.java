package model;

import gps.api.GPSState;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements GPSState, Cloneable {

	private static final int ROWS = 5;
	private static final int COLS = 5;
	private static final int MAX_MOVEMENTS = 20;
	private static final int MAX_COLORS = 7;
	private static final int EMPTY = 0;

	private int[][] tiles;
	private int movements;
	private int points;

	private Board(int[][] tiles, int movements, int points) {
		this.tiles = tiles;
		this.movements = movements;
		this.points = points;
	}

	public Board() {
		tiles = new int[ROWS][COLS];
		generateRows(ROWS);
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
		// TODO
		return false;
	}

	@Override
	public Board clone() {
		int[][] clonedTiles = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				clonedTiles[i][j] = this.tiles[i][j];
			}
		}
		return new Board(clonedTiles, movements, points);
	}

	public void shift(int row, int amount) {
		int[] shifted = new int[COLS];
		for (int i = 0; i < COLS; i++) {
			shifted[i] = tiles[row][(i + amount) % COLS];
		}
		tiles[row] = shifted;
		check();
	}
	
	@Override
	public String toString() {
		String s="";
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				s+=tiles[row][col] + ", ";
			}
			s+="\n";
		}
		s+="------------\n";
		return s;
	}


	private void check() {
		boolean popped = true;
		while (popped) {
			popped = false;
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					Set<Point> colored = adjacentTiles(i, j);
					if (colored.size() >= 3) {
						pop(colored);
						popped = true;
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

	private void fill() {
		boolean found = false;
		int count = 0;
		for (int row = ROWS - 1; row >= 0 && !found; row--) {
			for (int col = 0; col < COLS && !found; col++) {
				found = tiles[row][col] != EMPTY;
			}
			if (!found) {
				count++;
			}
		}
		if (count != 0) {
			dropDown(count);
			generateRows(count);
		}
	}

	private void dropDown(int amount) {
		for (int row = ROWS - 1; row >= 0; row--) {
			for (int col = 0; col < COLS; col++) {
				tiles[row][col] = tiles[row - amount][col];
			}
		}
	}

	private void generateRows(int amount) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Random rand = new Random();
				tiles[row][col] = rand.nextInt(MAX_COLORS)+1;
			}
		}
	}

	private boolean isValidPoint(int row, int col) {
		return row >= 0 && col >= 0 && row < ROWS && col < COLS;
	}
		
	public static void main(String[] args) {
		Board board = new Board();
		System.out.println(board);
		board.shift(3,1);
		System.out.println(board);
	}
}