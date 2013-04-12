window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.accessRequest = function() {
    var role = 0;
    var purpose = 0;
    var details = "Please enter details here";
    
    this._render = function() {
        var _me = this;
        
        var data = $('<div>')
            .addClass("portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .append($('<div>')
                .addClass("portlet-header ui-widget-header ui-corner-all")
                .text("Define Data Use")
            ).append($('<div>')
                .addClass('portlet-content')
                .append($('<div>')
                    .addClass("datause-para")
                    .text("I am a:")
                ).append($('<select>')
                    .append($('<option>').text('Scientist').attr('value', '1'))
                    .append($('<option>').text('Conservation Worker').attr('value', '2'))
                    .append($('<option>').text('Ecological Consultant').attr('value', '3'))
                    .append($('<option>').text('Planning Officer').attr('value', '4'))
                    .change(function() {
                         role = $(this).val();
                    })
                ).append($('<div>')
                    .addClass("datause-para")
                    .text("I am requesting data for the following purpose:")
                ).append($('<select>')
                    .append($('<option>').text('Species Conservation').attr('value', '1'))
                    .append($('<option>').text('Habitat Conservation').attr('value', '2'))
                    .append($('<option>').text('Scientific Study').attr('value', '3'))
                    .append($('<option>').text('Planning Permission').attr('value', '4'))
                    .change(function() {
                         purpose = $(this).val();
                    })
                ).append($('<div>')
                    .addClass("datause-para")
                    .text("I request access for the following purpose:")
                ).append($('<textarea>')
                    .attr('cols', '75')
                    .attr('rows', '15')
                    .text("Please enter details here")
                    .change(function() {
                         details = $(this).val();
                    })
                )
            );
        
        return data;
    }
    
    this.getJSON = function() {
        return { 'role': role, 'purpose': purpose, 'details': details }
    }
}

