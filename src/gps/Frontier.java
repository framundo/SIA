package gps;

import java.util.Collection;

public interface Frontier {

	public void push(GPSNode node);

	public void offer(GPSNode node);
	
	public int size();

	public GPSNode getNext();

	public 	Collection<GPSNode> getCollection();

	public boolean isEmpty();
}
