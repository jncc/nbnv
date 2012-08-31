package nbn.webmapping.params;

import java.util.Map;
import nbn.webmapping.params.complete.CompleteParameterNormaliser;
import nbn.webmapping.params.rational.RationalParameterNormaliser;

public class ParameterNormaliserFactory {
    public static ParameterNormaliser createParameterNormaliser(Map<String,String[]> parameterMap) {
        if(CompleteParameterNormaliser.canCreateInteractiveMapOptionsJSONString(parameterMap))
            return new CompleteParameterNormaliser(parameterMap);
        else if(RationalParameterNormaliser.canCreateInteractiveMapOptionsJSONString(parameterMap))
            return new RationalParameterNormaliser(parameterMap);
        else
            return new NullParameterNormaliser();
    }
}
