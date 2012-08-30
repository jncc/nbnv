/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.util.servlet;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.bridging.MapBridge;

/**
 *
 * @author Administrator
 */
public class ParameterHelper {
    public static Map<Parameter, String> getParametersAsParamValueMap(Map<String,String[]> parameters) {
        return new MapBridge<String,String[],Parameter,String>(new ParameterNameBridge(), new ParameterValueBridge()).convert(parameters);
    }

    public static Map<Parameter, String> getParametersAsParamValueMap(String queryString) {
        Map<Parameter,String> toReturn = new HashMap<Parameter, String>();
        for(String paramValuePair : queryString.split("&")) {
            String[] paramValuePairSplit = paramValuePair.split("=");
            toReturn.put(new Parameter(paramValuePairSplit[0]),paramValuePairSplit[1]);
        }
        return toReturn;
    }

    public static String createQueryString(Map<Parameter,String> parameterMap) {
        if(parameterMap.isEmpty())
            return "";
        else {
            StringBuilder toReturn = new StringBuilder("?");
            for(Entry<Parameter, String> entry: parameterMap.entrySet()) {
                toReturn.append(entry.getKey().getParameterName());
                toReturn.append('=');
                toReturn.append(entry.getValue());
                toReturn.append('&');
            }
            return toReturn.substring(0, toReturn.length()-1); //remove the trailing ampersand
        }
    }

    public static Entry<Parameter,String> getFirstMatchingParameterValueEntry(Map<Parameter,String> parameterMapToSearch, Parameter... params) {
        for(Parameter currParam : params) {
            if(parameterMapToSearch.containsKey(currParam))
                return new AbstractMap.SimpleEntry<Parameter,String>(currParam, parameterMapToSearch.get(currParam));
        }
        return null;
    }

    private static class ParameterNameBridge implements Bridge<String,Parameter> {
        public Parameter convert(String toConvert) throws BridgingException {
            return new Parameter(toConvert);
        }
    }
    
    private static class ParameterValueBridge implements Bridge<String[],String> {
        public String convert(String[] toConvert) throws BridgingException {
            return toConvert[0];
        }
    }
}
