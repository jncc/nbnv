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

    function addArkiveImage(name){
        $.ajax({
            url: "http://www.arkive.org/api/K8M8UWEO09/portlet/latin/" + name + "/1"
        }).done(function( html ) {
            
            $('#nbn-taxon-page-arkive').append(html);
        });
    }
    
    $(document).ready(function(){
        hideBusyImageOnMapLoad();
        addInitialMapImage();
        addArkiveImage($('#nbn-taxon-page-arkive').attr('sciName'));
        
    });
        
})(jQuery);


