package nbn.webmapping.params;

/**
 *
 * @author Administrator
 */
public interface ParameterNormaliser {
    public String createInteractiveMapOptionsJSONString() throws ParameterNormalisationException;
}
