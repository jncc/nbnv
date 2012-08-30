package nbn.common.bridging;

public abstract class AbstractNameLookupableBridge<I,O> implements NameLookupableBridge<I,O> {
    public abstract String getLookupableName(I toName);

    private Bridge<I,String> namedLookupBridge = new Bridge<I,String> () {
        public String convert(I toConvert) throws BridgingException {
            return getLookupableName(toConvert);
        }
    };
    
    public Bridge<I,String> getNamedLookupBridge() {
        return namedLookupBridge;
    }
}
