package nbn.common.searching;

import nbn.common.util.Pair;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 23-Nov-2010
* @description	    :- The following class defines a search term match pair.
* As it is a Pair, it adapts the Pair class
*/
public class SearchMatch<M> {
    private Pair<String,M> searchMatchPair;

    public SearchMatch(String matchedTerm, M match) {
	searchMatchPair = new Pair<String,M>(matchedTerm,match);
    }

    /**
     * Gets the full term which resulted in the underlying match being a match
     * @return The full search term which result in the underlying match of this object, being a match.
     */
    public String getMatchedTerm() {
	return searchMatchPair.getA();
    }

    /**
     * This method will get the matched object which was a match because of the underlying search term of this object
     * @return the matched object
     */
    public M getMatch() {
	return searchMatchPair.getB();
    }
}
