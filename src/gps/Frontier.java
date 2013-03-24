package gps;

import java.util.Collection;

public interface Frontier {

	public void push(GpsNode node);

	public void offer(GpsNode node);
	
	public int size();

	public GpsNode getNext();

	public 	Collection<GpsNode> getCollection();

	public boolean isEmpty();
}
