/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.user;

/**
 *
 * @author Administrator
 */
public class User {
    private int _userKey;
    private String _fullName;

    public static final User PUBLIC_USER = new User(0, "Public");

    /**
     * @return the _userKey
     */
    public int getUserKey() {
        return _userKey;
    }

    /**
     * @return the _fullName
     */
    public String getFullName() {
        return _fullName;
    }

    User(int key, String name) {
        this._userKey = key;
        this._fullName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User)  {
            User obj = (User)o;
            if (obj._userKey == this._userKey)
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this._userKey;
        return hash;
    }
}
