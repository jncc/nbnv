/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.dataset.privileges;

import nbn.common.user.User;

/**
 *
 * @author Administrator
 */
public class Privileges {
    private boolean _downloadRawData;
    private boolean _viewAttributes;
    private boolean _viewSensitive;
    private boolean _viewRecorder;
    private BlurLevel _blurLevel;
    private User _user;

    /**
     * @return the _downloadRawData
     */
    public boolean hasDownloadRawData() {
        return _downloadRawData;
    }

    /**
     * @return the _viewAttributes
     */
    public boolean hasViewAttributes() {
        return _viewAttributes;
    }

    /**
     * @return the _viewSensitive
     */
    public boolean hasViewSensitive() {
        return _viewSensitive;
    }

    /**
     * @return the _viewRecorder
     */
    public boolean hasViewRecorder() {
        return _viewRecorder;
    }

    /**
     * @return the _blurLevel
     */
    public BlurLevel getBlurLevel() {
        return _blurLevel;
    }

    Privileges(User user, BlurLevel blur, boolean download, boolean sensitive, boolean recorder, boolean attributes) {
        this._user = user;
        this._blurLevel = blur;
        this._downloadRawData = download;
        this._viewAttributes = attributes;
        this._viewRecorder = recorder;
        this._viewSensitive = sensitive;
    }

    /**
     * @return the _user
     */
    public User getUser() {
        return _user;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Privileges)  {
            Privileges obj = (Privileges)o;
            if (this._downloadRawData == obj._downloadRawData
                    && this._viewAttributes == obj._viewAttributes
                    && this._viewRecorder == obj._viewRecorder
                    && this._viewSensitive == obj._viewSensitive
                    && this._user.equals(obj._user)
                    && this._blurLevel.getBlurLevelCode() == obj._blurLevel.getBlurLevelCode())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this._downloadRawData ? 1 : 0);
        hash = 53 * hash + (this._viewAttributes ? 1 : 0);
        hash = 53 * hash + (this._viewSensitive ? 1 : 0);
        hash = 53 * hash + (this._viewRecorder ? 1 : 0);
        hash = 53 * hash + (this._blurLevel != null ? this._blurLevel.hashCode() : 0);
        hash = 53 * hash + (this._user != null ? this._user.hashCode() : 0);
        return hash;
    }
}
