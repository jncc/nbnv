package nbn.common.pathfinding;

import java.util.Collection;
/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- The interface which defines a Dijkstra Node. A node can be navigated to and from
*
* @see DijkstraLink
*/
public interface DijkstraNode<E extends DijkstraLink> {
    /**
    * Gets the Unique ID of this DijkstraNode. It is should be unique within a DijkstraMap
    * @see DijkstraMap#addDijkstraNode(N)
    * @return The Unique ID of this DijkstraNode
    */
    public int getID();

    /**
    * Gets a collection of links which this DijkstraNode is the origin of. The ordering of the collection is irrelevant
    * @return A collection of Dijkstra Links this DijkstraNode is connected to
    */
    public Collection<E> getConnectingLinks();
}