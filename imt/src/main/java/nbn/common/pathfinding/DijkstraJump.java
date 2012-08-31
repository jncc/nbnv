package nbn.common.pathfinding;

import java.util.Collection;
import java.util.ArrayList;
/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- A helper class for the DijkstraMap. This represents a dijkstra jump within a path in a dijkstra Map.
*
* This class is very much tied to the DijkstraMap implementation.
*
* This class is not intended to be referenced from outside this package, hence the use of default access. 
*/
class DijkstraJump<N extends DijkstraNode<L>, L extends DijkstraLink<N>> implements Comparable<DijkstraJump<N,L>> {
    private DijkstraJump<N,L> parent;
    private L linkToParent;
    private N startingNode;

    public DijkstraJump(DijkstraJump<N,L> parent, N startingNode, L linkToParent) {
	this.parent = parent;
	this.startingNode = startingNode;
	this.linkToParent = linkToParent;
    }

    /** Returns a collection of DijkstraLinks which are in the correct order to enable one to traverse the path
    * @return A collection of DijkstraLinks which represent a path. An empty collection is returned if this DijkstraJump represents a link between a DijkstraNode and itself
    */
    public Collection<L> createPathFromRoot() {
	ArrayList<L> path = new ArrayList<L>();
	if(parent!=null) {
		path.addAll(parent.createPathFromRoot());
		path.add(linkToParent);
	}
	return path;
    }

    /** Gets the starting node for this section of the path
    * @return The DijkstraNode which this section of the path starts at
    */
    public N getStartingNode() {
	return startingNode;
    }

    /**
    * Gets the weight total weight from this section of the path, to the root of the path
    * @return The total weight of the path from the root to this section
    */
    public int getDistanceToRoot() {
	if(parent!=null)
	    return linkToParent.getLinkWeight() + parent.getDistanceToRoot();
	else
	    return 0;
    }

    /**
    * Two DijkstraJumps are considered equal if they have the same starting node.
    * @param o The object to compare this one to.
    * @return True if the input object is a DijsktraJump which shares the same starting node as this DijkstraJump
    */
    public boolean equals(Object o) {
	if(o instanceof DijkstraJump) {
	    DijkstraJump toCompare = (DijkstraJump)o;
	    return startingNode.equals(toCompare.startingNode);
	}
	return false;
    }

    /**
    * Compares this DijkstraJump to another in terms of total weight.
    * @param o The DijkstraJump to compare this DijkstraJump to.
    * @see #getDistanceToRoot()
    * @return less than 0, 0 or greater than 0 if this object has a total weight which is less than, equal to, or greater than, respectively, to that of the input object.
    */
    public int compareTo(DijkstraJump<N,L> o) {
	return getDistanceToRoot()-o.getDistanceToRoot();
    }
}