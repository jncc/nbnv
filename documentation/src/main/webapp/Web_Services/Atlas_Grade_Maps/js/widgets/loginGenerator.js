(function($, undefined){
	$.widget("ui.nbn_loginGenerator", {
		_create:function(){
			var _me = this;
			this.element
				.addClass("nbn-loginGenerator ui-widget ui-widget-content ui-corner-all")
				.append($('<div>').addClass('nbn-loginGenerator-label').html('Username'))
				.append(this._username = $('<input>').attr('type','text').addClass('nbn-loginGenerator-input'))
				.append($('<div>').addClass('nbn-loginGenerator-label').html('Password'))
				.append(this._password = $('<input>').attr('type','password').addClass('nbn-loginGenerator-input'))
				.append($('<button>').addClass('nbn-loginGenerator-generateButton').html('Generate Login Details').button().click(function(event){
					event.preventDefault();
					_me._trigger('generate', 0, { username: _me._username.val(), userkey: $.md5(_me._password.val())});
				}));
		}
	});
})(jQuery);