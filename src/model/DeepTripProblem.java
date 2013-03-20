package model;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public class DeepTripProblem implements GPSProblem{

	public enum Heuristic {TILES};

	private static Heuristic HEURISTIC;

	public static void setHeuristic(Heuristic h) {
		HEURISTIC = h;
	}

	@Override
	public GPSState getInitState() {
//				return new Board();
		return Board.generateTestBoard();
	}

	@Override
	public GPSState getGoalState() {
		return null;
	}

	@Override
	public List<GPSRule> getRules() {
		List<GPSRule> rules = new ArrayList<GPSRule>(Board.getRows()*(Board.getCols()-1));
		for(int i = 0; i < Board.getRows(); i++){
			for(int j = 1; j < Board.getCols(); j++){
				rules.add(new ShiftRule(i,j));
			}
		}
		return rules;
	}

	@Override
	public Integer getHValue(GPSState state) {
		Board board = (Board)state;
		if (board.isDeadEnd()) {
			return Integer.MAX_VALUE;
		}
		switch(HEURISTIC){
		case TILES:
			return tilesHValue(board);
		default:
			return null;
		}
	}
	

//	private Double stepsHValue(Board board) {
//		return tilesHValue(board)/(Board.getRows()*Board.getCols());
//	}

	private Integer tilesHValue(Board board) {
		int qty = 0;
		int rows = Board.getRows();
		int cols = Board.getCols();
		for(int i = 0; i<rows; i++) {
			boolean empty = true;
			for(int j = 0; j < cols; j++) {
				if (board.getTile(i, j) != Board.EMPTY) {
					qty++;
					empty = false;
				}
			}
			if(empty) {
				return qty;
			}
		}
		return qty;
	}
}
