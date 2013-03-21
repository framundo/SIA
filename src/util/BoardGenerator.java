package util;

import gps.SearchStrategy;
import gps.api.GPSProblem;
import model.Board;
import model.DeepTripProblem;
import model.DeepTripProblem.Heuristic;
import model.EngineImpl;

public class BoardGenerator {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.err.println("No bardiŽs.");
		}
		int rows = Integer.valueOf(args[0]);
		int cols = Integer.valueOf(args[1]);
		int colors = Integer.valueOf(args[2]);
		int tries = Integer.valueOf(args[3]);

		for (int i = 0; i < tries; i++) {
			boolean answer = false;
			while (!answer) {
				Board board = new Board(rows, cols, colors);
				GPSProblem problem = new DeepTripProblem(board, Heuristic.TILES);
				answer = new EngineImpl().engine(problem, SearchStrategy.AStar);
				if(answer){
					System.err.println(board);
				}
			}
			
		}
	}
}
