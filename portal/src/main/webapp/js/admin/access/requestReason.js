window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestReason = function() {
    var purposes = {
        '1' : 'Data is for personal interest only and will not be passed on to other people. Examples – a local natural history society creating a recording checklist for a site or identifying gaps to target local recording effort; a county recorder checking the distribution of a species for verification purposes, commenting on records to improve data quality, comparing the NBN Gateway with other data sources.',
        '2' : 'Small scale student assignments (but not dissertations), environmental education – e.g. producing a leaflet. Non commercial training products.',
        '3' : 'Research project, for example part of formal academic study, especially where this is likely to lead to a publication.',
        '4' : 'Publication of data in printed or web-based media. Any journalistic or media use – e.g. BBC website. Publication of data, e.g. a distribution atlas or species identification guide.',
        '5' : 'Any non commercial work associated with the core business of an environmental NGO.',
        '6' : 'Data will be used to inform land management and decision making carried out by staff for a private or public landowner, e.g. MOD, Department of Transport, Network Rail, Local Authorities.  This includes use of data to inform forward planning and development control decisions, including mitigation and biodiversity offsetting.',
        '7' : 'Professional data services provided to paying clients by private sector organisations such as ecological consultants.  This includes desk studies for Environmental Impact Assessments, extended Phase I ecological surveys, agri-environment application surveys.',
        '8' : 'Professional data services provided to paying clients by non-profit organisations such as local environmental records centres, local recording groups and national recording schemes.',
        '9' : 'Any work that is connected with the core business of an organisation that has a statutory responsibility, such as Natural England, the Environment Agency or Forestry Commission.  This includes statutory nature conservation, regulatory functions and reporting.'
    };
    
    this._asID = -1;
    this._purpose = 1;
    this._details = '';
    
    var username = 'Myself';
    var purposename = 'Personal interest';
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'reason')
            .append($('<span>').addClass('filterheader').append('Request Access to Records'))
            .append($('<span>').attr('id', 'reasonResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        var asSelect = $('<select>')
                .append($('<option>').text('Myself').attr('value', '-1'))
                .change(function() {
                    _me._asID = $(this).val();
                    username = $(this).find("option:selected").text();
                });
        
        $.ajax({
            url: nbn.nbnv.api + '/user/adminOrganisations',
            success: function(orgs) {
                $.each(orgs, function (i, org) {
                    asSelect.append($('<option>').text(org.name).attr('value', org.id));
                });
            }
        })

        var data = $('<div>')
            .append($('<div>')
                .text("I requesting access for:")
            ).append(asSelect)
            .append($('<div>').addClass('queryBlock')
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
                    _me._purpose = $(this).val();
                    purposename = $(this).find("option:selected").text();
                    
                    $('#purposedescription').html('');
                    $('#purposedescription').append($('<p>')
                        .append($(this).find("option:selected").text())
                        .append(' - ')
                        .append(purposes[$(this).val()])
                    );
                })
            ).append($('<div>').addClass('resulttext').attr('id', 'purposedescription')
                .append($('<p>').append('Personal interest - ').append(purposes['1']))
            ).append($('<div>').addClass('queryBlock')
                .text("Detailed description of purpose:")
            ).append($('<textarea>')
                .attr('cols', '75')
                .attr('rows', '15')
                .text("Please enter details here")
                .change(function() {
                    _me._details = $(this).val();
                })
            );

        return data;
    };
    
    this._onEnter = function() {
        $('#reasonResult').text('');
    };
    
    this._onExit = function() {
        var text = username + ' - ' + purposename;
        
        $('#reasonResult').text(text);
    };
    
    this.getJson = function() {
        if (this._asID > -1)
            return { reason: { purpose: this._purpose, details: this._details, organisationID: this._asID }};
        
        return { reason: { purpose: this._purpose, details: this._details }};
    };

};