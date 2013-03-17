package model;

import gps.api.GPSState;
import gps.exception.NotAppliableException;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements GPSState, Cloneable {
	private static final int VISIBLE_ROWS = 5;
	private static final int INITIAL_ROW = 20;
	private static final int TOTAL_ROWS = INITIAL_ROW + VISIBLE_ROWS;
	private static final int COLS = 5;
	private static final int MAX_COLORS = 8;
	private static final int EMPTY = 0;
	private static final int GOAL_POINTS = 1;
	public static final int MAX_MOVEMENTS = 3;

	private int initialRow = INITIAL_ROW;
	private int[][] tiles;
	private int movements = MAX_MOVEMENTS;
	private int points = 0;

	private Board(int tiles[][], int movements, int points, int initialRow) {
		this.initialRow = initialRow;
		this.movements = movements;
		this.points = points;
		this.tiles = tiles;
	}
	
	public Board(){
		this.tiles = new int[TOTAL_ROWS][COLS];
		generate();
	}

	private void generate(){
		for(int row = 0; row<TOTAL_ROWS; row++){
			for(int col = 0; col<COLS; col++){
				Random rand = new Random();
				tiles[row][col] = rand.nextInt(MAX_COLORS) + 1;
				while(adjacentTiles(row, col).size()>=3){
					tiles[row][col] = rand.nextInt(MAX_COLORS) + 1;
				}
			}
		}
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
		return this.points >= GOAL_POINTS;
	}

	@Override
	public Board clone() {
		int[][] clonedTiles = new int[TOTAL_ROWS][COLS];
		for (int i = 0; i < TOTAL_ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				clonedTiles[i][j] = this.tiles[i][j];
			}
		}
		return new Board(clonedTiles, movements, points, initialRow);
	}

	public void shift(int row, int amount) throws NotAppliableException {
		if (movements <= 0) {
			throw new NotAppliableException();
		}
		this.movements--;
		int[] shifted = new int[COLS];
		for (int i = 0; i < COLS; i++) {
			shifted[i] = tiles[initialRow+row][(i + amount) % COLS];
		}
		tiles[initialRow+row] = shifted;
		check();
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int row = initialRow; row < initialRow + VISIBLE_ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				s.append(tiles[row][col] + ", ");
			}
			s.append("\n");
		}
		s.append(String.format("Points: %d\nMovements left: %d", points, movements));
		return s.toString();
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

	private void pop(Set<Point> colored) {
		for (Point point : colored) {
			tiles[point.x][point.y] = EMPTY;
		}
		points += Math.pow(2, colored.size()-2)-1;
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
		Board board = new Board();
		System.out.println(board);
//		board.shift(0, 2);
//		board.shift(1, 1);
//		System.out.println(board);
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
		int[][] realTiles = new int[TOTAL_ROWS][COLS];
		for (int i =0; i<TOTAL_ROWS; i++) {
			for (int j =0; j<COLS; j++) {
				realTiles[i][j] = tiles[i][j];
			}
		}
		Board board = new Board(realTiles, MAX_MOVEMENTS, 0, INITIAL_ROW);
		return board;
	}
	
	public static int getRows(){
		return VISIBLE_ROWS;
	}
	
	public static int getCols(){
		return COLS;
	}
}
