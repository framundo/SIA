package gps;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;
import gps.api.InformedFrontier;
import gps.api.NaiveFrontier;
import gps.exception.NotAppliableException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import model.ComparatorProvider;

public abstract class GPSEngine {

	private Frontier frontier;
	private List<GPSNode> explored = new ArrayList<GPSNode>();
	private GPSProblem problem;

	// Use this variable in the addNode implementation
	private SearchStrategy strategy;

	public void engine(GPSProblem myProblem, SearchStrategy myStrategy) {
		System.out.println("Arrancamos");
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

		GPSState rootState = problem.getInitState();
		GPSNode rootNode = new GPSNode(rootState, null, null, 0, problem.getHValue(rootState));
		boolean finished = false;
		boolean failed = false;
		
		frontier.offer(rootNode);
		while (!failed && !finished) {
			if (frontier.isEmpty()) {
				if (myStrategy == SearchStrategy.ID && iterativeDepth == nodeMaxHeight) { // Tengo que aumentar el nivel si o si
					System.out.println(iterativeDepth +" ->" + (iterativeDepth+1));
					iterativeDepth++;
					frontierTotalSize += frontier.size();
					frontier = new NaiveFrontier();
					frontier.offer(rootNode);
				} else {
					failed = true;
				}
			} else {
				GPSNode currentNode = frontier.getNext(); 
				System.out.println("Probando nodo altura " + currentNode.getHeight() + " f vale " + (currentNode.getCost()+currentNode.getHValue()));
				nodeMaxHeight = Math.max(nodeMaxHeight, currentNode.getHeight());
				explored.add(currentNode);
				if (isGoal(currentNode)) {
					finished = true;
					frontierTotalSize += frontier.size();
					System.out.println(currentNode.getSolution());
					System.out.println("Height of the solution: " + currentNode.getHeight());
					System.out.println("Generated nodes: " + (frontierTotalSize + explored.size()));
					System.out.println("Frontier nodes: " + frontierTotalSize);
					System.out.println("Expanded nodes: " + (explored.size() - 1));
					System.out.println("Time elapsed: " + (System.currentTimeMillis() - time0));
				} else {
					if (myStrategy != SearchStrategy.ID || iterativeDepth > currentNode.getHeight()) {
						explode(currentNode);
					}
				}
			}
		}
		if (finished) {
			System.out.println("OK! solution found!");
		} else if (failed) {
			System.err.println("FAILED! solution not found!");
		}
	}

	private  boolean isGoal(GPSNode currentNode) {
		return currentNode.getState().isGoal();
	}

	private  boolean explode(GPSNode node) {
		if(problem.getRules() == null){
			System.err.println("No rules!");
			return false;
		}

		for (GPSRule rule : problem.getRules()) {
			GPSState newState = null;
			try {
				newState = rule.evalRule(node.getState());
			} catch (NotAppliableException e) {
				// Do nothing
			}
			if (newState != null
					&& !checkBranch(node, newState)
					&& !checkOpenAndClosed(node.getCost() + rule.getCost(),
							newState)) {
				GPSNode newNode = new GPSNode(newState, node, rule, node.getCost()
						+ rule.getCost(), problem.getHValue(newState));
				addNode(newNode);
			}
		}
		return true;
	}

	private  boolean checkOpenAndClosed(Integer cost, GPSState state) {
		for (GPSNode openNode : frontier.getCollection()) {
			if (openNode.getState().compare(state) && openNode.getCost() < cost) {
				return true;
			}
		}
		for (GPSNode closedNode : explored) {
			if (closedNode.getState().compare(state)
					&& closedNode.getCost() < cost) {
				return true;
			}
		}
		return false;
	}

	private  boolean checkBranch(GPSNode parent, GPSState state) {
		if (parent == null) {
			return false;
		}
		return checkBranch(parent.getParent(), state)
				|| state.compare(parent.getState());
	}

	public abstract  void addNode(GPSNode node);

	public boolean isInformed() {
		return strategy.equals(SearchStrategy.AStar) || strategy.equals(SearchStrategy.GREEDY);
	}
	
	public Frontier getFrontier(){
		return frontier;
	}
	
	public SearchStrategy getStrategy(){
		return this.strategy;
	}
}
