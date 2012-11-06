(function($){
    
    function refreshObservationData(form){
        var apiServer = form.attr('api-server');
        var $dataContainer = $('#nbn-site-report-data-container');
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
        console.log(url);
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
console.log(observation);
                        var $row = $('<tr></tr>');
                        $row.append($('<td></td>').text('Where is the site name??!!'));
                        $row.append($('<td></td>').text(observation.identifier));
                        $row.append($('<td></td>').text(observation.startDate + 'to' + observation.endDate));
                        $row.append($('<td></td>').text(observation.dateTypeKey));
                        $row.append($('<td></td>').text(observation.sensitive));
                        $row.append($('<td></td>').text(observation.absence));
                        $table.append($row);
                   });
                   $datasetContent.append($table);
                   $dataContainer.append($datasetContent);
                });
                
            //                toAppend += '<ul>';
            //                $.each(data, function(key, val){
            //                    toAppend += '<li><a href="/Reports/Sites/' + featureID + '/Groups/' + taxonOutputGroupKey + '/Species/' + val.taxon.taxonVersionKey + '/Observations">' + val.taxon.name + '</a>';
            //                });
            //                toAppend += '</ul>';
            }else{
                toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
            }
        //            $dataContainer.empty();
        //            $($dataContainer).append(toAppend);
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


    //    <#if datasets?has_content>
    //        <#list datasets as dataset>
    //            <#assign provider=json.readURL("${api}/organisations/${dataset.organisationID}")>
    //            <div class="tabbed">
    //                <h3>Records</h3>
    //                <table>
    //                    <tr>
    //                        <th>Dataset:</th>
    //                        <th><a href="/Datasets/${dataset.key}">${dataset.title}</a></th>
    //                    </tr>
    //                    <tr>
    //                        <th>Provider:</th>
    //                        <th><a href="/Organisations/${provider.id}">${provider.name}</a></th>
    //                    </tr>
    //                    <tr>
    //                        <th>Your access:</th>
    //                        <th>Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</a></th>
    //                    </tr>
    //                </table>
    //                <table>
    //                    <tr>
    //                        <th>Site name</th>
    //                        <th>Location</th>
    //                        <th>Date range</th>
    //                        <th>Date type</th>
    //                        <th>Sensitive</th>
    //                        <th >Absence</th>
    //                    </tr>
    //                    <#list dataset.observations as observation>
    //                        <tr>
    //                            <td>${observation.siteName!"Not available"}</td>
    //                            <td>${observation.identifier}</td>
    //                            <td>${observation.startDate} to ${observation.endDate}</td>
    //                            <td>${observation.dateTypekey}</td>
    //                            <td>${observation.sensitive?string}</td>
    //                            <td>${observation.absence?string}</td>
    //                        </tr>
    //                    </#list>
    //                </table>
    //            </div>
    //        </#list>
    //    <#else>
    //        <@report_utils.noRecordsInfoBox/>
    //    </#if>



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