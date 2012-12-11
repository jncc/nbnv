package uk.gov.nbn.data.portal.controllers;


import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * The following Controller handles logging into and out of the NBN Gateway data
 * API
 * forward the cookie token to the browser
 * @author Christopher Johnson
 */
@Controller
public class SSOController {
    @Autowired WebResource resource; 
    
    @RequestMapping(value = "/User/SSO/Unauthorized", method = RequestMethod.GET)
    public ModelAndView processUnauthorized(
            @RequestParam(value="redirect", defaultValue="/") String redirect) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("redirect", redirect);
        model.put("status", "You need to be logged in to view this page.");
        return new ModelAndView("sso", model);
    }
    
    @RequestMapping(value = "/User/SSO/Login", method = RequestMethod.GET)
    public ModelAndView processRequest(
            @RequestParam(value="redirect", defaultValue="/") String redirect) {
        return new ModelAndView("sso", "redirect", redirect);
    }
    
    @RequestMapping(value = "/User/SSO/Login", method = RequestMethod.POST) 
    public ModelAndView login(  @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam(value="redirect", defaultValue="/") String redirect,
                                @RequestParam(value="remember", required=false) String remember,
                                HttpServletResponse response
            ) throws IOException, ServletException, JSONException {
        GenericType<Map<String, Object>> type = new GenericType<Map<String, Object>>(){};
        ClientResponse clientResponse = resource.path("/user/login")
                                                .queryParam("username", username)
                                                .queryParam("password", password)
                                                .queryParam("remember", remember != null ? remember : Boolean.toString(false) )
                                                .accept(MediaType.APPLICATION_JSON)
                                                .get(ClientResponse.class);
        
        Map<String, Object> body = new HashMap<String,Object>(clientResponse.getEntity(type));
        if((Boolean)body.get("success")) {
            response.addHeader("Set-Cookie", clientResponse.getHeaders().getFirst("Set-Cookie"));
            response.sendRedirect(redirect);
            return null;
        }
        else {
            body.put("redirect", redirect); //maintain any redirect request on second attempt
            return new ModelAndView("sso", body);
        }
    }
    
    @RequestMapping(value = "/User/SSO/Logout", method = RequestMethod.GET) 
    public String logout( HttpServletRequest request, 
                        HttpServletResponse response
            ) throws IOException, ServletException {
        ClientResponse clientResponse = resource.path("/user/logout")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .get(ClientResponse.class);
        response.addHeader("Set-Cookie", clientResponse.getHeaders().getFirst("Set-Cookie"));
        return "redirect:/";
    }
    
}
