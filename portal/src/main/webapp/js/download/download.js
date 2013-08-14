window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.download = function (json, div) {
    this.div = div;
    
    var reason = new nbn.nbnv.ui.downloadReason(json);
    var year = new nbn.nbnv.ui.filter.year(json);
    var spatial = new nbn.nbnv.ui.filter.spatial(json);
    var taxon = new nbn.nbnv.ui.filter.taxon(json);
    var dataset = new nbn.nbnv.ui.filter.dataset(json);
    var result = new nbn.nbnv.ui.downloadResult(json)
    
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
    this.div.append(result._renderHeader());
    this.div.append(result._renderPanel(function () {
        var j = { sensitive: 'sans' };
        $.extend(j, reason.getJson());        
        $.extend(j, taxon.getJson());
        $.extend(j, spatial.getJson());
        $.extend(j, year.getJson());        
        $.extend(j, dataset.getJson());        
        window.location = "/TaxonObservations/Download?json=" + JSON.stringify(j);
    }));

    this.div.accordion({
        autoHeight: false,
        change: function(event, ui) {
            var newFilter = ui.newHeader.attr('filtertype');
            var oldFilter = ui.oldHeader.attr('filtertype');

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
            } else if (newFilter == 'result') {
                var error = [];
                $.merge(error, taxon.getError());
                $.merge(error, spatial.getError());
                $.merge(error, year.getError());
                $.merge(error, reason.getError());
                
                if (dataset._all && taxon._all && spatial._all) { $.merge(error, ['You may not download all datasets on the Gateway. Please apply at least one filter.']); }
                
                result._onEnter(error);
            }

            if (oldFilter == 'year') {
                year._onExit();
            } else if (oldFilter == 'spatial') {
                spatial._onExit();
            } else if (oldFilter == 'taxon') {
                taxon._onExit();
            } else if (oldFilter == 'dataset') {
                dataset._onExit();
            } else if (oldFilter == 'reason') {
                reason._onExit();
            }
        }
    });
    
    spatial._onExit();
    year._onExit();
    taxon._onExit();
    dataset._onExit();
    
};