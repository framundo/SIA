package model;

import gps.api.GPSState;
import static model.Board.Tile.EMPTY;
import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements GPSState, Cloneable {

	private static final int ROWS = 10;
	private static final int COLS = 8;
	private static final int DROP_ROWS = 10;

	public enum Tile {
		GREEN, PURPLE, RED, YELLOW, BLUE, ORANGE, EMPTY
	}

	private Tile[][] tiles;
	private int usedRows;

	private Board(Tile[][] tiles, int usedRows) {
		this.tiles = tiles;
		this.usedRows = usedRows;
	}

	public Board() {
		tiles = new Tile[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Random rand = new Random();
				tiles[i][j] = Tile.values()[rand
						.nextInt(Tile.values().length - 1)];
			}
		}
	}

	@Override
	public boolean compare(GPSState state) {
		if (!(state instanceof Board)) {
			return false;
		}
		Board other = (Board) state;
		if (other.usedRows != usedRows) {
			return false;
		}
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (!other.tiles[i][j].equals(tiles[i][j])) {
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
		Tile[][] clonedTiles = new Tile[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				clonedTiles[i][j] = this.tiles[i][j];
			}
		}
		return new Board(clonedTiles, usedRows);
	}

	public void shift(int row, int amount) {
		Tile[] shifted = new Tile[COLS];
		for (int i = 0; i < COLS; i++) {
			shifted[i] = tiles[row][(i + amount) % COLS];
		}
		tiles[row] = shifted;
		check();
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
			lift();
			fill();
		}
	}

	private Set<Point> adjacentTiles(int row, int col) {
		Set<Point> colored = new HashSet<Point>();
		fillColored(colored, tiles[row][col], row, col);
		return colored;
	}

	private void fillColored(Set<Point> colored, Tile color, int row, int col) {
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
		int count=0;
		for(int row = ROWS-1; row>=0 && !found; row--) {
			for(int col = 0; col<COLS && !found; col++) {
				found = tiles[row][col] != EMPTY;
			}
			if (!found) {
				count++;
			}
		}
		if(count!=0) {
			int filledRows = availableRows()<count ? availableRows() : count;
			usedRows += filledRows;
			dropDown(filledRows);
			generateRows(filledRows);
		}
	}
	
	private int availableRows() {
		return DROP_ROWS - usedRows;
	}
	
	private boolean isValidPoint(int row, int col) {
		return row >= 0 && col >= 0 && row < ROWS && col < COLS;
	}
}
