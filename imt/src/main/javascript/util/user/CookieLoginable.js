/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This JScript extends on nbn.util.user.Loginable by adding support for cookies
* @dependencies	:-
*	nbn.util.user.Loginable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.user = nbn.util.user || {};

nbn.util.user.CookieLoginable = function() {
	var _me = this;
	var _cookieLogin = function() {
		var contents = nbn.util.CookieTools.read('auth');
		if (contents) {
			var contentparts = contents.split('#');
			_me.doUserLogin(contentparts[0], contentparts[1]);
		} 
	};
	
	var _cookieStore = function(newUser) {
		if(newUser) 
			nbn.util.CookieTools.set('auth', newUser.username + '#' + newUser.passhash);
		else 
			nbn.util.CookieTools.erase('auth');
	};
	
	$.extend(this, new nbn.util.user.Loginable()); // extend an instance of Loginable
	
	this.addUserUpdateListener({ //add listener to set cookie on login
		User: _cookieStore
	});
	
	_cookieLogin(); //do cookie login
};