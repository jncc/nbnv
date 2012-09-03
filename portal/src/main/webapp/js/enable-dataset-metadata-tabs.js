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
        
//        $.ajaxSetup({
//            beforeSend: function(){
//                $('#progress').show();
//            },
//            complete: function(){
//                $('#progress').hide();
//            }
//        });
        
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
            elementForRender = 'nbn-record-chart'
            if($(ui.panel).find('#' + 'nbn-record-chart').length > 0){
                var datasetKey = $('#' + 'nbn-record-chart').attr('datasetKey');
                var chartData = [ ];
                $.getJSON('http://localhost:8084/api/taxonDatasets/' + datasetKey + '/recordsPerYear', function(data){
                    var startYear = 1900;
                    var endYear = new Date().getFullYear();
                    var numYears = endYear - startYear + 1;
                    for(i=startYear;i<endYear+1;i++){
                        chartData.push([i,0]);
                    }
                    $.each(data, function(index, value){
                        var year = data[index].year;
                        var recordCount = data[index].recordCount;
                        lineIndex = year + 1 - startYear;
                        if(lineIndex > -1 && lineIndex < numYears){
                            if(year >= startYear && year <= endYear){
                                //Get rid of the spike in the test data
                                if(recordCount < 100000){
                                    chartData[lineIndex] = [year, recordCount];
                                }else{
//                                    alert('getting rid of spike in test data');
                                }
                            }
                        }
                    });
                    $.jqplot.LabelFormatter = function(format, val) {
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
                                max: endYear, 
                                ticks: [1900,1910,1920,1930,1940,1950,1960,1970,1980,1990,2000,2010,2020], 
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
                        }
                    });
                });
            }
        });
        $('#nbn-tabs').tabs({
            spinner: 'Loading <img src="/img/ajax-loader.gif"/>'
        });
    });
})(jQuery);
