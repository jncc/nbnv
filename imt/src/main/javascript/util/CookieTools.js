/**
*
* @author		:- Christopher Johnson
* @date			:- 30th-March-2011
* @description	:- This JScript is adapted from the code provided at http://www.quirksmode.org/js/cookies.html. It has been put into a form
*	which matches the code stylings used by the interactive mapper.
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.CookieTools = new function() {
	this.set = function(name,value,days) {
		if (days) {
			var date = new Date();
			date.setTime(date.getTime()+(days*24*60*60*1000));
			var expires = "; expires="+date.toGMTString();
		}
		else 
			var expires = "";
		document.cookie = name+"="+value+expires+"; path=/";
	}

	this.read = function(name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') 
				c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) 
				return c.substring(nameEQ.length,c.length);
		}
		return null;
	}

	this.erase = function(name) {
		this.set(name,"",0);
	}
};