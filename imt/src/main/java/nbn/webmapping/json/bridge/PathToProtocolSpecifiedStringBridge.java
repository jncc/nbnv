/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.bridge;

import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;

/**
 *
 * @author Administrator
 */
public class PathToProtocolSpecifiedStringBridge implements Bridge<String,String> {
    private String protocol;
    private boolean appendEndSlash;

    public PathToProtocolSpecifiedStringBridge(String protocol) {
        this(protocol, true); //by default append end slash
    }

    public PathToProtocolSpecifiedStringBridge(String protocol, boolean appendEndSlash) {
        this.protocol = protocol;
        this.appendEndSlash = appendEndSlash;
    }

    public String convert(String toConvert) throws BridgingException {
        StringBuilder toReturn = new StringBuilder(protocol);
        toReturn.append("://");
        toReturn.append(toConvert);
        if(appendEndSlash)
            toReturn.append('/');
        return toReturn.toString();
    }

}
