package gps.api;

import gps.Frontier;
import gps.GPSNode;

import java.util.Collection;
import java.util.LinkedList;

public class NaiveFrontier implements Frontier {

	private LinkedList<GPSNode> list = new LinkedList<GPSNode>();
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public GPSNode getNext() {
		return list.poll();
	}

	@Override
	public Collection<GPSNode> getCollection() {
		return list;
	}

	@Override
	public void push(GPSNode node) {
		list.push(node);
	}

	@Override
	public void offer(GPSNode node) {
		list.offer(node);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
}
