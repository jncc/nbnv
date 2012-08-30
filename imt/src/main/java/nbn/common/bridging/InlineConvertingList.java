
package nbn.common.bridging;

import java.util.AbstractList;
import java.util.List;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :-
*/
class InlineConvertingList<I,O> extends AbstractList<O> {
    private List<? extends I> listToConvert;
    private Bridge<I,O> conversionBridge;

    public InlineConvertingList(List<? extends I> listToConvert, Bridge<I,O> conversionBridge) {
	this.listToConvert = listToConvert;
	this.conversionBridge = conversionBridge;
    }

    @Override
    public O get(int index) {
	return conversionBridge.convert(listToConvert.get(index));
    }

    @Override
    public int size() {
	return listToConvert.size();
    }
}
