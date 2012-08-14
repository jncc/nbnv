package uk.gov.nbn.data.powerless.request;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The following class wraps up a Map of parameter names to values and presents 
 * it as an iterable of HttpRequestParameters. Being traditional, list values in
 * the map will create HttpRequestParameters with duplicate key values.
 * @author Christopher Johnson
 */
public class TraditionalHttpRequestParameterIterable implements Iterable<HttpRequestParameter> {
    private Map<String, Object> parameterMap;
    
    public TraditionalHttpRequestParameterIterable(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }
    
    @Override public Iterator<HttpRequestParameter> iterator() {
        return new TraditionalHttpRequestParameterIterator();
    }
    
    public void writeEncodedParameters(Writer toWriteTo) throws IOException {
        Iterator<HttpRequestParameter> iterator = iterator();
        if(iterator.hasNext()) { //is there any entries in map?
            iterator.next().writeEncodedParameter(toWriteTo); //write first
            while(iterator.hasNext()) { 
                toWriteTo.write("&"); //seperator others
                iterator.next().writeEncodedParameter(toWriteTo); //write others
            }
        }
    }
    
    public String getEncodedParameters() throws IOException {
        StringWriter toReturn = new StringWriter();
        writeEncodedParameters(toReturn);
        return toReturn.toString();
    }
    
    private class TraditionalHttpRequestParameterIterator implements Iterator<HttpRequestParameter> {
        private Iterator<Map.Entry<String, Object>> toWrap;
        
        private Map.Entry<String,Object> currEntry;
        private Iterator<String> currentIterator;
        
        private TraditionalHttpRequestParameterIterator() {
            this.toWrap = parameterMap.entrySet().iterator();
        }
        
        @Override public boolean hasNext() {
            return (currentIterator != null && currentIterator.hasNext()) || toWrap.hasNext();
        }

        @Override public HttpRequestParameter next() {
            if(currentIterator != null) {
                if(currentIterator.hasNext())
                    return new HttpRequestParameter(currEntry.getKey(), currentIterator.next());
                else
                    currentIterator = null;
            }
            
            currEntry = toWrap.next();
            Object toReturn = currEntry.getValue();
            if(toReturn instanceof List) {
                currentIterator = ((List<String>)toReturn).iterator();
                return next();
            }
            else if(toReturn instanceof String)
                return new HttpRequestParameter(currEntry.getKey(), (String)toReturn);
            else
                throw new UnsupportedOperationException("The passed in datatype " + toReturn.getClass() + " cannot be converted to a parameter");
        }

        @Override public void remove() {
            throw new UnsupportedOperationException("Removing of parameters is not possible");
        }
    }
}
