package solver;

import gps.SearchStrategy;
import gps.api.GPSProblem;
import model.DeepTripProblem;
import model.EngineImpl;

public class ProblemSolver {

	public static void main(String[] args) {
		GPSProblem problem = new DeepTripProblem();
		(new EngineImpl()).engine(problem, SearchStrategy.BFS);
	}
}
