package uk.gov.nbn.data.gis.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Christopher Johnson
 */
@Controller
public class WelcomePageController {
    @Autowired @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping;
    
    @RequestMapping("/")
    public ModelAndView produceWelcomePage() {
        List<Map<String, Object>> methods = new ArrayList<Map<String, Object>>();
        
        for(Entry<RequestMappingInfo, HandlerMethod> mapServiceMethod : handlerMapping.getHandlerMethods().entrySet()) {
            Map<String, Object> method = new HashMap<String, Object>();
            
            MethodParameter[] parameters = mapServiceMethod.getValue().getMethodParameters();
            method.put("service", mapServiceMethod.getKey().getPatternsCondition().toString());
            method.put("queryParams",getQueryParams(parameters));
            method.put("affectedByUser", isAffectedByUser(parameters));
            methods.add(method);
        }
            
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("mapMethods", methods);
        return new ModelAndView("welcome", data);
    }
    
    
    private boolean isAffectedByUser(MethodParameter[] parameters) {
        for(MethodParameter currParameter: parameters) {
            if(currParameter.getParameterType().equals(User.class)) {
                return true;
            }
        }
        return false;
    }
    
    private List<RequestParam> getQueryParams(MethodParameter[] parameters) {
        List<RequestParam> toReturn = new ArrayList<RequestParam>();
        for(MethodParameter currParameter: parameters) {
            RequestParam queryParam = currParameter.getParameterAnnotation(RequestParam.class);
            if(queryParam != null) {
                toReturn.add(queryParam);
            }
        }
        return toReturn;
    }
}
