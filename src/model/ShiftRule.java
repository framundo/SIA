package model;

import gps.api.GpsRulea;
import gps.api.GpsStatea;
import gps.exception.NotAppliableException;

public class ShiftRule implements GpsRulea{
	
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
	public GpsStatea evalRule(GpsStatea state) throws NotAppliableException {
		Board board = ((Board) state).clone();
		board.shift(row, amount);
		return board;
	}
}
