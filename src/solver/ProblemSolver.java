package solver;

import java.awt.datatransfer.StringSelection;

import gps.SearchStrategy;
import gps.api.GPSProblem;
import model.Board;
import model.DeepTripProblem;
import model.EngineImpl;
import model.DeepTripProblem.Heuristic;

public class ProblemSolver {

	private static final String STRATEGY_COMMAND = "strategy=";
	private static final String HEURISTICS_COMMAND = "heuristics=";
	private static final String ROWS_COMMAND = "rows=";
	private static final String COLS_COMMAND = "cols=";
	private static final String GOAL_POINTS_COMMAND = "points=";
	private static final String COLORS_COMMAND = "colors=";
	private static final String MOVEMENTS_COMMAND = "movs=";

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
		GPSProblem problem = new DeepTripProblem();
		DeepTripProblem.setHeuristic(Heuristic.TILES);
		(new EngineImpl()).engine(problem, SearchStrategy.AStar);
	}
	
	private static SearchStrategy parseStrategy(String strategyCommand) {
		if (!strategyCommand.startsWith(STRATEGY_COMMAND)) {
			throw new IllegalArgumentException("Invalid argument strategy");
		}
		strategyCommand = strategyCommand.substring(STRATEGY_COMMAND.length());
		try {
			return SearchStrategy.valueOf(strategyCommand);
		} catch(Exception e) {
			throw new IllegalArgumentException("Unable to find the given startegy");
		}
	}
	
	private static void parseAndSetBoardValues(String[] args) {
		String rows = parseCommand(args[3], ROWS_COMMAND);		
		String cols = parseCommand(args[4], COLS_COMMAND);
//		String goalPoints = parseCommand(args[5], GOAL_POINTS_COMMAND);
//		String movements = parseCommand(args[6], MOVEMENTS_COMMAND);
		String colors = parseCommand(args[5], COLORS_COMMAND);
		try {
			Board.setRows(Integer.valueOf(rows));
			Board.setColumns(Integer.valueOf(cols));
//			Board.setGoalPoints(Integer.valueOf(goalPoints));
//			Board.setMaxMovements(Integer.valueOf(movements));
			Board.setMaxColors(Integer.valueOf(colors));
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
