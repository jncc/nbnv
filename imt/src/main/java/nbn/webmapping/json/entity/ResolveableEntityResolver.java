package nbn.webmapping.json.entity;

/**
 *
 * @author Administrator
 */
public interface ResolveableEntityResolver<T> {
    public T resolveEntity(String toResolve) throws EntityResolvingException;
}
