package uk.gov.nbn.data.portal.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * The following Spring MVC Interceptor will record the current HttpServletResponse
 * object into an instance of ThreadLocal which can be accessed by the inner 
 * static class {@link HttpServletResponseFactoryBean} given that both classes 
 * are defined as beans in the same context of spring mvc.
 * 
 * By instantiating {@link HttpServletResponseFactoryBean}, this interceptor and
 * then registering the intercepter to spring mvc using {@link org.springframework.web.servlet.config.annotation.InterceptorRegistry#addInterceptor(org.springframework.web.servlet.HandlerInterceptor) }
 * 
 * Then it will be possible to autowire the HttpServletResponse as a protoype and
 * threads which are processing in the same context of the HttpServletRequest will
 * be able to read and modify that response instance.
 * 
 * @author Christopher Johnson
 */
public class RequestScopedResponseInterceptor extends HandlerInterceptorAdapter {
    private ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();
    
    /** Simply stored the HttpServletResponse into the thread local */
    @Override
    public boolean preHandle(   HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler) throws Exception {
        responses.set(response);
        return super.preHandle(request, response, handler);
    }
    
    /** Removes the thread local reference to the HttpServletResponse */
    @Override
    public void postHandle( HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            ModelAndView modelAndView) throws Exception {
        responses.remove();
    }

    /**
     * The following FactoryBean will return the current executing threads 
     * instance of HttpServletResponse.
     */
    public static class HttpServletResponseFactoryBean implements FactoryBean {
        @Autowired RequestScopedResponseInterceptor interceptor;

        @Override
        public Object getObject() throws Exception {
            return interceptor.responses.get();
        }

        @Override
        public Class getObjectType() {
            return HttpServletResponse.class;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }
}
