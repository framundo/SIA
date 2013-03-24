package gps;

import gps.api.GpsRulea;
import gps.api.GpsStatea;
import model.DeepTripProblem;
public class GpsNode {

	private GpsStatea state;
	private GpsNode parent;
	private GpsRulea rule;
	private Integer cost;
	private Double hValue;
	private int height;
	
	public GpsNode(GpsStatea state, GpsNode parent, GpsRulea rule, Integer cost, Double hValue) {
		super();
		this.rule = rule;
		this.parent = parent;
		this.state = state;
		this.cost = cost;
		this.hValue = hValue;
		this.height = parent == null ? 0 : parent.getHeight() + 1;
	}

	public GpsNode getParent() {
		return parent;
	}

	public GpsStatea getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}

	public int getHeight() {
		return height;
	}
	
	public Double getHValue() {
		return hValue;
	}

	@Override
	public String toString() {
		return (rule != null ? rule.getName() + "\n\n" : "") + state + "\n=========\n";
	}

	public String getSolution() {
		if (this.parent == null) {
			return toString();
		}
		return this.parent.getSolution() + "\n" + toString();
	}
	
	@Override
	public int hashCode(){
		return this.state.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GpsNode)) {
			return false;
		}
		return state.equals(((GpsNode)o).state);
	}
}
