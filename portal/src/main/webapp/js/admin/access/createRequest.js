window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.createRequest = function (json, div) {    
    this.div = div;
    
    var reason = new nbn.nbnv.ui.requestReason(json);
    var year = new nbn.nbnv.ui.filter.year(json);
    var spatial = new nbn.nbnv.ui.filter.spatial(json);
    var taxon = new nbn.nbnv.ui.filter.taxon(json);
    var dataset = new nbn.nbnv.ui.filter.dataset(json, false);
    var timeLimit = new nbn.nbnv.ui.timeLimit(json);
    var result = new nbn.nbnv.ui.requestResult(json);

    this.div.append(reason._renderHeader());
    this.div.append(reason._renderPanel());
    this.div.append(spatial._renderHeader());
    this.div.append(spatial._renderPanel());
    this.div.append(taxon._renderHeader());
    this.div.append(taxon._renderPanel());
    this.div.append(year._renderHeader());
    this.div.append(year._renderPanel());
    this.div.append(dataset._renderHeader());
    this.div.append(dataset._renderPanel());
    this.div.append(timeLimit._renderHeader());
    this.div.append(timeLimit._renderPanel());
    this.div.append(result._renderHeader());
    this.div.append(result._renderPanel(function () {
        var j = { sensitive: 'sans' };
        $.extend(j, reason.getJson());        
        $.extend(j, taxon.getJson());
        $.extend(j, spatial.getJson());
        $.extend(j, year.getJson());        
        $.extend(j, dataset.getJson());        
        $.extend(j, timeLimit.getJson());    
        
        var userUrl = nbn.nbnv.api + '/user/userAccesses/requests';
        var orgUrl = nbn.nbnv.api + '/organisation/organisationAccesses/requests';
        var url = ('organisationID' in j.reason && j.reason.organisationID !== -1) ? orgUrl : userUrl;
        
        $('#waiting-dialog').dialog({
            closeOnEscape: false,
            buttons: null,
            modal: true,
            resizable: false,
            dialogClass: "noclose"
        });
        
        $.ajax({
                type: "PUT",
                url: url,
                contentType: 'application/json',
                processData: false,
                data: JSON.stringify(j),
                success: function() {
                    $('#finished-dialog-text').html(
                            '<p>Your access request has successfully been submitted and sent to all the relevant dataset administrators.</p>' +
                            '<p>A summary of this request has been added to your list of pending access requests, available through your user account page. You will be notified of the dataset administrators decisions (either accepting or rejecting your request) for each dataset by email.</p>' +
                            '<p>Please ensure your use of the data complies with the <a href="/Terms">NBN Gateway Terms and Conditions</a>.</p>'
                    );
                            
                    $('#waiting-dialog').dialog('close');
                    
                    $('#finished-dialog').dialog({
                        width: 800,
                        closeOnEscape: false,
                        modal: true,
                        resizable: false,
                        dialogClass: "noclose",
                        buttons: { 'Return to my account' : function() {  window.location = '/User'; } }                        
                    });
                },
                error: function(error) {
                    $('#finished-dialog-text').html('');
                    
                    var obj = JSON.parse(error.responseText);
                    
                    if (obj.status.match('^Zero records would be granted in')) {
                        $('#finished-dialog-text').append(obj.status + ', please amend your request and re-submit. If you believe this was in error please submit this information to access@nbn.org.uk and we will look into it')
                            .append($('<p>').text(error.responseText))
                            .append($('<p>')
                                .append(JSON.stringify(j))
                            );
                    }
                    else {
                        $('#finished-dialog-text').append("Your access request has failed to be submitted correctly. Please submit this information to access@nbn.org.uk and we will look into it.")
                            .append($('<p>').text(error.responseText))
                            .append($('<p>')
                                .append(JSON.stringify(j))
                            );
                    }

                    $('#waiting-dialog').dialog('close');

                    $('#finished-dialog').dialog({
                        width: 800,           
                        title: 'An error has occured',
                        closeOnEscape: false,
                        modal: true,
                        resizable: false,
                        dialogClass: "noclose",
                        buttons: { 'Return to my account' : function() {  window.location = '/User'; } }                        
                    });
                }
            });        
    }));

    this.div.accordion({
        autoHeight: false,
		heightStyle: "content",
        activate: function(event, ui) {
            var newFilter = ui.newHeader.attr('filtertype');
            var oldFilter = ui.oldHeader.attr('filtertype');

            if (oldFilter == 'year') {
                year._onExit();
            } else if (oldFilter == 'spatial') {
                spatial._onExit();
            } else if (oldFilter == 'taxon') {
                taxon._onExit();
            } else if (oldFilter == 'dataset') {
                dataset._onExit();
            } else if (oldFilter == 'timeLimit') {
                timeLimit._onExit();
            } else if (oldFilter == 'reason') {
                reason._onExit();
            }

            if (newFilter == 'year') {
                year._onEnter();
            } else if (newFilter == 'spatial') {
                spatial._onEnter();
            } else if (newFilter == 'taxon') {
                taxon._onEnter();
            } else if (newFilter == 'dataset') {
                var j = { sensitive: 'sans' };
                $.extend(j, taxon.getJson());
                $.extend(j, spatial.getJson());
                $.extend(j, year.getJson());

                dataset.setupTable(j, '/taxonObservations/datasets/requestable');
                dataset._onEnter();
            } else if (newFilter == 'timeLimit') {
                timeLimit._onEnter();
            } else if (newFilter == 'reason') {
                reason._onEnter();
            } else if (newFilter == 'result') {
                var error = [];
                $.merge(error, reason.getError());
                $.merge(error, dataset.getError());
                $.merge(error, taxon.getError());
                $.merge(error, spatial.getError());
                $.merge(error, year.getError());
                $.merge(error, timeLimit.getError());
                
                if (dataset._all && taxon._all && spatial._all) { $.merge(error, ['You may not request access for all datasets on the Gateway. Please apply one filter.']); }
                
                result._onEnter(reason._perm, error);
            }
        }
    });
    
    spatial._onExit();
    year._onExit();
    taxon._onExit();
    timeLimit._onExit();
    dataset._onExit();
    
};