/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import uk.gov.nbn.data.powerless.BreadcrumbsHelper;
import uk.gov.nbn.data.powerless.json.CookiePassthrough;
import uk.gov.nbn.data.powerless.json.JSONReaderForFreeMarker;

/**
 * The following class will register the request specific objects to the current
 * model view for use in templates
 * @author Christopher Johnson
 */
public class PowerlessRequestSpecificsInterceptor extends HandlerInterceptorAdapter {   
    @Autowired ApplicationContext context;

    @Override
    public void postHandle(
		HttpServletRequest request, HttpServletResponse response, 
		Object handler, ModelAndView modelAndView)
		throws Exception {
        
        if(!(handler instanceof ResourceHttpRequestHandler) && modelAndView != null) {
            modelAndView.addObject("json", new JSONReaderForFreeMarker(new CookiePassthrough(request, response)));     
            modelAndView.addObject("breadcrumbs", BreadcrumbsHelper.getBreadcrumbs(
                    context.getBeansOfType(HandlerMapping.class).values(), request));     
        }
    }
}
