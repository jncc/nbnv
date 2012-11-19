window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.sensitivefilter = function() {
	$.extend(this, new nbn.util.ObservableAttribute('resultText'), '');
	
	this._value = 'ns';

        this.setSensitive = function(sensitive) {
            this._value = sensitive;
        };
        
	this._render = function() {
		var _me = this;
		
                var select = $('<select>')
				.append($('<option>').text("non-sensitive").attr('value', 'ns'))
				.append($('<option>').text("sensitive and non-sensitive").attr('value', 'sans'))
				.append($('<option>').text("sensitive").attr('value', 's'))
				.change(function() {
					var value = $(this).find('option:selected').text();
					
					_me._value = $(this).find('option:selected').attr('value');
					_me.setresultText("All <b>" + value + "</b> records");
				});
                
                select.val(this._value);
                select.change();
                
		var data = $('<div>')
			.append("All ")
			.append(select).append(" records");
		
		//this.setresultText("All <b>non-sensitive</b> records");
		
		return data;
	};
	
	this.getJSON = function() {
		return {
			sensitive: this._value
		};
	};
};