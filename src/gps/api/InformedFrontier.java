package gps.api;

import gps.Frontier;
import gps.GPSNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public class InformedFrontier implements Frontier {

	PriorityQueue<GPSNode> queue;
	
	public InformedFrontier(Comparator<GPSNode> comparator) {
		queue = new PriorityQueue<GPSNode>(10, comparator);
	}

	@Override
	public void push(GPSNode node) {
		throw new IllegalArgumentException("should not call");
	}

	@Override
	public void offer(GPSNode node) {
		queue.offer(node);
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public GPSNode getNext() {
		return queue.poll();
	}

	@Override
	public Collection<GPSNode> getCollection() {
		return queue;
	}
	
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
