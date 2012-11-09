(function($){
    
    var apiServer;
    
    function refreshObservationData(form){
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
            $dataContainer.empty();
            if(data.length > 0){
                $.each(data, function(key, dataset){
                    var providerName = getProviderName(dataset.organisationID);
                    var $attributeDropDown = getAttributeDropDown(dataset.key, queryString);
                    var $datasetContent = $('<div><div/>').addClass('tabbed');
                    $datasetContent.append('<h3>Records from dataset: <a href="/Datasets/' + dataset.key + '">' + dataset.title + '</a></h3>' +
                        '<table id="nbn-tabbed-heading-table"><tr><td>Provider:</td><td><a href="/Organisations/' + dataset.organisationID + '">' + providerName + '</a></tr>' +
                        '<tr><td>Your access:</td><td>Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</tr></td>');
                    var $table = $('<table></table>');
                    var $row = $('<tr></tr>');
                    $row.append($('<th></th>').text("Site name"));
                    $row.append($('<th></th>').text("Location"));
                    $row.append($('<th></th>').text("Start date"));
                    $row.append($('<th></th>').text("End date"));
                    $row.append($('<th></th>').text("Date type"));
                    $row.append($('<th></th>').text("Recorder"));
                    $row.append($('<th></th>').text("Determiner"));
                    $row.append($('<th></th>').text("Absence"));
                    $row.append($('<th></th>').text("Sensitive"));
                    $row.append($('<th></th>').text("Public"));
                    if($attributeDropDown){
                        $row.append($attributeDropDown);
                    }
                    $table.append($row);
                    $.each(dataset.observations, function(key, observation){
                        var $row = $('<tr></tr>');
                        $row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.siteName,'Unavailable')));
                        $row.append($('<td></td>').text(observation.location));
                        $row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDateText(new Date(observation.startDate))));
                        $row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDateText(new Date(observation.endDate))));
                        $row.append($('<td></td>').text(observation.dateTypekey));
                        $row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.recorder,'Unavailable')));
                        $row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.determiner,'Unavailable')));
                        $row.append($('<td></td>').text(observation.absence));
                        $row.append($('<td></td>').text(observation.sensitive));
                        $row.append($('<td></td>').text(!observation.fullVersion));
                        if($attributeDropDown){
                            $row.append($('<td id="' + observation.observationID + '"></td>').addClass('nbn-attribute-td'));
                        }
                        $table.append($row);
                    });
                    $datasetContent.append($table);
                    $dataContainer.append($datasetContent);
                    if($attributeDropDown){
                      addAttributeData(dataset.key, $attributeDropDown.val(), queryString);
                    }

                });
            }else{
                $dataContainer.append(nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox());
            }
        });
    }
    
    function getAttributeDropDown(datasetKey, queryString){
        var attributes = getDatasetAttributes(datasetKey);
        if(!$.isEmptyObject(attributes)){
            var $select = $("<select id='nbn-site-observation-attribute-select'></select>")
                .change(function(){
                    $('#nbn-attribute-dropdown-busy-image').attr('src','/img/ajax-loader.gif');
                    addAttributeData(datasetKey, $(this).val(), queryString);
                });
            $.each(attributes, function(index, attribute){
                $select.append($("<option></option>")
                .attr("value",attribute.attributeID)
                .text(attribute.label));
            });
//            $select.add($('<img src="" id="nbn-attribute-dropdown-busy-image">'));
            return $('<th></th>').append($select);
        }else{
            return false;
        }
    }
    
    function getDatasetAttributes(datasetKey){
        var url = apiServer + '/taxonDatasets/' + datasetKey + '/attributes';
        var toReturn = '';
        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'json',
            success: function(data){
                toReturn = data;
            },
            async: false
        });
        return toReturn;
    }
    
    function addAttributeData(datasetKey, attributeID, queryString){
        var url = apiServer + '/taxonObservations/' + datasetKey + '/attributes/' + attributeID + queryString;
        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'json',
            success: function(data){
                $('.nbn-attribute-td').empty();
                $.each(data, function(index, observationAttribute){
                    $('#' + observationAttribute.observationID).text(observationAttribute.textValue);
                });
            },
            async: false
        });
    }
    
    function getProviderName(providerID){
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
    
    function setupFormOnChange(){
        $('#nbn-site-report-form :input').change(function(){
            refreshObservationData($('#nbn-site-report-form'));
        });

    }

    function doFirstVisitToPage(){
        nbn.portal.reports.site.initializeValidation();
        refreshObservationData($('#nbn-site-report-form'));
    }
    
    $(document).ready(function(){
        apiServer = $('#nbn-site-report-form').attr('api-server');
        setupFormOnChange();
        doFirstVisitToPage();
    });
    
})(jQuery);