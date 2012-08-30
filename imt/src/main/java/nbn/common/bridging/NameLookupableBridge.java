package nbn.common.bridging;

/**
 *
 * @author Administrator
 */
public interface NameLookupableBridge<I,O> extends Bridge<I,O> {
    public Bridge<I,String> getNamedLookupBridge();
}
