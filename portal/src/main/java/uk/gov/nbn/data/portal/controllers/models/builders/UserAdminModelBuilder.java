/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers.models.builders;

import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.controllers.models.ChangePassword;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
public class UserAdminModelBuilder {

    private String _view;
    private User _user;
    private ChangePassword _changePassword;
    private String _successMessage = "";

    public UserAdminModelBuilder() {
    }

    public UserAdminModelBuilder user(User user) {
        _user = user;
        return this;
    }

    public ModelAndView build() {
        ModelAndView view = new ModelAndView(_view);

        view.addObject("user", _user);
        view.addObject("changePassword", _changePassword);
        view.addObject("successMessage", _successMessage);

        return view;
    }

    public UserAdminModelBuilder view(String view) {
        _view = view;
        return this;
    }

    public UserAdminModelBuilder changePassword(ChangePassword changePassword) {
        _changePassword = changePassword;
        return this;
    }

    public UserAdminModelBuilder changePassword(String username) {
        _changePassword = new ChangePassword();
        _changePassword.setUsername(username);
        _changePassword.setToken("t");
        return this;
    }

    public UserAdminModelBuilder successMessage(String successMessage) {
        _successMessage = successMessage;
        return this;
    }
}
