(function($){
    
    function refreshObservationData(form){
        var apiServer = form.attr('api-server');
        var $dataContainer = $('#nbn-observation-container');
        var featureID = form.attr('featureID');
        var ptvk = form.attr('ptvk');
       
        //Add a busy image to data container whilst getting data
        $dataContainer.empty();
        $dataContainer.append('<img src="/img/ajax-loader-medium.gif" class="nbn-centre-element">');
        
        //Get data from api and add to container
        var keyValuePairsFromForm = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
        keyValuePairsFromForm['featureID'] = featureID;
        keyValuePairsFromForm['ptvk'] = ptvk;
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
        var url = apiServer + '/taxonObservations/datasets/observations' + queryString;
        $.getJSON(url, function(data){
            if(data.length > 0){
                $dataContainer.empty();
                $.each(data, function(key, dataset){
                    var providerName = getProviderName(apiServer, dataset.organisationID);
                    var $datasetContent = $('<div><div/>').addClass('tabbed');
                    $datasetContent.append('<h3>Records from dataset: <a href="/Datasets/' + dataset.key + '">' + dataset.title + '</a></h3>' +
                        '<table id="nbn-tabbed-heading-table"><tr><td>Provider:</td><td><a href="/Organisations/' + dataset.organisationID + '">' + providerName + '</a></tr>' +
                        '<tr><td>Your access:</td><td>Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</tr></td>');
                   var $table = $('<table></table>');
                   var $row = $('<tr></tr>');
                   $row.append($('<th></th>').text("Site name"));
                   $row.append($('<th></th>').text("Location"));
                   $row.append($('<th></th>').text("Date range"));
                   $row.append($('<th></th>').text("Date type"));
                   $row.append($('<th></th>').text("Sensitive"));
                   $row.append($('<th></th>').text("Absence"));
                   $table.append($row);
                   $.each(dataset.observations, function(key, observation){
                        var $row = $('<tr></tr>');
                        $row.append($('<td></td>').text('Where is the site name??!!'));
                        $row.append($('<td></td>').text(observation.identifier));
                        $row.append($('<td></td>').text(observation.startDate + 'to' + observation.endDate));
                        $row.append($('<td></td>').text(observation.dateTypekey));
                        $row.append($('<td></td>').text(observation.sensitive));
                        $row.append($('<td></td>').text(observation.absence));
                        $table.append($row);
                   });
                   $datasetContent.append($table);
                   $dataContainer.append($datasetContent);
                });
            }else{
                toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
            }
        });
    }
    
    function getProviderName(apiServer, providerID){
        var url = apiServer + '/organisations/' + providerID;
        var toReturn = '';
        $.ajax({
           type: 'GET',
           url: url,
           dataType: 'json',
           success: function(data){
               toReturn = data.name;
           },
           async: false
        });
        return toReturn;
    }

    $(document).ready(function(){
        
        var $siteReportForm = $('#nbn-site-report-form');
        nbn.portal.reports.site.initializeValidation();
        refreshObservationData($siteReportForm);

        $siteReportForm.submit(function(){
            //Requires jquery.dataset-selector-utils.js
            //            nbn.portal.reports.utils.DatasetFields.doDeselectDatasetKeys();
            var form = $(this);
            refreshObservationData(form);
            //            nbn.portal.reports.utils.DatasetFields.doSelectDatasetKeys();
            return false;
        });

    });
    
})(jQuery);