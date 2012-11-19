/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import uk.gov.nbn.data.powerless.json.CookiePassthrough;
import uk.gov.nbn.data.powerless.json.JSONReaderForFreeMarker;

/**
 * The following class will register the request specific objects to the current
 * model view for use in templates
 * @author Christopher Johnson
 */
public class PowerlessRequestSpecificsInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(
		HttpServletRequest request, HttpServletResponse response, 
		Object handler, ModelAndView modelAndView)
		throws Exception {
        
        if(!(handler instanceof ResourceHttpRequestHandler) && modelAndView != null) {
            modelAndView.addObject("json", new JSONReaderForFreeMarker(new CookiePassthrough(request, response)));
        }
    }
}
