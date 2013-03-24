package gps;

import gps.api.GpsProblem;
import gps.api.GpsRule;
import gps.api.GpsState;
import gps.api.InformedFrontier;
import gps.api.NaiveFrontier;
import gps.exception.NotAppliableException;

import java.util.HashSet;
import java.util.Set;

import util.StatisticData;

import model.ComparatorProvider;

public abstract class GpsEngine {

	private Frontier frontier;
	private Set<GpsNode> explored = new HashSet<GpsNode>();
	private GpsProblem problem;

	// Use this variable in the addNode implementation
	private SearchStrategy strategy;

	public StatisticData engine(GpsProblem myProblem, SearchStrategy myStrategy) {
		if (myStrategy == SearchStrategy.AStar || myStrategy == SearchStrategy.GREEDY) {
			frontier = new InformedFrontier(ComparatorProvider.get(myStrategy));
		} else {
			frontier = new NaiveFrontier();
		}
		long time0 = System.currentTimeMillis();
		problem = myProblem;
		strategy = myStrategy;
		int iterativeDepth = 1;
		int nodeMaxHeight = 0; // Used on ID
		int frontierTotalSize = 0; 
		int exploredTotalSize = 0;

		GpsState rootState = problem.getInitState();
		GpsNode rootNode = new GpsNode(rootState, null, null, 0, problem.getHValue(rootState));
		boolean finished = false;
		boolean failed = false;
		long elapsedTime = 0;
		StatisticData data = new StatisticData();
		
		frontier.offer(rootNode);
		while (!failed && !finished) {
			if (frontier.isEmpty()) {
				if (myStrategy == SearchStrategy.ID && iterativeDepth == nodeMaxHeight) { // Tengo que aumentar el nivel si o si
					iterativeDepth++;
					frontierTotalSize += frontier.size();
					exploredTotalSize += explored.size();
					explored = new HashSet<GpsNode>();
					frontier = new NaiveFrontier();
					frontier.offer(rootNode);
				} else {
					failed = true;
				}
			} else {
				GpsNode currentNode = frontier.getNext(); 
				nodeMaxHeight = Math.max(nodeMaxHeight, currentNode.getHeight());
				explored.add(currentNode);
				if (isGoal(currentNode)) {
					finished = true;
					frontierTotalSize += frontier.size();
//					System.out.println(currentNode.getSolution());
//					System.out.println("Height of the solution: " + currentNode.getHeight());
//					System.out.println("Generated nodes: " + (frontierTotalSize + explored.size()));
//					System.out.println("Frontier nodes: " + frontierTotalSize);
//					System.out.println("Expanded nodes: " + (explored.size() - 1 + exploredTotalSize));
					elapsedTime = System.currentTimeMillis() - time0;
//					System.out.println("Time elapsed: " + elapsedTime);
					data.time = elapsedTime;
					data.height = currentNode.getHeight();
					data.nodes = (frontierTotalSize + explored.size());
				} else {
					if (myStrategy != SearchStrategy.ID || iterativeDepth > currentNode.getHeight()) {
						explode(currentNode);
					}
				}
			}
		}
		if (finished) {
//			System.out.println("OK! solution found!");
			return data;
		} else if (failed) {
//			System.out.println("FAILED! solution not found!");
			return null;
		}
		return null;
	}

	private  boolean isGoal(GpsNode currentNode) {
		return currentNode.getState().isGoal();
	}

	private  boolean explode(GpsNode node) {
		if(problem.getRules() == null){
			System.err.println("No rules!");
			return false;
		}

		for (GpsRule rule : problem.getRules()) {
			GpsState newState = null;
			try {
				newState = rule.evalRule(node.getState());
			} catch (NotAppliableException e) {
				// Do nothing
			}
			if (newState != null
					&& !checkBranch(node, newState)
					&& !checkOpenAndClosed(node.getCost() + rule.getCost(),
							newState)) {
				GpsNode newNode = new GpsNode(newState, node, rule, node.getCost() + rule.getCost(), problem.getHValue(newState));
				addNode(newNode);
			}
		}
		return true;
	}

	private  boolean checkOpenAndClosed(Integer cost, GpsState state) {
		for (GpsNode openNode : frontier.getCollection()) {
			if (openNode.getState().compare(state) && openNode.getCost() < cost) {
				return true;
			}
		}
		for (GpsNode closedNode : explored) {
			if (closedNode.getState().compare(state)
					&& closedNode.getCost() < cost) {
				return true;
			}
		}
		return false;
	}

	private  boolean checkBranch(GpsNode parent, GpsState state) {
		if (parent == null) {
			return false;
		}
		return checkBranch(parent.getParent(), state)
				|| state.compare(parent.getState());
	}

	public abstract  void addNode(GpsNode node);

	public boolean isInformed() {
		return strategy.equals(SearchStrategy.AStar) || strategy.equals(SearchStrategy.GREEDY);
	}
	
	public Frontier getFrontier(){
		return frontier;
	}
	
	public SearchStrategy getStrategy(){
		return this.strategy;
	}
	
	protected Set<GpsNode> getExplored() {
		return explored;
	}
}
