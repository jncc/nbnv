package nbn.common.pathfinding;

import java.util.Comparator;
/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- This class enables ordering of DijkstraNodes based on the ID value
*
* @see DijkstraNode
*/
public class DijkstraNodeIDComparator implements Comparator<DijkstraNode> {
	/**
	* Compares two DijkstraNodes based on their ID value.
	* @param o1 One of the DijkstraNodes to compare
	* @param o2 The other DijkstraNode to compare
	* @return less than 0, 0 or greater than 0 if DijkstraNode o1 has an ID which is less than, equal to, or greater than, respectively, to that of DijkstraNode o2.
	*/
	public int compare(DijkstraNode o1, DijkstraNode o2) {
		return o1.getID() - o2.getID();
	}
}