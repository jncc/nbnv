
package nbn.common.bridging;

import java.util.*;
import java.util.Map.Entry;

class InlineConvertingMap<IK,IV,OK,OV> extends AbstractMap<OK,OV>{
    private Map<IK,IV> mapToConvert;
    private Bridge<IK,OK> keyBridge;
    private Bridge<IV,OV> valueBridge;

    public InlineConvertingMap(Map<IK,IV> mapToConvert, Bridge<IK,OK> keyBridge, Bridge<IV,OV> valueBridge) {
        this.mapToConvert = mapToConvert;
        this.keyBridge = keyBridge;
        this.valueBridge = valueBridge;
    }

    @Override
    public Set<Entry<OK, OV>> entrySet() {
        return new InlineConvertingSet<Entry<IK, IV>,Entry<OK, OV>>(mapToConvert.entrySet(), new EntryBridge());
    }

    private class EntryBridge implements Bridge<Entry<IK,IV>,Entry<OK,OV>> {
        public Entry<OK, OV> convert(Entry<IK, IV> toConvert) throws BridgingException {
            return new InlineConvertingMapEntry<IK,OK,IV,OV>(toConvert,keyBridge,valueBridge);
        }
    }
}