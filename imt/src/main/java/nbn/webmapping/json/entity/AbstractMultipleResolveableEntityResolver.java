package nbn.webmapping.json.entity;

import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;

/**
 *
 * @author Administrator
 */
public abstract class AbstractMultipleResolveableEntityResolver implements ResolveableEntityResolver<JSONArray> {

    public abstract JSONArray resolveEntity(List<String> toResolve) throws EntityResolvingException;

    public final JSONArray resolveEntity(String toResolve) throws EntityResolvingException {
        return resolveEntity(Arrays.asList(toResolve.split(",")));
    }
}
