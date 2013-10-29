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

    function getURL(form){
        var tvk = $('#tvk').val(); 
        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
        var keyValuePairsWithBusinessLogic = getKeyValuePairsWithBusinessLogic(keyValuePairs);
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsWithBusinessLogic, true);
        return form.attr('gis-server') + '/SingleSpecies/' + tvk + '/map' + queryString;
    }
    
    function getKeyValuePairsWithBusinessLogic(keyValuePairs){
        //Add the image size
        keyValuePairs['imagesize'] = options.imagesize;
        
        //Vice county - remove the feature argument generated when Vice County value is 'none',
        //otherwise we are zooming to a vice county so add overlay=feature to highlight the vc
        if(keyValuePairs.hasOwnProperty('feature') && keyValuePairs['feature'].toUpperCase()=='NONE'){
            delete keyValuePairs['feature'];
        }else{
            keyValuePairs['overlay'] = 'feature';
        }
        
        //Add the year bands formatted for the grid map service
        var showOutline = keyValuePairs.hasOwnProperty('showOutline');
        for(var i=1; i<4; i++){
            if(keyValuePairs.hasOwnProperty('gridLayer' + i)){
                var fillColour = $('#value-nbn-colour-picker-' + i).val();
                var outlineColour = fillColour;
                if(showOutline){
                    outlineColour = $('#value-nbn-colour-picker-outline').val();
                }
                keyValuePairs['band' + i] = keyValuePairs['startYear' + i] + '-' + keyValuePairs['endYear' + i] + ',' + fillColour.replace("#","") + ',' + outlineColour.replace("#","");
            }
            delete keyValuePairs['gridLayer' + i];
            delete keyValuePairs['startYear' + i];
            delete keyValuePairs['endYear' + i];
            delete keyValuePairs['value-nbn-colour-picker-' + i];
        }
        
        //There is a specific order that background layers should be requested to force
        //some layers to be drawn over others
        //eg - if OS is used as a background it must appear first to force vector layers to be drawn over it and not be obscured by it
        if(keyValuePairs.hasOwnProperty('background') && keyValuePairs['background'] instanceof Array){
            var orderedBackgroundArgs = ['os','vicecounty',
            nationalExtentOptions.gbi.coastline,nationalExtentOptions.gb.coastline,nationalExtentOptions.ireland.coastline,
            nationalExtentOptions.gbi.grid10k,nationalExtentOptions.gb.grid10k,nationalExtentOptions.ireland.grid10k,
            nationalExtentOptions.gbi.grid100k,nationalExtentOptions.gb.grid100k,nationalExtentOptions.ireland.grid100k
            ];
            var toReturn = [];
            $.each(orderedBackgroundArgs, function(index, value){
                if($.inArray(value,keyValuePairs['background']) > -1){
                    toReturn.push(value);
                }
            });
            keyValuePairs['background'] = toReturn;
        }       
        
        //The dataset key argument is 'datasets', whereas the generic table of datasets uses 'datasetKey' - this needs changing
        if(keyValuePairs.hasOwnProperty('datasetKey')){
            keyValuePairs['datasets'] = keyValuePairs['datasetKey'];
            delete keyValuePairs['datasetKey'];
        }
        
        //Remove the hidden tvk, just used to get the tvk from the path of the page request to here
        delete keyValuePairs['tvk'];
        
        //Remove the hidden outline colour
        delete keyValuePairs['value-nbn-colour-picker-outline'];
        delete keyValuePairs['showOutline'];

        return keyValuePairs;
    }
        
    function getColourPickerOptions(colourPickerId){
        var currentColour = $('#' + colourPickerId + ' div').css('backgroundColor');
        return {
            color: colorToHex(currentColour),
            onShow: function (colpkr) {
                $(colpkr).fadeIn(500);
                return false;
            },
            onHide: function (colpkr) {
                //Refresh map if the associated date range check box is selected
                if($('input[colourPickerId="' + colourPickerId + '"]').is(':checked')){
                    doOnChange();
                }
                $(colpkr).fadeOut(500);
                return false;
            },
            onChange: function (hsb, hex, rgb) {
                $('#' + colourPickerId + ' div').css('backgroundColor', '#' + hex);
                $('#value-' + colourPickerId).attr('value',hex);
            }
        }
    }
        
    function colorToHex(color) {
        if (color.substr(0, 1) === '#') {
            return color;
        }
        var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);
    
        var red = parseInt(digits[2]);
        var green = parseInt(digits[3]);
        var blue = parseInt(digits[4]);
    
        var rgb = blue | (green << 8) | (red << 16);
        return digits[1] + '#' + rgb.toString(16);
    };
    
    function updateResolutionDropDown($form){
        var tvk = $('#tvk').val();
        var feature = $('#nbn-vice-county-selector').val().toUpperCase();
        var url = $form.attr('gis-server') + '/SingleSpecies/' + tvk + '/resolutions?callback=?';
        if(feature != 'NONE'){
            url += '&feature=' + feature;
        }
        return $.getJSON(url, function(json){
            var resolutions = json[options.imagesize];
            var $resolutionSelect = $('#nbn-grid-map-resolution');
            //Get the currently selected option, if possible it will be used to set the selected option
            var selectedResolution = $resolutionSelect.val();
            $resolutionSelect.find('option').remove();
            var isSelectedMatchFound = false;
            $.each(resolutions, function(index, resolution){
                var selected = '';
                if(resolution == selectedResolution){
                    selected = ' selected="selected"';
                    isSelectedMatchFound = true;
                }
                $resolutionSelect.append(
                    $('<option' + selected + '></option>').val(resolution).html(resolution)
                    );
            });
            //Default to 10km
            if(!isSelectedMatchFound){
                $resolutionSelect.val("10km");
                $('#nbn-grid-map-resolution-download-text').text($resolutionSelect.val());

            }
        });
    }
    
    function applyRules(){
        
        //There must be at least one year band - if none are selected then turn on first year band
        if(!$("INPUT[name='gridLayer1'][type='checkbox']").is(':checked')
            && !$("INPUT[name='gridLayer2'][type='checkbox']").is(':checked')
            && !$("INPUT[name='gridLayer3'][type='checkbox']").is(':checked')
            ){
            $("INPUT[name='gridLayer1'][type='checkbox']").prop('checked',true);
        }
        
        //Set grid and coast check boxes to their region specific values
        var nationalExtent = $('#nbn-region-selector').val();
        $('#nbn-grid-map-coastline').val(nationalExtentOptions[nationalExtent].coastline);
        $('#nbn-grid-map-100k-grid').val(nationalExtentOptions[nationalExtent].grid100k);
        $('#nbn-grid-map-10k-grid').val(nationalExtentOptions[nationalExtent].grid10k);
        
        //There should be at least one background layer (eg coastlines)
        if($("INPUT:checked[name='background'][type='checkbox']").length == 0){
            $('#nbn-grid-map-coastline').prop('checked',true);
        }
        
    }
    
    function setupRegionVCInteractions(){
        //When selecting a national region the Vice County drop down must return to 'none'
        //If Ireland is selected, then disable os and vc checkboxes
        $('#nbn-region-selector').change(function(){
            $('#nbn-vice-county-selector').val("none");
            var disableNonIrishLayers = ($('#nbn-region-selector').val().toUpperCase() == 'IRELAND');
            $('#nbn-grid-map-vicecounty').prop('disabled', disableNonIrishLayers);
            $('#nbn-grid-map-os').prop('disabled', disableNonIrishLayers);
        });
        //When selecting a vice county the national region must return 'gb' and vc/os checkboxes must be enabled
        $('#nbn-vice-county-selector').change(function(){
            $('#nbn-region-selector').val("gb");
            $('#nbn-grid-map-vicecounty').prop('disabled', false);
            $('#nbn-grid-map-os').prop('disabled', false);
        });
    }
    
    function setupColourPickers(){
        $('#nbn-colour-picker-1, #nbn-colour-picker-2, #nbn-colour-picker-3, #nbn-colour-picker-outline').each(function(){
            $(this).ColorPicker(getColourPickerOptions($(this).attr('id')));
        });
    }
    
    function doOnChange(){
        var $form = $('#nbn-grid-map-form');
            
        //Apply any rules eg, must have at least one year band selected
        applyRules();
            
        //Deselect datasets if all are selected - requires jquery.dataset-selector-utils.js
        nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
        
        updateResolutionDropDown($form).complete(function(){
            //Do map refresh
            $('#nbn-grid-map-busy-image').show();
            $('#nbn-grid-map-image').attr('src',getURL($form));

            //Turn on all datasets if they are all off
            nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
        })   
    }
    
    function setupFormOnChange(){
        //The map should refresh when any form field is changed
        //except when the nbn-select-datasets-auto check box is deselected
        $('#nbn-grid-map-form :input').change(function(){
            if(($(this).attr('id')!='nbn-select-datasets-auto') || ($('#nbn-select-datasets-auto').is(':checked'))){
                var $input = $(this);
                if(nbn.portal.reports.utils.forms.isGridMapFormFieldValid($input)){
                    doOnChange();
                }
            }
            //Update resolution text used on data download section
            if($(this).attr('id') == 'nbn-grid-map-resolution'){
                $('#nbn-grid-map-resolution-download-text').text($(this).val());
            }
        });
    }
    
    function hideBusyImageOnMapLoad(){
        $('#nbn-grid-map-image').load(function(){
            $('#nbn-grid-map-busy-image').hide();
            if($('#nbn-grid-map-container').hasClass('nbn-map-image-container-startup-height')){
                $('#nbn-grid-map-container').removeClass('nbn-map-image-container-startup-height');
            }
        });
    }

    function addInitialMapImage(){
        $('#nbn-grid-map-busy-image').hide();
        $('#nbn-grid-map-image').attr('src','/img/ajax-loader-medium.gif');
        nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
        $('#nbn-grid-map-image').attr('src',getURL($('#nbn-grid-map-form')));
        nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
    }
    
    function setupDownloadSquaresButton(){
        $('#nbn-grid-map-squares-download').click(function(e){
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        var $form = $('#nbn-grid-map-form');
                        applyRules();
                        nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                        var tvk = $('#tvk').val(); 
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
                        var keyValuePairsWithBusinessLogic = getKeyValuePairsWithBusinessLogic(keyValuePairs);
                        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsWithBusinessLogic, false);
                        var url = $form.attr('api-server') + '/gridMapSquares/' + tvk + queryString;
                        nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
                        $(this).dialog("close");
                        window.location = url;
                    },
                    'Cancel': function(){
                        $(this).dialog("close");
                    }
                }
            });
            e.preventDefault();
        });
    }
    
    // Creates a link to the IMT retaining TVK / dataset filters
    function setupIMTLink() {
        $('#nbn-interactive-map').click(function() {            
            window.location = '/imt?mode=SPECIES&species=' + 
                    $('#tvk').val() + 
                    '&datasets=' + 
                    nbn.portal.reports.utils.datasetfields.getSelectedDatasets(); 
        });
    }
   
    function setupBetterAccessLink() {
        $('#nbn-request-better-access').click(function() {
            window.location = '/AccessRequest/Create?json={' + 
                    getTaxonJSON() + ',' +
                    nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() +
                    '}';
        });
    }
    
    function getTaxonJSON() {
        return 'taxon:{tvk:\'' + $('#tvk').val() + '\'}';
    }
    
    function setupDownloadRecordsLink() {
        $('#nbn-download-observations-button').click(function(e) {
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        var form = $('#nbn-site-report-form');
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
                        window.location = '/Download?json={' + 
                            getTaxonJSON() + ',' +
                            nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
                            '}';
                    },
                    'Cancel': function(){
                        $(this).dialog("close");
                    }
                }
            });
            e.preventDefault();
        });
    }

    $(document).ready(function(){
        $('#nbn-download-terms').hide();
        setupFormOnChange();
        setupColourPickers();
        setupRegionVCInteractions();
        setupDownloadSquaresButton();
        setupIMTLink();
        setupBetterAccessLink();
        setupDownloadRecordsLink();
        hideBusyImageOnMapLoad();
        addInitialMapImage();
    });
        
})(jQuery);