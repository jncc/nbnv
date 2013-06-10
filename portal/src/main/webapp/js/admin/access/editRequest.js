window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.editRequest = function (json, requester, dataset, id, div, orgReq) {
    this.div = div;
    
    var reqEndpoint;
    
    if (orgReq) {
        reqEndpoint = '/organisation/organisationAccesses/requests';
    } else {
        reqEndpoint = '/user/userAccesses/requests';
    }
    
    var reason = new nbn.nbnv.ui.requestDetails(json, requester, 'Test');
    var sensitive = new nbn.nbnv.ui.filter.sensitive(json);
    var year = new nbn.nbnv.ui.filter.year(json);
    var spatial = new nbn.nbnv.ui.filter.spatial(json);
    var taxon = new nbn.nbnv.ui.filter.taxon(json);
    var timeLimit = new nbn.nbnv.ui.timeLimit(json);
    var result = new nbn.nbnv.ui.requestEditResult(reqEndpoint);

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
    this.div.append(timeLimit._renderHeader());
    this.div.append(timeLimit._renderPanel());
    this.div.append(result._renderHeader());
    this.div.append(result._renderPanel());

    this.div.accordion({
        autoHeight: false,
        change: function(event, ui) {
            var newFilter = ui.newHeader.attr('filtertype');
            var oldFilter = ui.oldHeader.attr('filtertype');

            if (newFilter == 'year') {
                year._onEnter();
            } else if (newFilter == 'spatial') {
                spatial._onEnter();
            } else if (newFilter == 'sensitive') {
                sensitive._onEnter();
            } else if (newFilter == 'taxon') {
                taxon._onEnter();
            } else if (newFilter == 'result') {
                $.extend(json, sensitive.getJson());
                $.extend(json, taxon.getJson());
                $.extend(json, spatial.getJson());
                $.extend(json, year.getJson());

                result.setupData(json, dataset, '/taxonObservations/datasets/' + dataset + '/requestable');
                result._onEnter(json, id);
            }  else if (newFilter == 'timeLimit') {
                timeLimit._onEnter();
            } 

            if (oldFilter == 'year') {
                year._onExit();
            } else if (oldFilter == 'sensitive') {
                sensitive._onExit();
            } else if (oldFilter == 'spatial') {
                spatial._onExit();
            } else if (oldFilter == 'taxon') {
                taxon._onExit();
            }  else if (oldFilter == 'timeLimit') {
                timeLimit._onExit();
            } 
        }
    });
    
    spatial._onExit();
    year._onExit();
    taxon._onExit();
    timeLimit._onExit();
    sensitive._onExit();
    
};