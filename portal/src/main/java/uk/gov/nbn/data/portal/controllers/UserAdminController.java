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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUserLandingPage() {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

        if (isNotPublicUser(currentUser)) {
            return new ModelAndView("userLanding");
        }

        return ssoRedirect("/User", "You need to be logged in to view the user landing page.");
    }

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
            data.add("allowEmailAlerts", user.isAllowEmailAlerts() ? "1" : "0");
            data.add("subscribedToAdminEmails", user.isSubscribedToAdminMails() ? "1" : "0");
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
                successMessage = "Succesfully changed email subscriptions.";
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
