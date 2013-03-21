package model;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public class DeepTripProblem implements GPSProblem{

	public enum Heuristic {TILES, COLORS, STEPS};

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
		case STEPS:
			return stepsHValue(board);
		case COLORS:
			return (board.getTileQty() * 6 + board.getLeftColorsQty() * (Board.getCols() * Board.getRows())) / 10;
		case TILES:
			return board.getTileQty();
		default:
			return null;
		}
	}

	private Integer stepsHValue(Board board) {
		int hVal = 0;
		for(int color = 0; color<board.getMaxColors(); color++) {
			hVal += board.getColorQty(color + 1) / 3;
		}
		return hVal;
	}
}
