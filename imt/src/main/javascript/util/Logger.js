/**
*
* @author		:- Christopher Johnson
* @date			:- 28-Feb-2011
* @description	:- This JScript file will allow errors which have occured to be registered and shown to the use in a clean way
* @dependencies	:-
*	jquery ui
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.Logger = function() {
	var _neverShow = false;
	var _errorDialog, _errorMessages;
	
	var _showDialog = function() {
		if(!_neverShow) {
			if(!_errorDialog) {
				_errorDialog = $('<div>')
					.append($('<div>')
						.addClass('ui-state-error')
						.append('<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;">')
						.append('<strong>An error has occurred whilst exploring the map and may result in unexpected behaviour. This does not necessarily prevent you from carrying on and viewing records but if this error persists then please report it through the NBN Gateway feedback. Close this dialog to continue.</strong>')
						.append('<p>Below are the errors which caused this interruption: </p>')
						.append(_errorMessages = $('<div style="border-style:solid; height: 200px; width: 600px; overflow:auto;">'))
					)
					.dialog({
						title: "Whoops, There has been an error", 
						dialogClass: 'ui-state-error',
						modal: true,
						buttons: {
							"Close and Never Show" : function() {
								_neverShow = true; //flag to never open again
								$( this ).dialog( "close" );
							},
							Close: function() {
								$( this ).dialog( "close" );
							}
						},
						width: '800'
					});
			}
			else if(!_errorDialog.dialog('isOpen')) {
				_errorMessages.empty();
				_errorDialog.dialog('open');
			}
		}
	}
	
	this.error = function(type, desc) {
		_showDialog();
		_errorMessages.append($('<div>').html(type + ' : ' + desc));
	};
};