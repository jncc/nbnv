
package nbn.common.bridging;

import java.util.Map;

public class MapBridge<IK,IV,OK,OV> implements Bridge<Map<IK,IV>,Map<OK,OV>> {
    private Bridge<IK,OK> keyBridge;
    private Bridge<IV,OV> valueBridge;

    public MapBridge(Bridge<IK,OK> keyBridge, Bridge<IV,OV> valueBridge) {
        this.keyBridge = keyBridge;
        this.valueBridge = valueBridge;
    }

    public Map<OK, OV> convert(Map<IK, IV> toConvert) throws BridgingException {
        return new InlineConvertingMap<IK,IV,OK,OV>(toConvert,keyBridge,valueBridge);
    }
}
