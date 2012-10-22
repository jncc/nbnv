package uk.gov.nbn.data.gis.processor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following bean will aid in the creation of the gis system helper page
 * @author Christopher Johnson
 */
@Component
public class WelcomePageHelper {
    @Autowired MethodArgumentFactory methodArgumentFactory;
    @Autowired MapServiceMethodFactory serviceFactory;
    
    private final Template welcomeTemplate;
    
    public WelcomePageHelper() throws IOException {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(getClass(), "/");
        this.welcomeTemplate = config.getTemplate("welcome.ftl");
    }
    
    public void produceWelcomePage(HttpServletResponse servletResponse) throws TemplateException, IOException {
        servletResponse.setContentType("text/html");
        PrintWriter out = servletResponse.getWriter();
        try {
            List<Map<String, Object>> methods = new ArrayList<Map<String, Object>>();
            for(MapServiceMethod mapServiceMethod : serviceFactory.getMapServiceMethodsRegistered()) {
                Map<String, Object> method = new HashMap<String, Object>();
                MethodArgumentFactory.Argument[] arguments = methodArgumentFactory.getArguments(mapServiceMethod.getUnderlyingMapMethod());
                method.put("service", mapServiceMethod);
                method.put("queryParams",getQueryParams(arguments));
                method.put("affectedByUser", isAffectedByUser(arguments));
                methods.add(method);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("mapMethods", methods);
            welcomeTemplate.process(data, out);
        }
        finally {
            out.close();
        }
    }
    
    private boolean isAffectedByUser(MethodArgumentFactory.Argument[] arguments) {
        for(MethodArgumentFactory.Argument currArgument: arguments) {
            if(currArgument.getParameterClass().equals(User.class)) {
                return true;
            }
        }
        return false;
    }
    
    private List<QueryParam> getQueryParams(MethodArgumentFactory.Argument[] arguments) {
        List<QueryParam> toReturn = new ArrayList<QueryParam>();
        for(MethodArgumentFactory.Argument currArgument: arguments) {
            QueryParam queryParam = currArgument.getAnnotationMap().get(QueryParam.class);
            if(queryParam != null) {
                toReturn.add(queryParam);
            }
        }
        return toReturn;
    }
}
