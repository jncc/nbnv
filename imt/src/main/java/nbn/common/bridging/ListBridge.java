
package nbn.common.bridging;

import java.util.List;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :- The list bridge will bridge all the elements in the list
* specified in the conversion method on the fly, that is when they are requested
* from the list
*/
public class ListBridge<I,O> implements Bridge<List<? extends I>,List<O>>{
    private Bridge<I,O> conversionBridge;

    /**
     * The constructor of the List Bridge who can convert List of type I to List of type O
     * @param conversionBridge The conversion bridge which will be used to convert
     * all the underlying elements of a list to convert, on the fly.
     */
    public ListBridge(Bridge<I,O> conversionBridge) {
        this.conversionBridge = conversionBridge;
    }

    /**
     *
     * @param toConvert The list to convert
     * @return A list of type O based on the specified list of type I (toConvert)
     */
    public List<O> convert(List<? extends I> toConvert) {
        return new InlineConvertingList<I,O>(toConvert,conversionBridge);
    }
}
