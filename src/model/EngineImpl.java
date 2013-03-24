package model;

import gps.Frontier;
import gps.GPSEngine;
import gps.GPSNode;

import java.util.HashSet;
import java.util.Set;

public class EngineImpl extends GPSEngine{

	
	@Override
	public void addNode(GPSNode node) {
		Frontier frontier = getFrontier();
		Set<GPSNode> explored = getExplored();
		if(explored.contains(node) ){
			System.out.println("vamo lopi");
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
