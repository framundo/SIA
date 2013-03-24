package model;

import gps.api.GpsRule;
import gps.api.GpsState;
import gps.exception.NotAppliableException;

public class ShiftRule implements GpsRule{
	
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
	public GpsState evalRule(GpsState state) throws NotAppliableException {
		Board board = ((Board) state).clone();
		board.shift(row, amount);
		return board;
	}
}
