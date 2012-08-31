
package nbn.common.bridging;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

class InlineConvertingSet<I,O> extends AbstractSet<O> {
    private InlineConvertingList<I,O> listBasedUpon;
    public InlineConvertingSet(Set<I> setToConvert, Bridge<I,O> bridge) {
        listBasedUpon = new InlineConvertingList<I,O>(new ArrayList<I>(setToConvert), bridge);
    }

    @Override
    public Iterator<O> iterator() {
        return listBasedUpon.iterator();
    }

    @Override
    public int size() {
        return listBasedUpon.size();
    }
}
