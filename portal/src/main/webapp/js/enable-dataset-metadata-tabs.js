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
            
            //Render the datatable
            $(ui.panel).find('#nbn-generic-datatable').dataTable({
                "bJQueryUI": true,
                "aoColumnDefs": [
                {
                    "bVisible": false, 
                    "aTargets": [1]
                }
                ]
            });
            
            //Render the record count chart
            //            $.jqplot('nbn-record-chart',  [[[1, 2],[3,5.12],[5,13.1],[7,33.6],[9,85.9],[11,219.9]]]);
            //            var ajaxDataRenderer = function(url, plot, options) {
            //                var ret = null;
            //                $.ajax({
            //                    // have to use synchronous here, else the function 
            //                    // will return before the data is fetched
            //                    async: false,
            //                    url: url,
            //                    dataType:"json",
            //                    success: function(data) {
            //                        ret = data;
            //                    }
            //                });
            //                return ret;
            //            }; 
            //            jsonurl = 'http://localhost:8084/Datasets/TESTDS01/Records_Per_Year';
            //            $.jqplot('nbn-record-chart',jsonurl,{
            //                title: "AJAX JSON Data Renderer",
            //                dataRenderer: ajaxDataRenderer,
            //                dataRendererOptions: {
            //                    unusedOptionalUrl: jsonurl
            //                }
            //            });

            //nice example from:
            //http://stackoverflow.com/questions/3535680/problem-with-json-and-jqplot
            //see page as the code below is the broken example
            var line = [ ];
            $(function(){
                $.getJSON('http://localhost:8084/api/taxonDatasets/testds01/recordsPerYear', function(data){
                    var startYear = 1800;
                    var endYear = new Date().getFullYear();
                    var numYears = endYear - startYear + 1;
                                        
                    for(i=startYear;i<endYear+1;i++){
                        line.push([i,0]);
                    }
                    $.each(data, function(index, value){
                        var year = data[index].year;
                        var recordCount = data[index].recordCount;
                        lineIndex = year + 1 - startYear;
                        if(lineIndex > -1 && lineIndex < numYears){
                            if(year >= startYear && year <= endYear){
                                //Get rid of the spike in the test data
                                if(recordCount < 100000){
                                    line[lineIndex] = [year, recordCount];
                                }
                            }
                        }
                    });
                    $.jqplot('nbn-record-chart', [line], {
                        title:'Blah',
                        axes:{
                            xaxis:{
                                renderer:$.jqplot.DateAxisRenderer
                            }
                        },
                        series:[{
                            lineWidth:4, 
                            markerOptions:{
                                style:'square'
                            }
                        }]
                    });
                });
            });



        });
        
        
        
        
        $('#nbn-tabs').tabs();
    });
})(jQuery);
