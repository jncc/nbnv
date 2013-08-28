window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.downloadReason = function(json) {
    if (typeof(json.reason) === 'undefined') { json.reason = { purpose: 1, details: '', organisationID: -1 }; }
    
    var purposes = {
        '1' : { text : 'Data is for personal interest only and will not be passed on to other people. Examples - a local natural history society creating a recording checklist for a site or identifying gaps to target local recording effort; a county recorder checking the distribution of a species for verification purposes, commenting on records to improve data quality, comparing the NBN Gateway with other data sources.', perm : false },
        '2' : { text : 'Small scale student assignments (but not PhD theses or papers published in peer reviewed literature), environmental education - e.g. producing a leaflet. Non commercial training products.', perm : false },
        '3' : { text : 'Any funded research project, such as a PhD or postdoctoral research, and any research that leads to publication.', perm : true },
        '4' : { text : 'Publication of data in printed or web-based media. Any journalistic or media use - e.g. BBC website. Publication of data, e.g. a distribution atlas or species identification guide.', perm : true },
        '5' : { text : 'Any non commercial work associated with the core business of an environmental NGO.', perm : false },
        '6' : { text : 'Data will be used to inform land management and decision making carried out by staff for a private or public landowner, e.g. MOD, Department of Transport, Network Rail, Local Authorities.  This includes use of data to inform forward planning and development control decisions, including mitigation and biodiversity offsetting.', perm : true },
        '7' : { text : 'Professional data services provided to paying clients by private sector organisations such as ecological consultants.  This includes desk studies for Environmental Impact Assessments, extended Phase I ecological surveys, agri-environment application surveys.', perm : true },
        '8' : { text : 'Professional data services provided to paying clients by non-profit organisations such as local environmental records centres, local recording groups and national recording schemes.', perm : true },
        '9' : { text : 'Any work that is connected with the core business of an organisation that has a statutory responsibility, such as Natural England, the Environment Agency or Forestry Commission.  This includes statutory nature conservation, regulatory functions and reporting.', perm : false }
    };
    
    this._asID = json.reason.organisationID;
    this._purpose = json.reason.purpose;
    this._details = json.reason.details;
    this._perm = false;
    this._includeAttributes = false;
    
    var username = 'Myself';
    var purposename = 'Personal interest';
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'reason')
            .append($('<span>').addClass('filterheader').append('Download Details'))
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
                
                if (_me._asID > -1) { 
                    asSelect.val(_me._asID);
                    asSelect.change();
                }
            }
        })

        var purpose = $('<select>')
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
                    _me._perm = purposes[$(this).val()].perm;
                    
                    purposename = $(this).find("option:selected").text();
                    
                    $('#purposedescription').html('');
                    $('#purposedescription').append($('<p>')
                        .append($(this).find("option:selected").text())
                        .append(' - ')
                        .append(purposes[$(this).val()].text)
                    );
                        
                    if (_me._perm) {
                        $('#purposedescription').append($('<p>')
                            .addClass('ui-state-error')
                            .append('WRITTEN PERMISSION FROM THE DATA PROVIDER IS REQUIRED FOR THIS TYPE OF USE.')
                            );
                    }
                });
                
        purpose.val(this._purpose);
        purpose.change();
                
        var details = $('<textarea>')
                .attr('cols', '75')
                .attr('rows', '15')
                .watermark("Please enter details here")
                .change(function() {
                    _me._details = $(this).val();
                });
        
        if (this._details != '') { details.text(this._details); }
        
        var data = $('<div>')
            .append($('<div>')
                .text("I am downloading these records for:")
            ).append(asSelect)
            .append($('<div>').addClass('queryBlock')
                .text("I am downloading these records for the following purpose:")
            ).append(purpose)
            .append($('<div>').addClass('resulttext').attr('id', 'purposedescription')
                .append($('<p>').append('Personal interest - ').append(purposes['1'].text))
            ).append($('<div>').addClass('queryBlock')
                .text('Detailed description of purpose:')
            ).append(details)
             .append($('<br>'))
             .append($('<div>').text('Include Attributes:'))
             .append($('<input>')
                .attr('type', 'checkbox')
                .attr('name', 'includeattributes')
                .attr('value', _me._includeAttributes)
                .change(function() {
                    _me._includeAttributes = this.checked;
                })
            ).append($('<span>').text('Attributes will only be included if you have full access to an observation record'));

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
            return { reason: { purpose: this._purpose, details: this._details, organisationID: this._asID, includeAttributes: this._includeAttributes }};
        
        return { reason: { purpose: this._purpose, details: this._details, userID: nbn.nbnv.userID, includeAttributes: this._includeAttributes }};
    };

    this.getError = function() {
        if (this._details == '') { return ['Please enter detailed reason for this download']; }
        
        return [];
    };
};