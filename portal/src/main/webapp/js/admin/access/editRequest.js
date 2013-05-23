window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.editRequest = function (json, requester, dataset, div) {
    this.div = div;
    
    var reason = new nbn.nbnv.ui.requestDetails(json, requester, 'Test');
    var year = new nbn.nbnv.ui.filter.year(json);
    var spatial = new nbn.nbnv.ui.filter.spatial(json);
    var taxon = new nbn.nbnv.ui.filter.taxon(json);
//    var dataset = new nbn.nbnv.ui.filter.dataset(json);
    var timeLimit = new nbn.nbnv.ui.timeLimit(json);
    var result = new nbn.nbnv.ui.requestEditResult();

    this.div.append(reason._renderHeader());
    this.div.append(reason._renderPanel());
    this.div.append(spatial._renderHeader());
    this.div.append(spatial._renderPanel());
    this.div.append(taxon._renderHeader());
    this.div.append(taxon._renderPanel());
    this.div.append(year._renderHeader());
    this.div.append(year._renderPanel());
//    this.div.append(dataset._renderHeader());
//    this.div.append(dataset._renderPanel());
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
            } else if (newFilter == 'taxon') {
                taxon._onEnter();
            } else if (newFilter == 'result') {
                var j = { sensitive: 'sans' };
                $.extend(j, taxon.getJson());
                $.extend(j, spatial.getJson());
                $.extend(j, year.getJson());

                result.setupData(j, dataset, '/taxonObservations/datasets/requestable');
                result._onEnter();
            }  else if (newFilter == 'timeLimit') {
                timeLimit._onEnter();
            } 

            if (oldFilter == 'year') {
                year._onExit();
            } else if (oldFilter == 'spatial') {
                spatial._onExit();
            } else if (oldFilter == 'taxon') {
                taxon._onExit();
            } /* else if (oldFilter == 'dataset') {
                dataset._onExit();
            } */ else if (oldFilter == 'timeLimit') {
                timeLimit._onExit();
            } 
        }
    });
    
    spatial._onExit();
    year._onExit();
    taxon._onExit();
    timeLimit._onExit();
//    dataset._onExit();
    
};