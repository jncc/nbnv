package uk.gov.nbn.data.powerless.json;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classes which extend this class can have all there methods exposed to 
 * freemarker callable using the following syntax
 * <code>
 *  freemarker_var("methodName", param1, param2 ...)
 * </code>
 * @author Chris Johnson
 */
public class WrappedTemplateMethod<T> implements TemplateMethodModel{
    private final T wrapped;
    public WrappedTemplateMethod(T wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Object exec(List list) throws TemplateModelException {
        try {
            if(!list.isEmpty()) {     
                List parameters = new ArrayList(list);
                String methodName = (String)parameters.remove(0); //remove method name
                return wrapped.getClass().getMethod(methodName, getTypes(parameters)).invoke(wrapped, parameters.toArray());
            }
            throw new IllegalArgumentException("No method was specified");
        } catch (IllegalAccessException ex) {
            throw new TemplateModelException(ex);
        } catch (IllegalArgumentException ex) {
            throw new TemplateModelException(ex);
        } catch (InvocationTargetException ex) {
            throw new TemplateModelException(ex.getCause().getMessage(), ex);
        } catch (NoSuchMethodException ex) {
            throw new TemplateModelException(ex);
        } catch (SecurityException ex) {
            throw new TemplateModelException(ex);
        }
    }
    
    private static Class[] getTypes(List instances) {
        Class[] toReturn = new Class[instances.size()];
        for(int i=0; i<instances.size(); i++) {
            toReturn[i] = instances.get(i).getClass();
        }
        return toReturn;
    }
}
