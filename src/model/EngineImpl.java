package model;

import gps.Frontier;
import gps.GPSEngine;
import gps.GPSNode;

import java.util.HashSet;
import java.util.Set;

public class EngineImpl extends GPSEngine{


	Set<Integer> nodeHashes;
	
	public EngineImpl() {
		this.nodeHashes = new HashSet<Integer>();
	}
	@Override
	public void addNode(GPSNode node) {
		Frontier frontier = getFrontier();
		int hash = node.hashCode();
		if( nodeHashes.contains(hash) ){
			//System.out.println("vamo lopi");
			return;
		}
		nodeHashes.add(hash);
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
