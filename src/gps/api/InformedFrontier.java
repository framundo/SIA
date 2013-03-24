package gps.api;

import gps.Frontier;
import gps.GpsNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public class InformedFrontier implements Frontier {

	PriorityQueue<GpsNode> queue;
	
	public InformedFrontier(Comparator<GpsNode> comparator) {
		queue = new PriorityQueue<GpsNode>(10, comparator);
	}

	@Override
	public void push(GpsNode node) {
		throw new IllegalArgumentException("should not call");
	}

	@Override
	public void offer(GpsNode node) {
		queue.offer(node);
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public GpsNode getNext() {
		return queue.poll();
	}

	@Override
	public Collection<GpsNode> getCollection() {
		return queue;
	}
	
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
