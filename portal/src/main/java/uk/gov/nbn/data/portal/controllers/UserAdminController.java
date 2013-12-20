/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.controllers.models.ChangePassword;
import uk.gov.nbn.data.portal.controllers.models.builders.UserAdminModelBuilder;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
@RequestMapping(value = "/User")
public class UserAdminController {

    @Autowired
    WebResource resource;

    @RequestMapping(value = "Modify", method = RequestMethod.GET)
    public ModelAndView getUserModifyPage() {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (isNotPublicUser(currentUser)) {
            return new UserAdminModelBuilder()
                    .view("userModify")
                    .user(currentUser)
                    .changePassword(currentUser.getUsername())
                    .build();
        }

        return ssoRedirect("/User/Modify", "You need to be logged in to view the user modify page.");
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST)
    public ModelAndView postUserModifyPage(@Valid User modifiedUser, BindingResult result) throws JSONException {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (isNotPublicUser(currentUser)) {
            if (!result.hasErrors()) {
                ClientResponse regResponse = resource.path("user/modify")
                        .type(MediaType.APPLICATION_JSON)
                        .post(ClientResponse.class, modifiedUser);

                if (regResponse.getClientResponseStatus() != ClientResponse.Status.OK) {
                    JSONObject entity = regResponse.getEntity(JSONObject.class);
                    result.addError(new ObjectError(result.getObjectName(), entity.getString("status")));
                }
            }

            String successMessage = "";

            if (!result.hasErrors()) {
                successMessage = "User Details have been successfully modified.";
            }

            return new UserAdminModelBuilder()
                    .view("userModify")
                    .user(modifiedUser)
                    .changePassword(currentUser.getUsername())
                    .successMessage(successMessage)
                    .build();
        }

        return ssoRedirect("/User/Modify", "You need to be logged in to view the user modify page.");
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST, params = "changePassword")
    public ModelAndView changePassword(@Valid ChangePassword changePassword, Model model, BindingResult result) throws JSONException {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (isNotPublicUser(currentUser)) {
            if (!result.hasErrors()) {
                MultivaluedMap data = new MultivaluedMapImpl();
                data.add("password", changePassword.getPassword());
                ClientResponse response = resource.path("user/passwords/change")
                        .type(MediaType.APPLICATION_FORM_URLENCODED)
                        .post(ClientResponse.class, data);

                if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
                    JSONObject entity = response.getEntity(JSONObject.class);
                    result.addError(new ObjectError(result.getObjectName(), entity.getString("status")));
                }

                String successMessage = "";

                if (!result.hasErrors()) {
                    successMessage = "You have successfully changed your password. Next time you login to the NBN Gateway please supply your new password.";
                }

                return new UserAdminModelBuilder()
                        .view("userModify")
                        .user(currentUser)
                        .changePassword(currentUser.getUsername())
                        .successMessage(successMessage)
                        .build();
            } else {
                return new UserAdminModelBuilder()
                        .view("userModify")
                        .user(currentUser)
                        .changePassword(changePassword)
                        .build();
            }
        }

        return ssoRedirect("/User/Modify", "You need to be logged in to view the user modify page.");
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST, params = "emailSettings")
    public ModelAndView changeEmailSettings(User user, Model model, BindingResult result) throws JSONException {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (isNotPublicUser(currentUser)) {
            MultivaluedMap data = new MultivaluedMapImpl();
            data.add("subscribedToNBNMarketting", user.isSubscribedToNBNMarketting() ? "1" : "0");

            ClientResponse response = resource.path("user/emailSettings")
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .post(ClientResponse.class, data);

            if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
                JSONObject entity = response.getEntity(JSONObject.class);
                result.addError(new ObjectError(result.getObjectName(), entity.getString("status")));
            }

            String successMessage = "";

            if (!result.hasErrors()) {
                successMessage = "Successfully changed email subscription";
            }

            User updatedUser = resource.path("user/full")
                    .accept(MediaType.APPLICATION_JSON)
                    .get(User.class);

            return new UserAdminModelBuilder()
                    .view("userModify")
                    .user(updatedUser)
                    .changePassword(currentUser.getUsername())
                    .successMessage(successMessage)
                    .build();
        }

        return ssoRedirect("/User/Modify", "You need to be logged in to view the user modify page.");
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST, params = "changeEmail")
    public ModelAndView startEmailModification(@ModelAttribute("user") User user, Model model, BindingResult result) {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (currentUser.equals(User.PUBLIC_USER)) {
            return ssoRedirect("/User/Modify", "You must be logged in to change your email");
        }

        ClientResponse response = resource.path("/user/modify/email")
                .entity(user.getEmail())
                .post(ClientResponse.class);

        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setForename(currentUser.getForename());
        user.setSurname(currentUser.getSurname());
        user.setPhone(currentUser.getPhone());
        
        if (response.getClientResponseStatus() == ClientResponse.Status.OK) {
            return new UserAdminModelBuilder()
                    .view("userModify")
                    .user(user)
                    .changePassword(user.getUsername())
                    .successMessage("You will shortly receive an email at " + user.getEmail() + " with further instructions")
                    .build();
        }

        result.addError(new ObjectError(result.getObjectName(), "Email Address is already in use"));

        return new UserAdminModelBuilder()
                .view("userModify")
                .user(user)
                .changePassword(user.getUsername())
                .build();
    }

    @RequestMapping(value = "Email/Activate/{key}", method = RequestMethod.GET)
    public ModelAndView completeEmailModification(
            @PathVariable("key") String key) {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (currentUser.equals(User.PUBLIC_USER)) {
            return ssoRedirect("/User/Email/Activate/" + key, "You must be logged in to change your email");
        }
        
        ClientResponse response = resource.path("user/modify/email")
                .path("activate")
                .path(key)
                .get(ClientResponse.class);

        if (response.getClientResponseStatus() == ClientResponse.Status.OK) {
            return new ModelAndView("emailChangeSuccess");
        }

        if (response.getClientResponseStatus() == ClientResponse.Status.FORBIDDEN) {
            return new ModelAndView("invalidEmailChangeRequest");
        }

        return new ModelAndView("noEmailChangePending");
    }

    private boolean isNotPublicUser(User currentUser) {
        if (currentUser.getId() != User.PUBLIC_USER_ID) {
            return true;
        }

        return false;
    }

    private ModelAndView ssoRedirect(String url, String status) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("redirect", url);
        model.put("status", status);
        return new ModelAndView("sso", model);
    }
}
