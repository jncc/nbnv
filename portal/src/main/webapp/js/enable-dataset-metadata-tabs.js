/* This javascript will implement JQuery tabs for a div with id=nbn-tabs
 * The html inside the div must be as specified in http://jqueryui.com/demos/tabs/ 
 * It will also wrap up a table as a JQueryUI plugin DataTable provided it has the id 'nbn-generic-table'
 * Dependencies:
 * - /js/jquery.dataTables.min.js
 * - http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css
 */
(function($){
    $(document).ready(function(){

        $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
        
        $('#nbn-tabs').bind('tabsload', function(event, ui){
            var elementForRender = 'nbn-species-datatable'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                renderSpecies(elementForRender);
            }
            elementForRender = 'nbn-temporal-chart'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                renderTemporal(elementForRender);
            }
            elementForRender = 'nbn-surveys'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                renderSurveys();
            }
            elementForRender = 'nbn-attributes'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                renderAttributes();
            }
            elementForRender = 'nbn-site-boundaries'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                renderSiteBoundaries(elementForRender);
            }
            elementForRender = 'nbn-dataset-admin'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                renderDatasetAdmin(elementForRender);
            }
        });
        $('#nbn-tabs').tabs({
            spinner: 'Loading <img src="/img/ajax-loader.gif"/>',
            cache: true
        });
        applyTableEvenRowStyle();
    });
    
    function renderSpecies(elementForRender){
        $('#' + elementForRender).dataTable({
            "bJQueryUI": true,
            "iDisplayLength": 25,
            "sPaginationType": "full_numbers",
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aoColumnDefs": [
                {"sClass": ".nbn-datatable-generic", "aTargets": [0]},
                {"bVisible": false, "aTargets": [1]},
                {"sWidth": "15%", "sClass": ".nbn-datatable-generic", "aTargets": [2]},
                {"sWidth": "6%", "sClass": ".nbn-datatable-generic", "aTargets": [3]}
            ]
        });
        $('#' + elementForRender).width("100%");
    }
    
    //Render the temporal chart
    //nice example from:
    //http://stackoverflow.com/questions/3535680/problem-with-json-and-jqplot
    function renderTemporal(elementForRender){
        var localElementForRender = elementForRender;
        var datasetKey = $('#' + localElementForRender).attr('datasetKey');
        var chartData = [ ];
        var startYear = 1800;
        var endYear = new Date().getFullYear();
        var numYears = endYear - startYear + 1;
        var earliestYearInData = endYear;
        var latestYearInData = -1;
        for(i=startYear;i<endYear+1;i++){
            chartData.push([i,0]);
        }
        $.getJSON('/api/taxonDatasets/' + datasetKey + '/recordsPerYear', function(recordData){
            $.each(recordData, function(index, value){
                var year = value.year;
                var recordCount = value.recordCount;
                var chartDataIndex = year - startYear;

                //Log the earliest and latest years for later use
                if(year < earliestYearInData){
                    earliestYearInData = year;
                }
                if(year > latestYearInData){
                    latestYearInData = year;
                }
                        
                //Put the data from the json call into the chartData array
                if(chartDataIndex > -1 && chartDataIndex < numYears){
                    if(year >= startYear && year <= endYear){
                        chartData[chartDataIndex] = [year, recordCount];
                    }
                }
            });
            $.jqplot.LabelFormatter = function(format, val) {
                //Regex to put commas into integers (eg 25,264,390)
                return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            };
            $.jqplot(localElementForRender, [chartData], {
                title:'Number of records per year',
                seriesDefaults:{
                    lineWidth:4, 
                    markerOptions:{
                        show:false
                    }
                },
                highlighter: {
                    show: true
                },
                axesDefaults: {
                    tickOptions: {
                        fontSize: '10pt'
                    }
                },
                axes: {
                    xaxis: {
                        label: 'Year', 
                        labelRenderer: $.jqplot.CanvasAxisLabelRenderer, 
                        min: startYear, 
                        max: Math.ceil(endYear/10) * 10, 
                        tickInterval: '10',
                        tickOptions: {
                            formatString: '%s'
                        }
                    },
                    yaxis: {
                        label: 'Number of records', 
                        labelRenderer: $.jqplot.CanvasAxisLabelRenderer, 
                        min: 0, 
                        tickOptions: {
                            formatString: '%s', 
                            formatter: $.jqplot.LabelFormatter
                        }
                    }
                },
                cursor:{
                    show: true,
                    zoom: true,
                    showTooltip: false
                }
            });
            //Add the start and end years to the page
            $('#nbn-dataset-startyear').html(earliestYearInData);
            $('#nbn-dataset-endyear').html(latestYearInData);
        });
                
        //The extra information under the chart needs styling
        applyTableEvenRowStyle();
                
        //Toggle the table of record counts per year
        doCollapsibleList();

    }
    
    function renderSurveys(){
        doCollapsibleList();
        applyTableEvenRowStyle();
    }
    
    function renderAttributes(){
        applyTableEvenRowStyle();
    }
    
    function renderSiteBoundaries(elementForRender){
        $('#' + elementForRender).dataTable({
            "bJQueryUI": true,
            "iDisplayLength": 25,
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aoColumnDefs": [
                {
                    "aTargets": [1],
                    "bVisible": false 
                },
                {
                    "aTargets": [0],
                    "sWidth": "100%"
                }
            ]
        });
        $('#' + elementForRender).width("100%");
    }
    
    function renderDatasetAdmin(elementForRender) {
        $('#nbn-dataset-add-admin').autocomplete({
            source: $('#nbn-dataset-add-admin').data('url'),
            minLength: 3,
            select: function(event, ui) {
                event.preventDefault();
                $('#nbn-dataset-add-admin').val(ui.item.forename + ' ' + ui.item.surname);
                $('#nbn-dataset-add-admin').text(ui.item.forename + ' ' + ui.item.surname);
                $('#nbn-dataset-add-admin-id').val(ui.item.id);
            }
        })
        .data('autocomplete')._renderItem = function(ul, item) {
            var re = new RegExp(this.term, 'i');
            return $('<li></li>')
                    .data('item.autocomplete', item)
                    .append('<a><strong style="font-size: small;">' + replaceTerm(item.forename + ' ' + item.surname, re) + '</strong><br><span style="font-size: smaller;">' + replaceTerm(item.email, re) + '</span></a>')
                    .appendTo(ul);
        };

        $('#nbn-dataset-add-admin-submit').click(function(e) {
            $('#dialog-dataset-admin-text').text("Are you sure you would like to add this user as a Dataset Administrator?");

            $('#dialog-dataset-admin').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    'Add User': function() {
                        displaySendingRequestDialog('Adding User');
                        $.ajax({
                            type: 'POST',
                            contentType: "application/json; charset=utf-8",
                            url: $('#nbn-dataset-add-admin-submit').data('url'),
                            data: JSON.stringify({userID: $('#nbn-dataset-add-admin-id').val()}),
                            dataType: "json",
                            context: $(this),
                            success: function() {
                                $(this).dialog('close');
                                var current_index = $('#nbn-tabs').tabs('option','selected');
                                $('#nbn-tabs').tabs('load',current_index);                                
                                showSuccessDialog('Gave the selected user Admin Rights for this dataset');
                            },
                            error: function() {
                                $('#dialog-dataset-admin').dialog('close');
                                alert('ERROR: Could not add user ' + $('#nbn-dataset-add-admin').text());
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                }
            });
        });
        
        $('.nbn-dataset-admin-remove').click(function(e) {
            e.preventDefault();
            
            var name = $(this).data('name');
            var id = $(this).data('id');

            $('#dialog-dataset-admin-text').text(
                    'Are you sure you want to revoke this user\'s (' + name + 
                    ') Dataset Administration Privileges?');

            $('#dialog-dataset-admin').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Revoke Admin Rights": function() {
                        displaySendingRequestDialog("Revoking Admin Rights");
                        $.ajax({
                            type: "POST",
                            contentType: "application/json; charset=utf-8",
                            url: $("#nbn-dataset-admin").data("remove"),
                            data: JSON.stringify({userID: id}),
                            dataType: "json",
                            success: function(result) {
                                var current_index = $('#nbn-tabs').tabs('option','selected');
                                $('#nbn-tabs').tabs('load',current_index);
                                $('#dialog-dataset-admin').dialog('close');
                                showSuccessDialog('Revoked Admin Rights for ' + name);
                            },
                            error: function(result) {
                                $("#dialog-dataset-admin").dialog("close");
                                alert("ERROR: Could not Revoke rights for the user " + name + ", please try again later");
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        });

        $('#' + elementForRender).dataTable({
            "bJQueryUI": true,
            "iDisplayLength": 10,
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aaSorting": [[ 1, "asc" ]],
            "aoColumnDefs": [
                {
                    "aTargets": [0],
                    "bVisible": false 
                }
            ]
        });
        $('#' + elementForRender).width("100%");
    }
    
    function replaceTerm(input, re) {
        return input.replace(re, '<span style="font-weight:bold;">' + '$&' + '</span>');
    }
    
    function showSuccessDialog(text) {
        $("#dialog-dataset-admin-success-text").text(text);
        $("#dialog-dataset-admin-success").dialog({
            resizable: false,
            width: 400,
            modal: true,
            buttons: {
                "OK": function() {
                    $(this).dialog('close');
                }
            }
        });
    }
    
    function doCollapsibleList(ui){
        $('.collapsible-list ul').hide(); //first off. Hide all the sub lists

        $('.collapsible-list ul').each(function(){
            var list = $(this);
                list.parent().prepend(     //put the container before the sublist
                    $("<span>")
                    .addClass("collapsible-list-icon icons-expand")
                    .click(function(){  //register a click listener which toggles the icon and list visibility
                        $('.collapsible-list-icon', this).toggleClass("icons-expand icons-collapse");
                        $('~ ul', this).stop().slideToggle('slow');
                    })
                    );
        });
    }
    
    function applyTableEvenRowStyle(){
        $(".nbn-simple-table tr:even").addClass("ui-state-highlight");
    }
    
})(jQuery);
