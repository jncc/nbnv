package uk.gov.nbn.data.gis;

import edu.umn.gis.mapscript.OWSRequest;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscript;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The following Servlet wraps up MapServer as a Servlet ready for requests to be
 * made
 * @author Christopher Johnson
 */
public class MapServerServlet extends HttpServlet {
    private static FileFilter DIRECTORY_FILE_FILTER = new DirectoryFileFilter();
    private static FilenameFilter MAP_FILENAME_FILTER = new MapServerMapFilenameFilter();
    private static String MAPS_LOCATION = "WEB-INF\\maps";
    private Map<String,String> mapFiles;
    
    @Override public void init(ServletConfig config) throws ServletException {
        super.init(config);
        File mapFileDirectory = new File(config.getServletContext().getRealPath(MAPS_LOCATION)); //get the map directory
        mapFiles = loadMapFiles("", mapFileDirectory); //load up all the map files
    }
    
    @Override protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        String requestedMapFile = request.getPathInfo();
        if(mapFiles.containsKey(requestedMapFile)) {
            ServletOutputStream out = response.getOutputStream();
            try {
                mapscript.msIO_installStdoutToBuffer(); //buffer the bytes of the map script
                
                mapObj map = new mapObj(mapFiles.get(requestedMapFile));                
                
                int owsResult = map.OWSDispatch( createMapRequest(request) );

                if( owsResult != 0 ) {
                    throw new ServletException("OWSDispatch failed. (expect 0): " + owsResult);
                }

                response.setContentType(mapscript.msIO_stripStdoutBufferContentType()); //pass the content type

                out.write(mapscript.msIO_getStdoutBufferBytes()); //output the bytes to the end user
            }catch(Throwable mapEx) {
                throw new ServletException("An error occured whilst generating the map server request", mapEx);
            } finally {            
                out.close();
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private static OWSRequest createMapRequest(HttpServletRequest request) {
        OWSRequest toReturn = new OWSRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for(Entry<String, String[]> currParam : parameterMap.entrySet()) {
            for(String currValue : currParam.getValue()) {
                toReturn.setParameter(currParam.getKey(), currValue);
            }
        }
        return toReturn;
    }
    
    /**
     * The following method will obtain all the maps in the specified directory 
     * and store them in a map
     * @param prefix A prefix to store in front of the map name
     * @param toSearch Directory to search in
     * @return A Map of PathInfo map name to real location
     */
    private static Map<String,String> loadMapFiles(String prefix, File toSearch) {
        Map<String, String> toReturn = new HashMap<String,String>();
        for(File currDirectory : toSearch.listFiles(DIRECTORY_FILE_FILTER)) {
            toReturn.putAll(loadMapFiles(prefix + "/" + currDirectory.getName(), currDirectory));
        }
        
        for(File currMapFile : toSearch.listFiles(MAP_FILENAME_FILTER)) {
            toReturn.put(prefix + "/" + MapServerMapFilenameFilter.getMapName(currMapFile.getName()), currMapFile.getAbsolutePath());
        }
        
        return toReturn;
    }
    
    /**Simple utility class for deciding weather a file is a folder or not*/
    private static class DirectoryFileFilter implements FileFilter {
        @Override public boolean accept(File file) {
            return file.isDirectory();
        }
    }
}
