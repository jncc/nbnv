/* This javascript will implement JQuery tabs for a div with id=nbn-tabs
 * The html inside the div must be as specified in http://jqueryui.com/demos/tabs/ 
 * It will also wrap up a table as a JQueryUI plugin DataTable provided it has the id 'nbn-generic-table'
 * Dependencies:
 * - /js/jquery.dataTables.min.js
 * - http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css
 */
(function($){
    $(function(){
        $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
        
        $('#nbn-tabs').bind('tabsload', function(event, ui){
            
            //Render the datatable
            var elementForRender = 'nbn-species-datatable'
            if($(ui.panel).find('#' + elementForRender).length > 0){
                $(ui.panel).find('#' + elementForRender).dataTable({
                    "bJQueryUI": true,
                    "aoColumnDefs": [
                    {
                        "bVisible": false, 
                        "aTargets": [1]
                    }
                    ]
                });
            }
            
            //Render the temporal chart
            //nice example from:
            //http://stackoverflow.com/questions/3535680/problem-with-json-and-jqplot
            elementForRender = 'nbn-temporal-chart'
            if($(ui.panel).find('#' + 'nbn-temporal-chart').length > 0){
                var datasetKey = $('#' + 'nbn-temporal-chart').attr('datasetKey');
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
                                //Get rid of the spike in the test data
                                //[TODO get rid of this if statement]
                                if(recordCount < 100000){
                                    chartData[chartDataIndex] = [year, recordCount];
                                }
                            }
                        }
                    });
                    $.jqplot.LabelFormatter = function(format, val) {
                        //Regex to put commas into integers (eg 25,264,390)
                        return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    };
                    $.jqplot(elementForRender, [chartData], {
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
                $(".nbn-simple-table tr:even").addClass("ui-state-highlight");
                
                //Toggle the table of record counts per year
                $('.collapsible-list ul').hide(); //first off, hide all the sub lists
                $('.collapsible-list ul').each(function(){
                    var list = $(this);
                    list.parent().prepend(//put the container before the sublist
                        $("<span>")
                            .addClass("collapsible-list-icon icons-expand")
                            .click(function(){  //register a click listener which toggles the icon and list visibility
                                $('.collapsible-list-icon', this).toggleClass("icons-expand icons-collapse");
                                $('~ ul', this).stop().slideToggle('slow');
                            })
                    );
                });
            }
        });
        $('#nbn-tabs').tabs({
            spinner: 'Loading <img src="/img/ajax-loader.gif"/>'
        });
    });
})(jQuery);
