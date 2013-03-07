(function($){
    
    function refreshSiteListData(form){
        var $dataContainer = $('#nbn-species-site-list-data');
        var ptvk = form.attr('ptvk');
        var taxonOutputGroupKey = form.attr('taxonOutputGroupKey');
       
        //Add busy image to data container whilst getting data
        $dataContainer.empty();
        $dataContainer.append('<img src="/img/ajax-loader-medium.gif" class="nbn-centre-element">');
        
        //Get data from api and add to container
        var keyValuePairsFromForm = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
        var url = form.attr('api-server') + '/taxa/' + ptvk + '/siteBoundaries' + queryString;
        var toAppend = '';
        var numSites = 0;
        var datatableDisplayThreshold = 25;
        $.getJSON(url, function(data){
            if(data.length > 0){
                numSites = data.length;
                toAppend += '<table id="nbn-species-table" class="nbn-simple-table"><thead><tr><th>Site name</th><th>Dataset</th><th>Category</th></thead><tbody>';
                $.each(data, function(key, val){
                    //Observations disabled for Christmas release 2012
                    toAppend += '<tr><td><a href="/Reports/Sites/' + val.identifier + '/Groups/' + taxonOutputGroupKey + '/Species">' + val.name + '</a></td>';
//                    toAppend += '<tr><td><a href="/Reports/Sites/' + val.identifier + '/Groups/' + taxonOutputGroupKey + '/Species/' + ptvk + '/Observations">' + val.name + '</a></td>';
                    toAppend += '<td><a href="/Datasets/' + val.siteBoundaryDatasetKey + '">' + val.siteBoundaryDataset.title + '</a></td>';
                    toAppend += '<td>' + val.siteBoundaryCategory.name + '</td></tr>';
                });
                toAppend += '</tbody></table>';
            }else{
                toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
            }
            $dataContainer.empty();
            $($dataContainer).append(toAppend);
            if(numSites > datatableDisplayThreshold){
                addDataTable();
            }
        });
    }

    function addDataTable(){
        $('#nbn-species-table').dataTable({
            "bJQueryUI": true,
            "sPaginationType": "full_numbers",
            "oLanguage": {
                "sLengthMenu": "Show _MENU_ sites", 
                "sSearch": 'Search list',
                "sInfo": "Showing _START_ to _END_ of _TOTAL_ sites",
                "sInfoFiltered": " (filtered from _MAX_ total sites)"
            },
            "iDisplayLength": 25,
            "bSortClasses": false,
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aoColumnDefs": [
                            {"sWidth": "33%", "aTargets": [0,1,2]}
            ]
        });
    }

    function setupFormOnChange(){
        //The list refresh when any form field is changed and has valid data
        //except when the nbn-select-datasets-auto check box is deselected
        $('#nbn-species-site-list-form :input').change(function(){
            var $input = $(this);
            if(($input.attr('id')!='nbn-select-datasets-auto') || ($('#nbn-select-datasets-auto').is(':checked'))){
                if(nbn.portal.reports.utils.forms.isSiteReportFormFieldValid($input)){
                    //Requires jquery.dataset-selector-utils.js
                    nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                    refreshSiteListData($('#nbn-species-site-list-form'));
                    nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
                }
            }
        });
    }

   function setupDownloadSitesButton(){
        $('#nbn-site-report-download-button').click(function(e){
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        var $form = $('#nbn-species-site-list-form');
                        var ptvk = $('#nbn-species-site-list-form').attr('ptvk');
                        nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
//                        keyValuePairs.featureID = $form.attr("featureID");
//                        keyValuePairs.taxonOutputGroup = $form.attr("taxonOutputGroupKey");
                        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairs, false);
                        var url = $form.attr('api-server') + '/taxa/' + ptvk + '/siteBoundaries/download/' + queryString;
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

    function doFirstVisitToPage(){
        refreshSiteListData($('#nbn-species-site-list-form'));
    }
    
    $(document).ready(function(){
        setupFormOnChange();
        setupDownloadSitesButton();
        doFirstVisitToPage();
    });
})(jQuery);