package model;

import gps.GPSNode;
import gps.SearchStrategy;

import java.util.Comparator;

public class ComparatorProvider {

	public static Comparator<GPSNode> get(SearchStrategy strategy) {
		switch(strategy) {
		case AStar:
			return new Comparator<GPSNode>() {
				@Override
				public int compare(GPSNode node1, GPSNode node2) {
					if (node1 == null || node2 == null) {
						throw new IllegalArgumentException("Comparing nulls");
					}
					Integer value1 = node1.getHValue() + node1.getCost();
					Integer value2 = node2.getHValue() + node2.getCost();
					if (value1.equals(value2)) {
						return node1.getHValue().compareTo(node2.getHValue()); //TODO check order
					}
					return value1 - value2;
				}
			};
		case GREEDY:
			return new Comparator<GPSNode>() {
				@Override
				public int compare(GPSNode node1, GPSNode node2) {
					if (node1 == null || node2 == null) {
						throw new IllegalArgumentException("Comparing nulls");
					}
					return node1.getHValue().compareTo(node2.getHValue());
				}
			};
		default:
			return null;
		}
	}
}
