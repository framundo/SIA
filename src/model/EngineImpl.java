package model;

import gps.Frontier;
import gps.GpsEngine;
import gps.GpsNode;

import java.util.Set;

public class EngineImpl extends GpsEngine{

	
	@Override
	public void addNode(GpsNode node) {
		Frontier frontier = getFrontier();
		Set<GpsNode> explored = getExplored();
		if(explored.contains(node)){
			return;
		}
		explored.add(node);
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
