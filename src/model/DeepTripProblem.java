package model;

import gps.api.GpsProblema;
import gps.api.GpsRulea;
import gps.api.GpsStatea;

import java.util.ArrayList;
import java.util.List;

public class DeepTripProblem implements GpsProblema{

	public enum Heuristic {
		/** H1 */
		TILES, 
		/** H2 */
		STEPS,
		/** H3 */
		COLORS, 
		/** H4 */
		GROUP,
		/** H5 */
		EMPTY
	};

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
	public GpsStatea getInitState() {
		return this.board;
	}

	@Override
	public GpsStatea getGoalState() {
		return null;
	}

	@Override
	public List<GpsRulea> getRules() {
		List<GpsRulea> rules = new ArrayList<GpsRulea>(board.getRows()*(board.getCols()-1));
		for(int i = 0; i < board.getRows(); i++){
			for(int j = 1; j < board.getCols(); j++){
				rules.add(new ShiftRule(i,j));
			}
		}
		return rules;
	}

	@Override
	public Double getHValue(GpsStatea state) {
		Board board = (Board)state;
		if (board.isDeadEnd()) {
			return Double.MAX_VALUE;
		}
		switch(heuristic){
		case TILES:
			return (double)board.getTileQty();
		case STEPS:
			return stepsHValue(board);
		case COLORS:
			return (double)(board.getTileQty() * 6 + 4*board.getLeftColorsQty() * (board.getCols() * board.getRows())) / 10;
		case EMPTY:
			return emptyHValue(board);
		case GROUP:
			return groupHValue(board);
		default:
			return null;
		}
	}

	private Double stepsHValue(Board board) {
		double hVal = 0;
		for(int color = 0; color<board.getMaxColors(); color++) {
			hVal += board.getColorQty(color + 1) / 3;
		}
		return hVal;
	}
	
	private Double emptyHValue(Board board){
		int blank = board.getRows() * board.getCols() - board.getTileQty();
		return 1.0 / ((blank+1) * board.getMovements());
	}
	
	private Double groupHValue(Board board){
		return (double)board.getTileQty() / (2*(board.getGroupQty() + 1));
	}
}
