/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.logging;

import nbn.common.user.User;

/**
 *
 * @author Administrator
 */
public class RequestMetadata {
    private String _referer;
    private String _userAgent;
    private String _remoteHost;
    private User _user;

    /**
     * @return the _referer
     */
    public String getReferer() {
        return _referer;
    }

    /**
     * @param referer the _referer to set
     */
    public void setReferer(String referer) {
        this._referer = referer;
    }

    /**
     * @return the _userAgent
     */
    public String getUserAgent() {
        return _userAgent;
    }

    /**
     * @param userAgent the _userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this._userAgent = userAgent;
    }

    /**
     * @return the _remoteHost
     */
    public String getRemoteHost() {
        return _remoteHost;
    }

    /**
     * @param remoteHost the _remoteHost to set
     */
    public void setRemoteHost(String remoteHost) {
        this._remoteHost = remoteHost;
    }

    /**
     * @return the _user
     */
    public User getUser() {
        return _user;
    }

    /**
     * @param user the _user to set
     */
    public void setUser(User user) {
        this._user = user;
    }


}
