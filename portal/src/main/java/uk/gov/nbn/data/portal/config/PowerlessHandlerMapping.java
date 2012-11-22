package uk.gov.nbn.data.portal.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import uk.gov.nbn.data.powerless.request.TraditionalHttpRequestParametersHashModel;

/**
 * The following Spring MVC Request handler will register each of the powerless
 * templates to a path relative to the root.
 * @author Christopher Johnson
 */
public class PowerlessHandlerMapping extends AbstractUrlHandlerMapping {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    
    @PostConstruct
    public void registerPowerlessMappings() {     
        //recurse over the powerless folder
        File root = new File(getServletContext().getRealPath("/WEB-INF/ftl/powerless"));
        addChildren(root, root.getAbsolutePath().length());
    }
    
    private void addChildren(File powerlessRoot, int startOfPath) {
        for(File currChild : powerlessRoot.listFiles()) {
            if(currChild.isDirectory()) {
                addChildren(currChild, startOfPath);
            }
            
            if(currChild.getName().equals("index.ftl")) {
                String toRegister = powerlessRoot.getAbsolutePath()
                                                 .substring(startOfPath)
                                                 .replace("\\", "/");

                registerHandler(getRequestMapping(toRegister),
                                new PowerlessController(toRegister));
            }
        }
    }
    
    private static class PowerlessController implements Controller {
        private static final String POWERLESS_URL_PARAMETERSATION_KEY = "URLParameters";
        private static final String POWERLESS_REQUEST_PARAMETERS_KEY = "RequestParameters";
        
        private final String path;
        public PowerlessController(String path) {
            this.path = path;
        }
        
        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put(POWERLESS_URL_PARAMETERSATION_KEY, PATH_MATCHER.extractUriTemplateVariables(getRequestMapping(path), request.getRequestURI()));
            data.put(POWERLESS_REQUEST_PARAMETERS_KEY, new TraditionalHttpRequestParametersHashModel(request));
            return new ModelAndView("powerless" + path + "/index",data);
        }
    }
    
    private static String getRequestMapping(String path) {
        return (path.isEmpty()) ? "/" : path;
    }
}
