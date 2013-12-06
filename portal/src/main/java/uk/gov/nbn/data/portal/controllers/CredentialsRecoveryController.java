package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.controllers.models.ChangePassword;

/**
 *
 * @author Developer
 */
@Controller
public class CredentialsRecoveryController {
    @Autowired WebResource resource; 
    
    @RequestMapping(value = "/User/Recovery", method = RequestMethod.GET)
    public ModelAndView processRequest() {
        return new ModelAndView("recovery-form", "recoveryRequest", new RecoveryRequest());
    }
    
    @RequestMapping(value = "/User/Recovery", method = RequestMethod.POST)
    public ModelAndView registerUser(@Valid RecoveryRequest recovery, BindingResult result) {
        if(!result.hasErrors()) {
            ClientResponse response;
            
            if(recovery.getForgotten().equals("password")) {
                response = resource.path("user/mail/password").post(ClientResponse.class,recovery.getEmail());
            }
            else {
                response = resource.path("user/mail/username").post(ClientResponse.class,recovery.getEmail());
            }
            
            if (response.getClientResponseStatus().equals(ClientResponse.Status.NOT_FOUND)) {                
                recovery.setErrorOutput("No user exists with this email address, please check that the address is correct and resubmit the password recovery form");
                return new ModelAndView("recovery-form", "recoveryRequest", recovery);
            } else {
                //Go to the recovery-success page regardless of if it was successful
                return new ModelAndView("recovery-success");
            }
        }
        else {
            return new ModelAndView("recovery-form", "recoveryRequest", recovery);
        }
    }
    
    @RequestMapping(value = "/User/Recovery/Password", method = RequestMethod.GET)
    public ModelAndView getChangePasswordForm(
            @RequestParam(required=true, value="reset") String token
            ) {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setToken(token);
        return new ModelAndView("recovery-password-form", "changePassword", changePassword);
    }
    
    @RequestMapping(value = "/User/Recovery/Password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid ChangePassword changePassword, BindingResult result) {
        if(!result.hasErrors()) {
            MultivaluedMap data = new MultivaluedMapImpl();
            data.add("reset", changePassword.getToken());
            data.add("password", changePassword.getPassword());
            resource.path("user/passwords/change/")
                    .path(changePassword.getUsername())
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .post(ClientResponse.class, data);
            
            return new ModelAndView("password-change-success");
        }
        else {
            return new ModelAndView("recovery-password-form", "changePassword", changePassword);
        }
    }
    
    public static class RecoveryRequest {
        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        @Pattern(regexp = "(username)|(password)")
        private String forgotten;
        
        private String errorOutput = "";

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getForgotten() {
            return forgotten;
        }

        public void setForgotten(String forgotten) {
            this.forgotten = forgotten;
        }

        public String getErrorOutput() {
            return errorOutput;
        }

        public void setErrorOutput(String errorOutput) {
            this.errorOutput = errorOutput;
        }
    }
}
