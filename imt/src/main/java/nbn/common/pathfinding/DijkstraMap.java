package nbn.common.pathfinding;

import java.util.*;

/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- A store for all the Dijkstra Nodes which are interconnected via Dijkstra Links
*
* Thread safety has been considered to some extent within this class.
*/
public class DijkstraMap<N extends DijkstraNode<L>, L extends DijkstraLink<N>> {
    private TreeMap<N,N> knownPlaces;
    /**
    * Creates a new DijkstraMap
    */
    public DijkstraMap() {
	knownPlaces = new TreeMap<N,N>(new DijkstraNodeIDComparator());
    }

    /**
    * Adds a Dijkstra Node to this map.
    * Please note that all Dijkstra Nodes which linked to from other Dijkstra Nodes should be added to this map before a 'getShortestPath' can be validly called.
    * @param toAdd The DijkstraNode to add to this Dijkstra Map
    * @see #getShortestPath(N,N)
    * @throws DijkstraNodeAlreadyKnownException if a DijkstraNode has already been added which contains the same ID as this one which is to be added
    */
    public synchronized void addDijkstraNode(N toAdd) throws DijkstraNodeAlreadyKnownException {
	if(knownPlaces.containsKey(toAdd))
	    throw new DijkstraNodeAlreadyKnownException("A DijkstraNode has already been added to this map which has the same ID as the one attempted to be added");
	knownPlaces.put(toAdd, toAdd); //okay to just add
    }

    private synchronized N getLocalDijkstraNode(N toGet) throws UnknownDijkstraNodeException {
	N toReturn = knownPlaces.get(toGet);
	if(toReturn == null)
	    throw new UnknownDijkstraNodeException("This Dijkstra map does not know of a DijkstraNode which has an id of : " + toGet.getID());
	return toReturn; //okay to just return
    }

    /**
    * This method will return the shortest path between two given DijkstraNodes. The implementation uses Dijkstras algorithm for determining the shortest path.
    * It should be noted that all known Dijkstra Nodes should be added to this map before this method is called.
    * If this is not done, a UnknownDijkstraNodeException will likely be thrown.
    * @param startNode The originating node which a path should be found from
    * @param endNode The node which is attempted to be reached
    * @throws NoPossiblePathException If it is not possible to link these two nodes together
    * @throws UnknownDijkstraNodeException If either the startNode can not be found or a node connecting from it cannot be found
    */
    public synchronized Collection<L> getShortestPath(N startNode, N endNode) throws NoPossiblePathException, UnknownDijkstraNodeException {
	N localStartNode = getLocalDijkstraNode(startNode); //will throw an exception if does not exist
	TreeMap<N,DijkstraJump<N,L>> vistableNodes = new TreeMap<N,DijkstraJump<N,L>>(new DijkstraNodeIDComparator()); //to avoid infinite loops, store where I can go
	PriorityQueue<DijkstraJump<N,L>> possibleRoutes = new PriorityQueue<DijkstraJump<N,L>>(); //this will contain the next possible routes

	DijkstraJump<N,L> startingJump = new DijkstraJump<N,L>(null,localStartNode,null);
	vistableNodes.put(localStartNode,startingJump);
	possibleRoutes.add(startingJump);
	while(!possibleRoutes.isEmpty()) {
	    DijkstraJump<N,L> closestElement = possibleRoutes.poll(); //get the first element from possibleRoutes
	    if(endNode.equals(closestElement.getStartingNode())) //is this one the one we want?
		return closestElement.createPathFromRoot(); //return the path between these two systems

	    for(L currLink : closestElement.getStartingNode().getConnectingLinks()) { //go thru all the Links and record a jump if it is the first or shorter than an existing jump
		N localCurrentConnectsToNode = getLocalDijkstraNode(currLink.getEndingNode());
		DijkstraJump<N,L> currJumpToAdd = new DijkstraJump<N,L>(closestElement,localCurrentConnectsToNode,currLink);
		if(!vistableNodes.containsKey(localCurrentConnectsToNode)) { //first time seen, add
		    vistableNodes.put(localCurrentConnectsToNode,currJumpToAdd);
		    possibleRoutes.add(currJumpToAdd);
		}
		else if(currJumpToAdd.compareTo(vistableNodes.get(localCurrentConnectsToNode)) < 0){ //the node is already visitable, is the distance to it shorter than I have seen before
		    vistableNodes.remove(localCurrentConnectsToNode); //remove old path
		    possibleRoutes.remove(currJumpToAdd); //remove old path
		    vistableNodes.put(localCurrentConnectsToNode,currJumpToAdd);
		    possibleRoutes.add(currJumpToAdd);
		}
	    }
	}
	throw new NoPossiblePathException("I cannot create a path between the required locations"); //failed to find a path
    }
}