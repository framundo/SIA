package model;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public class DeepTripProblem implements GPSProblem{

	public enum Heuristic {TILES, COLORS, STEPS};

	private Heuristic heuristic;

	private Board board;
	
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	
	public DeepTripProblem(Board board, Heuristic heuristic) {
		this.board = board;
		this.heuristic = heuristic;
	}
	
	@Override
	public GPSState getInitState() {
		return this.board;
	}

	@Override
	public GPSState getGoalState() {
		return null;
	}

	@Override
	public List<GPSRule> getRules() {
		List<GPSRule> rules = new ArrayList<GPSRule>(board.getRows()*(board.getCols()-1));
		for(int i = 0; i < board.getRows(); i++){
			for(int j = 1; j < board.getCols(); j++){
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
		switch(heuristic){
		case STEPS:
			return stepsHValue(board);
		case COLORS:
			return (board.getTileQty() * 6 + 4 * board.getLeftColorsQty() * (board.getCols() * board.getRows())) / 10;
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
