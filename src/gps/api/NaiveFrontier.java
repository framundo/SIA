package gps.api;

import gps.Frontier;
import gps.GpsNode;

import java.util.Collection;
import java.util.LinkedList;

public class NaiveFrontier implements Frontier {

	private LinkedList<GpsNode> list = new LinkedList<GpsNode>();
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public GpsNode getNext() {
		return list.poll();
	}

	@Override
	public Collection<GpsNode> getCollection() {
		return list;
	}

	@Override
	public void push(GpsNode node) {
		list.push(node);
	}

	@Override
	public void offer(GpsNode node) {
		list.offer(node);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
}
