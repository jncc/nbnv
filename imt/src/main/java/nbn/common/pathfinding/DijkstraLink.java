package nbn.common.pathfinding;
/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- The interface which a Link with in a map has to implement inorder to be searched using the Dijkstra algorithm implemented within DijkstraMap
*
* This link is generic, and requires the type of DijkstraNodes which it will connect together
*
* @see DijkstraMap
*/
public interface DijkstraLink<E extends DijkstraNode> {
    /**
    * Gets the weight of this particular link. If there is no prioritisation between links then all links can have the same weight, this will mean that any alternative could be chosen.
    * @return The weight attributed to travelling down this link
    */
    public int getLinkWeight();

    /**
    * Gets the DijkstraNode to which this link connects to and ends up at if this Link is traversed
    * @return The Dijkstra Node which this link connects to.
    */
    public E getEndingNode();

    /**
    * Gets the DijkstraNode to which this link connects to
    * @return The Dijkstra Node which this link is connected to.
    */
    public E getStartingNode();
}