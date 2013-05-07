package uk.gov.nbn.data.portal.jersey;

import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The following client filter wraps up an instance of {@link JerseyCookieClientHandler}
 * and presents it as a JerseyClientFilter.
 * 
 * It is necessary to split the ClientFilter and it's processing into two instances.
 * By doing this, ordering of filters can be handled by a fixed instance of 
 * JerseyCookieClientFilter where as the Spring handled dynamic proxy of 
 * JerseyCookieClientHandler can have its backing implementation dropped and 
 * recreated without affecting the wiring of Jersey.
 * 
 * @author Christopher Johnson
 */
public class JerseyCookieClientFilter extends ClientFilter {
    private JerseyCookieClientHandler handler;
        
    public JerseyCookieClientFilter(JerseyCookieClientHandler handler) {
        this.handler = handler;
    }

    /**
     * Handle the ClientRequest by delegating the processing to the instance of 
     * JerseyCookieClientHandler
     * @param cr to handle
     * @return A client response handled by the JerseyCookieClientHandler
     * @throws ClientHandlerException {@link JerseyCookieClientHandler#handle}
     */
    @Override
    public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
        return handler.handle(getNext(), cr);
    }
    
    /**
     * The intension is that {@link JerseyCookieClientHandler} is dynamically proxied 
     * by Spring in order to autowire the Servlet request specific objects request 
     * and response. This object must be 
     */
    public static class JerseyCookieClientHandler {
        @Autowired HttpServletRequest request;
        @Autowired HttpServletResponse response;
        
        public ClientResponse handle(ClientHandler next, ClientRequest clientRequest) throws ClientHandlerException {    
            String cookie = request.getHeader("Cookie");
            if(cookie != null) {
                clientRequest.getHeaders().add("Cookie", cookie);
            }

            ClientResponse clientResponse = next.handle(clientRequest);

            String newCookie = clientResponse.getHeaders().getFirst("Set-Cookie");
            if(newCookie != null) {
                response.setHeader("Set-Cookie", newCookie);
            }

            return clientResponse;
        }
    }
}
