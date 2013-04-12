window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestReason = function() {
    this._renderHeader = function() {
        return $('<h3>')
            .append($('<span>').addClass('filterheader').append('Request Access to Records'));
    };
    
    this._renderPanel = function() {
        var data = $('<div>')
            .append($('<div>')
                .text("I requesting access for:")
            ).append($('<select>')
                .append($('<option>').text('Myself').attr('value', '1'))
                .append($('<option>').text('Organisation #1').attr('value', '2'))
                .append($('<option>').text('Organisation #2').attr('value', '3'))
                .change(function() {
                    //role = $(this).val();
                })
            ).append($('<div>')
                .text("I am requesting data for the following purpose:")
            ).append($('<select>')
                .append($('<option>').text('Personal interest').attr('value', '1'))
                .append($('<option>').text('Educational purposes').attr('value', '2'))
                .append($('<option>').text('Research and scientific analysis').attr('value', '3'))
                .append($('<option>').text('Media publication').attr('value', '4'))
                .append($('<option>').text('Conservation NGO work').attr('value', '5'))
                .append($('<option>').text('Professional land management').attr('value', '6'))
                .append($('<option>').text('Data provision and interpretation services (commercial)').attr('value', '7'))
                .append($('<option>').text('Data provision and interpretation services (non-profit)').attr('value', '8'))
                .append($('<option>').text('Statutory work').attr('value', '9'))
                .change(function() {
                    //purpose = $(this).val();
                })
            ).append($('<div>')
                .append($('<p>').append('Purpose description here ####'))
            ).append($('<div>')
                .text("Detailed description of purpose:")
            ).append($('<textarea>')
                .attr('cols', '75')
                .attr('rows', '15')
                .text("Please enter details here")
                .change(function() {
                    //details = $(this).val();
                })
            );

        return data;
    };
};