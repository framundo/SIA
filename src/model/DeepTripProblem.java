package model;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public class DeepTripProblem implements GPSProblem{

	
	@Override
	public GPSState getInitState() {
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
			for(int j = 0; j < Board.getCols(); j++){
				rules.add(new ShiftRule(i,j));
			}
		}
		return rules;
	}

	@Override
	public Integer getHValue(GPSState state) {
		// TODO Esto va a estar heavy... no se me ocurre nada...
		return null;
	}

}
