package model;

import gps.Frontier;
import gps.GPSEngine;
import gps.GPSNode;

public class EngineImpl extends GPSEngine{

	@Override
	public void addNode(GPSNode node) {
		Frontier frontier = getFrontier();
		switch(this.getStrategy()){
			case DFS:
				frontier.push(node);
				break;
			case BFS:
				frontier.offer(node);
				break;
			case AStar:
			case GREEDY:
				frontier.offer(node);
				break;
			case ID:
				frontier.push(node);
				break;
		}
	}

}
