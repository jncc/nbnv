(function($){
    
    var options = {
        imagesize: 5
    };
    
    var nationalExtentOptions = {
        gbi: {
            coastline: 'gbi', 
            grid100k: 'gbi100kextent', 
            grid10k: 'gbi10kextent'
        },
        gb: {
            coastline: 'gb', 
            grid100k: 'gb100kextent', 
            grid10k: 'gb10kextent'
        },
        ireland: {
            coastline: 'i', 
            grid100k: 'i100kextent', 
            grid10k: 'i10kextent'
        } 
    };

    function getURL(div){
        return div.attr('gis-server') + '/SingleSpecies/' + div.attr('tvk') + '/map?resolution=10km&nationalextent=gbi&background=gbi&nbn-select-datasets-auto=on&imagesize=3';
    }
    
    function hideBusyImageOnMapLoad(){
        $('#nbn-grid-map-image').load(function(){
            $('#nbn-grid-map-busy-image').hide();
        });
    }

    function addInitialMapImage(){
        $('#nbn-grid-map-busy-image').hide();
        $('#nbn-grid-map-image').attr('src','/img/ajax-loader-medium.gif');
        $('#nbn-grid-map-image').attr('src',getURL($('#nbn-grid-map-container')));
    }

    function setupDownloadRecordsLink() {
        $('#nbn-download-observations-button').click(function(e) {
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        window.location = '/Download?json={' + 
                            getTaxonJSON() + '}';
                    },
                    'Cancel': function(){
                        $(this).dialog("close");
                    }
                }
            });
            e.preventDefault();
        });
    }

    function getTaxonJSON() {
        return 'taxon:{tvk:\'' + $('#tvk').val() + '\'}';
    }

    $(document).ready(function(){
        hideBusyImageOnMapLoad();
        addInitialMapImage();
        $('#nbn-download-terms').hide();
        setupDownloadRecordsLink();
    });
        
})(jQuery);


