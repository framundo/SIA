package gps;

import gps.api.GPSRule;
import gps.api.GPSState;

public class GPSNode {

	private GPSState state;

	private GPSNode parent;

	private GPSRule rule;
	
	private Integer cost;

	private int height;
	
	public GPSNode(GPSState state, GPSRule rule, Integer cost) {
		super();
		this.rule = rule;
		this.state = state;
		this.cost = cost;
		this.height = parent == null ? 0 : parent.getHeight() + 1;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
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
