window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.download = function (json, div) {
    this.div = div;
    
    var reason = new nbn.nbnv.ui.downloadReason(json);
    var sensitive = new nbn.nbnv.ui.filter.sensitive(json);
    var year = new nbn.nbnv.ui.filter.year(json);
    var spatial = new nbn.nbnv.ui.filter.spatial(json);
    var taxon = new nbn.nbnv.ui.filter.taxon(json);
    var dataset = new nbn.nbnv.ui.filter.dataset(json, true);
    var result = new nbn.nbnv.ui.downloadResult(json);
    
    this.div.append(reason._renderHeader());
    this.div.append(reason._renderPanel());
    this.div.append(sensitive._renderHeader());
    this.div.append(sensitive._renderPanel());
    this.div.append(spatial._renderHeader());
    this.div.append(spatial._renderPanel());
    spatial._postRender();
    this.div.append(taxon._renderHeader());
    this.div.append(taxon._renderPanel());
    this.div.append(year._renderHeader());
    this.div.append(year._renderPanel());
    this.div.append(dataset._renderHeader());
    this.div.append(dataset._renderPanel());
    this.div.append(result._renderHeader());
    this.div.append(result._renderPanel(function () {       
        var j = sensitive.getJson();
        $.extend(j, reason.getJson());        
        $.extend(j, taxon.getJson());
        $.extend(j, spatial.getJson());
        $.extend(j, year.getJson());        
        $.extend(j, dataset.getJson());        
        
        $('#resultsubmitbtn').attr("disabled", true);

        $('#preparing-download-dialog').dialog({modal: true});
              
        $.fileDownload(nbn.nbnv.api + '/taxonObservations/download', {
            httpMethod: 'POST',
            data: {json: JSON.stringify(j)},
            successCallback: function(responseHtml, url) {
                $('#preparing-download-dialog').dialog('close');
            },
            failCallback: function (responseHtml, url) {
                $('#preparing-download-dialog').dialog('close');
                $('#error-download-reason').html(responseHtml);
                $('#error-download-dialog').dialog({modal: true});
            }
        });
        return false;
    }));

    this.div.accordion({
        autoHeight: false,
        change: function(event, ui) {
            var newFilter = ui.newHeader.attr('filtertype');
            var oldFilter = ui.oldHeader.attr('filtertype');

            if (oldFilter === 'year') {
                year._onExit();
            } else if (oldFilter === 'spatial') {
                spatial._onExit();
            } else if (oldFilter === 'taxon') {
                taxon._onExit();
            } else if (oldFilter === 'dataset') {
                dataset._onExit();
            } else if (oldFilter === 'reason') {
                reason._onExit();
            }

            if (newFilter === 'year') {
                year._onEnter();
            } else if (newFilter === 'spatial') {
                spatial._onEnter();
            } else if (newFilter === 'taxon') {
                taxon._onEnter();
            } else if (newFilter === 'dataset') {
                var j = { sensitive: 'sans' };
                $.extend(j, taxon.getJson());
                $.extend(j, spatial.getJson());
                $.extend(j, year.getJson());

                dataset.setupTable(j, '/taxonObservations/datasets');
                dataset._onEnter();
            } else if (newFilter === 'result') {
                var error = [];
                $.merge(error, dataset.getError());
                $.merge(error, taxon.getError());
                $.merge(error, spatial.getError());
                $.merge(error, year.getError());
                $.merge(error, reason.getError());
                
                if (dataset._all && taxon._all && spatial._all) { $.merge(error, ['You may not download all datasets on the Gateway. Please apply at least one filter.']); }
                
                result._onEnter(error);
            }
        }
    });
    
    spatial._onExit();
    year._onExit();
    taxon._onExit();
    dataset._onExit();
    
};