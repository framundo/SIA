package model;

import gps.api.GPSRule;
import gps.api.GPSState;
import gps.exception.NotAppliableException;

public class ShiftRule implements GPSRule{
	
	private int row, amount;

	public ShiftRule(int row, int amount){
		this.row = row;
		this.amount = amount;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Shifted row "+row+", amount "+amount;
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		Board board = ((Board) state).clone();
		board.shift(row, amount);
		return board;
	}
}
