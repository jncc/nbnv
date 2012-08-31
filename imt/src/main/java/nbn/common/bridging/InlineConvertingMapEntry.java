
package nbn.common.bridging;

import java.util.Map.Entry;

class InlineConvertingMapEntry<IK,OK,IV,OV> implements Entry<OK,OV> {
    private Entry<IK,IV> toConvert;
    private Bridge<IK,OK> keyBridge;
    private Bridge<IV,OV> valueBridge;

    public InlineConvertingMapEntry(Entry<IK,IV> toConvert, Bridge<IK,OK> keyBridge, Bridge<IV,OV> valueBridge) {
        this.toConvert = toConvert;
        this.keyBridge = keyBridge;
        this.valueBridge = valueBridge;
    }

    public OK getKey() {
        return keyBridge.convert(toConvert.getKey());
    }

    public OV getValue() {
        return valueBridge.convert(toConvert.getValue());
    }

    public OV setValue(OV value) {
        throw new UnsupportedOperationException("Optional operation, unsupported");
    }
}
