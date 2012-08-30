/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This JScript will validate to see if a user is part of the nbn gateway and if so, will store there details to be obtained later
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	jquery
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.user = nbn.util.user || {};

nbn.util.user.Loginable = function() {
	var userAttribute = new nbn.util.ObservableAttribute('User');
	$.extend(this, userAttribute); //make this extend a user attribute
	delete this.setUser; //remove the set user method
	this.doUserLogin = function(username, passhash, callback) {
        $.getJSON('LoginServlet?username=' + username + '&userkey=' + passhash, function(result) {
			if (result.success) {
				userAttribute.setUser({
					userID: result.userid,
					username: username,
					passhash: passhash,
					fullname: result.name
				});
			}
			if($.isFunction(callback))
				callback(result); //notify callbacks
		});
    };
	
	this.isUserLoggedIn = function() {
		return userAttribute.getUser() & true;
	};
	
	this.doUserLogout = function() {
		userAttribute.setUser(undefined);
	};
};