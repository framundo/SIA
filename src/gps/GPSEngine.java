package gps;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;
import gps.exception.NotAppliableException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Board;

public abstract class GPSEngine {

	private LinkedList<GPSNode> open = new LinkedList<GPSNode>();

	private List<GPSNode> closed = new ArrayList<GPSNode>();

	private GPSProblem problem;

	// Use this variable in the addNode implementation
	private SearchStrategy strategy;

	public void engine(GPSProblem myProblem, SearchStrategy myStrategy) {

		long time0 = System.currentTimeMillis();
		problem = myProblem;
		strategy = myStrategy;
		int iterativeDepth = 1;
		int openSize = 0;

		GPSNode rootNode = new GPSNode(problem.getInitState(), null, 0);
		boolean finished = false;
		boolean failed = false;

		open.add(rootNode);
		while (!failed && !finished) {
			if (open.isEmpty()) {
				if (myStrategy == SearchStrategy.ID && iterativeDepth < Board.MAX_MOVEMENTS) { // Tengo que aumentar el nivel si o si
					iterativeDepth++;
					openSize += open.size();
					open = new LinkedList<GPSNode>();
					open.add(rootNode);
				} else {
					failed = true;
				}
			} else {
				GPSNode currentNode = open.get(0);
				closed.add(currentNode);
				open.remove(0);
				if (isGoal(currentNode)) {
					finished = true;
					openSize += open.size();
					System.out.println(currentNode.getSolution());
					System.out.println("Height of the solution: " + currentNode.getHeight());
					System.out.println("Generated nodes: " + (openSize + closed.size()));
					System.out.println("Frontier nodes: " + openSize);
					System.out.println("Expanded nodes: " + (closed.size() - 1));
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
				GPSNode newNode = new GPSNode(newState, rule, node.getCost()
						+ rule.getCost());
				newNode.setParent(node);
				addNode(newNode);
			}
		}
		return true;
	}

	private  boolean checkOpenAndClosed(Integer cost, GPSState state) {
		for (GPSNode openNode : open) {
			if (openNode.getState().compare(state) && openNode.getCost() < cost) {
				return true;
			}
		}
		for (GPSNode closedNode : closed) {
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

	public LinkedList<GPSNode> getOpened(){
		return this.open;
	}

	public SearchStrategy getStrategy(){
		return this.strategy;
	}
}
