window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.util = nbn.nbnv.ui.util || {};

nbn.nbnv.ui.util.requestJsonToText = function(json) {
    var text;
        
    if (json.sensitive == 'ns') {
        text = "All <b>non-sensitive</b> records";
    }
        
    if (json.sensitive == 'sans') {
        text = "All <b>sensitive and non-sensitive</b> records";
    }
        
    if (json.sensitive == 's') {
        text = "All <b>sensitive</b> records";
    }
        
    $.each(json.filters, function(id, filter) {
        if (filter.type == 'year') {
            text += " between <b>" + filter.start + "</b> and <b>" + filter.end + "</b>";
        }
            
        if (filter.type == 'taxon') {
            text += " for <b><i>" + filter.sciname + "</i></b>";
        }
            
        if (filter.type == 'spatial') {
            text += " <b>" + filter.match + "</b> the boundary of <b>" + filter.boundary + "</b>";
        }
    });
        
    return text;
};
