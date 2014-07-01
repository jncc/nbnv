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
        if (nbn.portal.reports.utils.datasetfields.getSelectedDatasetsCount() > 0) {
            $.getJSON(url, function(data){
                if(data.length > 0){
                    numSites = data.length;
                    toAppend += '<table id="nbn-species-table" class="nbn-simple-table"><thead><tr><th>Site name</th><th>Dataset</th><th>Category</th></thead><tbody>';
                    $.each(data, function(key, val){
                        toAppend += '<tr><td><a href="/Reports/Sites/' + val.identifier + '/Groups/' + taxonOutputGroupKey + '/Species/' + ptvk + '/Observations">' + val.name + '</a></td>';
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
        } else {
            $dataContainer.empty();
            $($dataContainer).append(nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox());            
        }
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
            if(nbn.portal.reports.utils.forms.isSiteReportFormFieldValid($input)){
                refreshSiteListData($('#nbn-species-site-list-form'));
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
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
//                        keyValuePairs.featureID = $form.attr("featureID");
//                        keyValuePairs.taxonOutputGroup = $form.attr("taxonOutputGroupKey");
                        keyValuePairs['datasetKey'] = nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
                        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairs, false);
                        var url = $form.attr('api-server') + '/taxa/' + ptvk + '/siteBoundaries/download/' + queryString;
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
    
    function setupBetterAccessLink() {
        $('#nbn-request-better-access').click(function() {
            var form = $('#nbn-species-site-list-form');
            var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
            window.open('/AccessRequest/Create?json={' +
                    'taxon:{tvk:\'' + form.attr('ptvk') + '\'},' +
                    // Disabled as it selects publicly applicable
                    //nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() +
                    'dataset:{all:true},' +
                    nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) +
                    '}');
        });
    }

    function doFirstVisitToPage(){
        refreshSiteListData($('#nbn-species-site-list-form'));
    }
    
    $(document).ready(function(){
        $('#nbn-download-terms').hide();
        setupFormOnChange();
        setupDownloadSitesButton();
        setupBetterAccessLink();
        doFirstVisitToPage();
    });
})(jQuery);