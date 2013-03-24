package solver;

import gps.SearchStrategy;
import gps.api.GPSProblem;
import model.Board;
import model.DeepTripProblem;
import model.DeepTripProblem.Heuristic;
import model.EngineImpl;

public class ProblemSolver {

	private static final String STRATEGY_COMMAND = "strategy=";
	private static final String HEURISTICS_COMMAND = "heuristics=";
	private static final String ROWS_COMMAND = "rows=";
	private static final String COLS_COMMAND = "cols=";
	private static final String COLORS_COMMAND = "colors=";

	public static void main(String[] args) {
//		if (args.length != 2 && args.length != 5) { // strategia y heurist
//			throw new IllegalArgumentException("Invalid argument quantity");
//		}
//		SearchStrategy strategy = parseStrategy(args[0]);
//		//TODO Parse heuristics
//		if (args.length == 5) { // especifico los valores del board
//			parseAndSetBoardValues(args);
//		}
//		GPSProblem problem = new DeepTripProblem();
//		(new EngineImpl()).engine(problem, strategy);
		//GPSProblem problem = new DeepTripProblem(Board.generateTestBoard(), Heuristic.TILES);
		//(new EngineImpl()).engine(problem, SearchStrategy.AStar);
	}
	
	private static SearchStrategy parseStrategy(String strategyCommand) {
		strategyCommand = parseCommand(strategyCommand, STRATEGY_COMMAND);
		try {
			return SearchStrategy.valueOf(strategyCommand);
		} catch(Exception e) {
			throw new IllegalArgumentException("Unable to find the given startegy");
		}
	}
	
	private static Heuristic parseHeuristic(String heuristicCommand) {
		heuristicCommand = parseCommand(heuristicCommand, HEURISTICS_COMMAND);
		try {
			return Heuristic.valueOf(heuristicCommand);
		} catch(Exception e) {
			throw new IllegalArgumentException("Unable to find the given heuristic");
		}
	}
	
	private Board parseAndSetBoardValues(String[] args) {
		String rows = parseCommand(args[3], ROWS_COMMAND);		
		String cols = parseCommand(args[4], COLS_COMMAND);
		String colors = parseCommand(args[5], COLORS_COMMAND);
		try {
			Board board = new Board(Integer.valueOf(rows),Integer.valueOf(cols),Integer.valueOf(colors));
			return board;
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Error parsing board arguments");
		}
	}
	
	private static String parseCommand(String value, String comandPrefix) {
		if (!value.startsWith(comandPrefix)) {
			throw new IllegalArgumentException("Invalid argument " + value);
		}
		return value.substring(comandPrefix.length());
	}
}
