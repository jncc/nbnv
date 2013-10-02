window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.createGrant = function (json, div) {
    this.div = div;
    
    var reason = new nbn.nbnv.ui.requestPickUserReason(json);
    reason.setGrant(true);
    var sensitive = new nbn.nbnv.ui.filter.sensitive(json);
    var year = new nbn.nbnv.ui.filter.year(json);
    var spatial = new nbn.nbnv.ui.filter.spatial(json);
    var taxon = new nbn.nbnv.ui.filter.taxon(json);
    var dataset = new nbn.nbnv.ui.requestPickDataset(json);
    var timeLimit = new nbn.nbnv.ui.timeLimit(json);
    var result = new nbn.nbnv.ui.requestGrantResult(json);

    this.div.append(reason._renderHeader());
    this.div.append(reason._renderPanel());
    this.div.append(sensitive._renderHeader());
    this.div.append(sensitive._renderPanel());
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
        $('#resultsubmitbtn').prop('disabled', 'true');
        $('#resultworkingspan').show();
        var j = {};
        $.extend(j, reason.getJson());        
        $.extend(j, sensitive.getJson());
        $.extend(j, taxon.getJson());
        $.extend(j, spatial.getJson());
        $.extend(j, year.getJson());        
        $.extend(j, dataset.getJson());        
        $.extend(j, timeLimit.getJson());    
        
        var endpoint;
        
        if (j.reason.userID > -1) { endpoint = '/user/userAccesses/requests/admin/granted'; } else { endpoint = '/organisation/organisationAccesses/requests/admin/granted'; }
        
        $.ajax({
            url: nbn.nbnv.api + endpoint,
            type: "PUT",
            contentType: 'application/json',
            processData: false,
            data: JSON.stringify(j),
            success: function() {
                window.location = "/AccessRequest/Admin";
            },
            error: function() {
                alert('Problem granting access, please try again later');
                window.location = "/AccessRequest/Admin";
            }
        });
        
    }));

    this.div.accordion({
        autoHeight: false,
        change: function(event, ui) {
            var newFilter = ui.newHeader.attr('filtertype');
            var oldFilter = ui.oldHeader.attr('filtertype');

            if (newFilter == 'year') {
                year._onEnter();
            } else if (newFilter == 'sensitive') {
                sensitive._onEnter();
            } else if (newFilter == 'spatial') {
                spatial._onEnter();
            } else if (newFilter == 'taxon') {
                taxon._onEnter();
            } else if (newFilter == 'dataset') {
                dataset._onEnter();
            } else if (newFilter == 'timeLimit') {
                timeLimit._onEnter();
            } else if (newFilter == 'reason') {
                reason._onEnter();
            } else if (newFilter == 'result') {
                var error = [];
                $.merge(error, reason.getError());
                $.merge(error, sensitive.getError());
                $.merge(error, dataset.getError());
                $.merge(error, taxon.getError());
                $.merge(error, spatial.getError());
                $.merge(error, year.getError());
                $.merge(error, timeLimit.getError());
                
                result._onEnter(error);
            }

            if (oldFilter == 'year') {
                year._onExit();
            } else if (oldFilter == 'sensitive') {
                sensitive._onExit();
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
        }
    });
    
    spatial._onExit();
    sensitive._onExit();
    year._onExit();
    taxon._onExit();
    timeLimit._onExit();
    dataset._onExit();
    
};