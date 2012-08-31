
package nbn.common.util.servlet;

/*
 * This class ensures that the parameters which the proxy deals with are not dependant on case
 */
public class Parameter {
    private String name;

    public Parameter(String name) {
        this.name = name;
    }
    
    public String getParameterName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Parameter) {
            Parameter toCompare = (Parameter)o;
            return toCompare.name.equalsIgnoreCase(name);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
