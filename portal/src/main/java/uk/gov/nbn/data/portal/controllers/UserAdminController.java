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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import uk.gov.nbn.data.portal.controllers.models.ChangePassword;
import uk.org.nbn.nbnv.api.model.EmailSettingsModel;
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
        if (isNotPublicUser()) {
            return new ModelAndView("userLanding");
        }

        return ssoRedirect("/User", "You need to be logged in to view the user landing page.");
    }

    @RequestMapping(value = "Modify", method = RequestMethod.GET)
    public ModelAndView getUserModifyPage() {
        if (isNotPublicUser()) {
            ModelAndView view = new ModelAndView("userModify");
            addToModel(view, "change");
            return view;
        }

        return ssoRedirect("/User/Modify", "You need to be logged in to view the user modify page.");
    }

    private void addToModel(ModelAndView model, String token) {
        User currentUser = resource.path("user/full")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);
        model.addObject("user", currentUser);
        ChangePassword changePassword = new ChangePassword();
        changePassword.setToken(token);
        changePassword.setUsername(currentUser.getUsername());
        model.addObject("changePassword", changePassword);
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST)
    public ModelAndView postUserModifyPage(@Valid User modifiedUser, BindingResult result) throws JSONException {
        if (isNotPublicUser()) {
            if (!result.hasErrors()) {
                ClientResponse regResponse = resource.path("user/modify")
                        .type(MediaType.APPLICATION_JSON)
                        .post(ClientResponse.class, modifiedUser);

                if (regResponse.getClientResponseStatus() == ClientResponse.Status.OK) {
                    return new ModelAndView(new RedirectView("/User/Modify", true));
                } else {
                    JSONObject entity = regResponse.getEntity(JSONObject.class);
                    result.addError(new ObjectError(result.getObjectName(), entity.getString("status")));
                }
            }

            ModelAndView view = new ModelAndView("userModify");
            addToModel(view, "change");
            return view;
        }

        return ssoRedirect("/User/Modify", "You need to be logged in to view the user modify page.");
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST, params = "changePassword")
    public ModelAndView changePassword(@Valid ChangePassword changePassword, Model model, BindingResult result) {
        if (!result.hasErrors()) {
            MultivaluedMap data = new MultivaluedMapImpl();
            data.add("password", changePassword.getPassword());
            ClientResponse response = resource.path("user/passwords/change")
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .post(ClientResponse.class, data);

            return new ModelAndView("password-change-success");
        } else {
            ModelAndView view = new ModelAndView("userModify");
            addToModel(view, "change");
            return view;
        }
    }

    @RequestMapping(value = "Modify", method = RequestMethod.POST, params = "emailSettings")
    public ModelAndView changeEmailSettings(User user, Model model, BindingResult result) {
        ClientResponse response = resource.path("user/emailSettings")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, new EmailSettingsModel(user.isAllowEmailAlerts(), user.isSubscribedToAdminMails(), user.isSubscribedToNBNMarketting()));

        ModelAndView view = new ModelAndView("userModify");
        addToModel(view, "change");
        return view;
    }

    private boolean isNotPublicUser() {
        User currentUser = resource.path("user")
                .accept(MediaType.APPLICATION_JSON)
                .get(User.class);

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
