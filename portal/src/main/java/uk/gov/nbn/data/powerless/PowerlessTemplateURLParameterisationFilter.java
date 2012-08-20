package uk.gov.nbn.data.powerless;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

/**
 * The following Servlet Filter will locate the real path of parameterised URLS
 * and store the key value pairs of the parameters into the 
 * POWERLESS_URL_PARAMETERS_ATTRIBUTE attribute for the servlet request
 * @author Christopher Johnson
 */
public class PowerlessTemplateURLParameterisationFilter implements Filter {
    public static final String POWERLESS_URL_PARAMETERS_ATTRIBUTE =  "uk.gov.nbn.data.powerless.URLParameters";
    
    private ServletContext context;
    
    @Override public void init(FilterConfig filterConfig) {
        context = filterConfig.getServletContext();
    }
    
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {        
        String requestedURL = ((HttpServletRequest)request).getRequestURI();
        
        if(!new File(context.getRealPath(requestedURL)).exists()) {
            String[] requestedParts = requestedURL.substring(1).split("/"); //split the requestedURL removing the first slash
            String[] realParts = findSuitableResourcePathParts(new File(context.getRealPath("/")), requestedParts); //resolve the pathParts to realParts
            
            if(realParts != null) { //get and store parameters, then forward to the found template
                request.setAttribute(POWERLESS_URL_PARAMETERS_ATTRIBUTE, getParameters(realParts, requestedParts));
                context.getRequestDispatcher(createPath(realParts)).forward(request, response);
                return; //don't fall into the normal chain
            }
        }
        chain.doFilter(request, response);
    }
    
    
    /**
     * Creates a Map of parameter to value entries for a list of realPathParts
     *  and those which were initially requested.
     * 
     * WARNING This method does not check that the lengths of the input array 
     *  are equal. This was deemed acceptable as this is not intended to be 
     *  called outside of this class. Unexpected results may occur if array
     *  lengths are not guaranteed to be of the same size.
     * @param realPathParts
     * @param requestedPathParts
     * @return A Map
     */
    private static Map<String,String> getParameters(String[] realPathParts, String[] requestedPathParts) {
        Map<String, String> toReturn = new HashMap<String, String>();
        for(int i=0; i<realPathParts.length; i++) {
            if(ParameterisedFilenameFilter.isParameterisedPath(realPathParts[i])) {
                toReturn.put(ParameterisedFilenameFilter.getParameterNameFromParameterisedPath(realPathParts[i]), requestedPathParts[i]);
            }
        }
        return toReturn;
    }
    
    /**
     * Creates a path by concatenating each element in the array separated by a
     * /
     * @param pathParts
     * @return A path from the parameters
     */
    private static String createPath(String[] pathParts) {
        StringBuilder toReturn = new StringBuilder("/");
        for(String currPathPart : pathParts) {
            toReturn.append(currPathPart);
            toReturn.append("/");
        }
        return toReturn.toString();
    }
    
    /**
     * The following method will initiate a recursive search over the folder 
     * structure for a resource which best matches the requested PathParts.
     * What it finds will be converted into an array of realPathParts which can 
     * be converted to a real path using #createPath
     * @param root The file of the root to search from
     * @param requestedPathParts The Parts of the path as they are received in 
     *  a request 
     * @return The path parts of a real resource on disk or null if no suitable
     *  resource could be found
     */
    private static String[] findSuitableResourcePathParts(File root, String[] requestedPathParts) {
        String[] toReturn = new String[requestedPathParts.length];
        File suitableResource = findSuitableResource(root, 0, requestedPathParts); //start recursion
        if(suitableResource != null) {
            for(int i=requestedPathParts.length-1; i>=0; i--) { //populate the path parts backwards
                toReturn[i] = suitableResource.getName();
                suitableResource = suitableResource.getParentFile(); // iterate to parent
            }
            return toReturn; //return the populated real parts
        }
        else {
            return null; //failed to find Suitable Resource
        }
    }
    
    /**
     * The following method will recurse over the path structure trying to find
     * a path which best matches the one which was requested in the URL
     * @param root The real file of the servlet path
     * @param currIndex The current index of the path to find in path parts.
     * @param pathParts The path parts which were found in the requested URL
     * @return The File object of the best match to the resource or null if 
     *  nothing could be matched
     */
    private static File findSuitableResource(File root, int currIndex, String[] pathParts) {
        if(pathParts.length != currIndex) { //at the end
            for(int i=currIndex; i< pathParts.length; i++) {
                File foundFile = new File(root, pathParts[i]);
                if(foundFile.exists()) { //directory exists look for parameter holder
                    return findSuitableResource(foundFile, currIndex+1, pathParts);
                }
                else {
                    for(File currParameterisedFile : root.listFiles(new ParameterisedFilenameFilter())) {
                        File findSuitableResource = findSuitableResource(currParameterisedFile, currIndex+1, pathParts); //set the next file to the first parameterised value
                        if(findSuitableResource != null) return findSuitableResource;
                    }
                }
            }
            return null; // failed to find. Break out
        }
        else {
            return root; //reached end
        }
    }
    
    /**Start defining what makes a path part a parameter*/
    private static class ParameterisedFilenameFilter implements FilenameFilter {
        @Override public boolean accept(File dir, String name) {
            return isParameterisedPath(name);
        }
        
        public static boolean isParameterisedPath(String name) {
            return name.startsWith("{") && name.endsWith("}");
        }
        
        /**
         * Simple method to strip the '{' and the '}' from the input 
         * @param parameter To Strip
         * @return The parameter name with the { and } stripped
         */
        public static String getParameterNameFromParameterisedPath(String parameter) {
            return parameter.substring(1, parameter.length()-1);
        }
    }

    //Do nothing here
    @Override public void destroy() {}
}
