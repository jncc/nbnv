/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This JScript will validate to see if a user is part of the nbn gateway and if so, will store there details to be obtained later
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	jquery
*/

$.namespace("nbn.util.user.Loginable", function() {
    var userAttribute = new nbn.util.ObservableAttribute('User');
    $.extend(this, userAttribute); //make this extend a user attribute
    delete this.setUser; //remove the set user method
    
    //Check with the data api to see if a user is already logged in
    $.getJSON(nbn.util.ServerGeneratedLoadTimeConstants.data_api + '/user', function(user) {
        if (user.id !== 1) {
            userAttribute.setUser({
                userID: user.id,
                username: user.username,
                fullname: user.forename + ' ' + user.surname
            });
        }
    });
    
    this.doUserLogin = function(username, password) {
        return $.getJSON(nbn.util.ServerGeneratedLoadTimeConstants.data_api + '/user/login?username=' + username + '&password=' + password, function(result) {
            var user = result.user;
            if (result.success) {
                userAttribute.setUser({
                    userID: user.id,
                    username: user.username,
                    fullname: user.forename + ' ' + user.surname
                });
            }
        });
    };
	
    this.isUserLoggedIn = function() {
        return userAttribute.getUser() & true;
    };

    this.doUserLogout = function() {
        $.getJSON(nbn.util.ServerGeneratedLoadTimeConstants.data_api + '/user/logout', function() {
            userAttribute.setUser(undefined);
        });
    };
});