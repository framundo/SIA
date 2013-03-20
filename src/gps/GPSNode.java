package gps;

import gps.api.GPSRule;
import gps.api.GPSState;
import model.DeepTripProblem;
public class GPSNode {

	private GPSState state;
	private GPSNode parent;
	private GPSRule rule;
	private Integer cost;
	private Integer hValue;
	private int height;
	
	public GPSNode(GPSState state, GPSNode parent, GPSRule rule, Integer cost, Integer hValue) {
		super();
		this.rule = rule;
		this.parent = parent;
		this.state = state;
		this.cost = cost;
		this.hValue = hValue;
		this.height = parent == null ? 0 : parent.getHeight() + 1;
	}

	public GPSNode getParent() {
		return parent;
	}

	public GPSState getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}

	public int getHeight() {
		return height;
	}
	
	public Integer getHValue() {
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
}
