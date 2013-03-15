package model;

import gps.GPSEngine;
import gps.GPSNode;

import java.util.LinkedList;

public class EngineImpl extends GPSEngine{

	@Override
	public void addNode(GPSNode node) {
		LinkedList<GPSNode> open = this.getOpened();
		switch(this.getStrategy()){
			case DFS:
				open.push(node);
				break;
			case BFS:
				open.offer(node);
				break;
			case AStar:
				break;
		}
	}

}
