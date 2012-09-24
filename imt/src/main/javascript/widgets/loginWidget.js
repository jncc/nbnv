/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This Jquery widget will render the status of an nbn.util.user.Loginable and allow login
* @dependencies	:-
*	nbn.util.user.Loginable
*/
(function( $, undefined ) {

    $.widget( "ui.nbn_login", {
        _setLoginErrorMessage: function(message) {
            this._messageBox = this._messageBox || $('<div>').addClass('nbn-login-errorMessage ui-state-error ui-corner-all');
            if(message)
                this.element.prepend(this._messageBox.html(message));
            else
                this._messageBox.remove();
        },
	
        _create: function() {
            this.element.addClass("nbn-login ui-widget ui-widget-content ui-corner-all");
            var _me = this;
		
            var _updateLoginWidget = function(newUser) {
                _me.element.empty();
                if(newUser)
                    _me.element
                    .append($('<div>').html('You are currently logged in as ' + newUser.fullname))
                    .append($('<div>').nbn_buttonbar({
                        buttons:{
                            Logout: function() { 
                                _me.options.loginUser.doUserLogout(); 
                            }
                        }
                    }));
                else {
                    _me.element
                    .append($('<div>Username</div>').addClass('nbn-login-label'))
                    .append(_me._usernameField = $('<input type="text" name="username"/>').addClass('nbn-login-input'))
                    .append($('<div>Password</div>').addClass('nbn-login-label'))
                    .append(_me._passwordField = $('<input type="password" name="password"/>').addClass('nbn-login-input'))
                    .append($('<div>').nbn_buttonbar({
                        buttons:{
                            Login: function() {
                                _me._setLoginErrorMessage(); //clear the message box
                                _me.options.loginUser.doUserLogin(_me._usernameField.val(), _me._passwordField.val(), function(result) {
                                    if(!result.success)
                                        _me._setLoginErrorMessage('Login has failed, please try again');
                                }); 
                            }
                        }
                    }));
                }
            };
		
            this._loginUpdateListener = {
                User: _updateLoginWidget
            };
		
            this.options.loginUser.addUserUpdateListener(this._loginUpdateListener);
            _updateLoginWidget(_me.options.loginUser.getUser()); //do an initial update of the user
        },

        destroy: function() {
            this.element.removeClass( "nbn-login ui-widget ui-widget-content ui-corner-all" ); //remove the appended class
            this.options.loginUser.removeUserUpdateListener(this._loginUpdateListener);
            $.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
        }
    })

    $.extend( $.ui.nbn_login, {
        version: "@VERSION"
    });

})( jQuery );