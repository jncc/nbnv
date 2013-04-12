window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.privileges = function(json) {
    var datasets = json.datasetselection.datasets;
    var mode = 0;
    
    this.setAccessRenderOnly = function() {
        mode = 1;
    }
    
    this._render = function() {
        var data = $('<div>')
                    .addClass("privileges");
        
        $.each(datasets, function(id, td) {
            var priv;
            
            if (mode == 0)
                priv = new nbn.nbnv.ui.privilegeFull();
            else if (mode == 1)
                priv = new nbn.nbnv.ui.privilegeAccess();
            
            priv.setDataset(td);
            data.append(priv._render());
        });
        
        return data;
    };
};
