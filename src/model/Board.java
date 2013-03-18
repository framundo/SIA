package model;

import gps.api.GPSState;
import gps.exception.NotAppliableException;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements GPSState, Cloneable {
	private static final int EMPTY = 0;

	private static int ROWS = 10;
	private static int COLS = 8;
	private static int MAX_COLORS = 8;
	private static int GOAL_POINTS = 10;
	private static int MAX_MOVEMENTS = 10;

	private int[][] tiles;
	private int movements = MAX_MOVEMENTS;
	private int points = 0;

	private Board(int tiles[][], int movements, int points) {
		this.movements = movements;
		this.points = points;
		this.tiles = tiles;
	}
	
	public Board(){
		this.tiles = new int[ROWS][COLS];
		generate(ROWS);
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
		return this.points >= GOAL_POINTS;
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

	public void shift(int row, int amount) throws NotAppliableException {
		if (movements <= 0) {
			throw new NotAppliableException();
		}
		this.movements--;
		int[] shifted = new int[COLS];
		for (int i = 0; i < COLS; i++) {
			shifted[i] = tiles[row][(i + amount) % COLS];
		}
		tiles[row] = shifted;
		check();
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
		s.append(String.format("\nPoints: %d\nMovements left: %d", points, movements));
		return s.toString();
	}

	private void check() {
		boolean popped = true;
		while (popped) {
			popped = false;
			for (int row = 0; row < ROWS; row++) {
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
				found = found || tiles[row][col] != EMPTY;
			}
			if (!found) {
				count++;
			}
		}
		if (count != 0) {
			dropDown(count);
			generate(count);
		}
	}

	private void dropDown(int amount) {
		for (int row = ROWS - 1; row >= amount; row--) {
			for (int col = 0; col < COLS; col++) {
				tiles[row][col] = tiles[row - amount][col];
			}
		}
	}
	
//	private void dropDown(int amount){
//		this.initialRow -= amount;
//	}

	private void generateRows(int amount) {
		for (int row = 0; row < amount; row++) {
			for (int col = 0; col < COLS; col++) {
				Random rand = new Random();
				tiles[row][col] = rand.nextInt(MAX_COLORS) + 1;
			}
		}
	}

	private boolean isValidPoint(int row, int col) {
		return row >= 0 && col >= 0 && row < ROWS && col < COLS;
	}

	public static void main(String[] args) throws NotAppliableException {
		Board board = generateTestBoard();
		System.out.println(board);
		board.shift(1, 4);
		board.shift(2, 3);
		board.tiles[4][0] = 0;
		board.tiles[4][1] = 0;
		board.fill();
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
		int[][] realTiles = new int[ROWS][COLS];
		for (int i =0; i<ROWS; i++) {
			for (int j =0; j<COLS; j++) {
				realTiles[i][j] = tiles[i][j];
			}
		}
		Board board = new Board(realTiles, MAX_MOVEMENTS, 0);
		return board;
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

	public static void setGoalPoints(int points) {
		if(points <= 0) {
			throw new IllegalArgumentException("Invalid points value: " + points);
		}
		GOAL_POINTS = points;
	}

	public static void setMaxMovements(int movements) {
		if(movements <= 0) {
			throw new IllegalArgumentException("Invalid movements value: " + movements);
		}
		MAX_MOVEMENTS = movements;
	}

	public static int getMaxMovements() {
		return MAX_MOVEMENTS;
	}
}
