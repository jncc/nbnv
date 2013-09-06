window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};

(function($) {   
    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function() {
        $('#startDate').datepicker({
            showOtherMonths: true,
            selectOtherMonths: true,
            altField: "#startDateHidden",
            altFormat: "yy-mm-dd",
            dateFormat: 'dd/mm/yy'
        });
        $("#startDate").change(function(){
            if (!$(this).val()) $("#startDateHidden").val('');
        });

        $('#endDate').datepicker({
            showOtherMonths: true,
            selectOtherMonths: true,
            altField: "#endDateHidden",
            altFormat: "yy-mm-dd",            
            dateFormat: 'dd/mm/yy'
        });
        $("#endDate").change(function(){
            if (!$(this).val()) $("#endDateHidden").val('');
        });
        
        $('#nbn-download-filter').validate({
            submitHandler: function(form) {
                var data = { };
                if ($('#startDateHidden').val() !== undefined && $('startDateHidden').val() !== "") {
                    data['startDate'] = $('#startDateHidden').val();
                }
                if ($('#endDateHidden').val() !== undefined && $('endDateHidden').val() !== "") {
                    data['endDate'] = $('#endDateHidden').val();                    
                }
                if ($('#purposeID').val() > 0) {
                    data['purposeID'] = $('#purposeID').val();
                }
                if ($('#organisationID').val() > 0) {
                    data['organisationID'] = $('#organisationID').val();
                }
                
                loadTableContent(data);
                return false;
            },
            rules: {
                startDate: {
                    date: true
                },
                endDate: {
                    date: true
                }
            },
            messages: {
                startDate: {
                    date: 'Must be valid date in the format DD-MM-YYYY'
                },
                endDate: {
                    date: 'Must be valid date in the format DD-MM-YYYY'
                }
            }
        });
        
        loadTableContent({});
    });
    
    function loadTableContent(data) {       
        $('.nbn-datatable').each(function(index) {
            var dataset = $(this).data('dataset');
            $('#nbn-downloads-div-' + dataset).empty().append($('<img>')
                    .attr('src', '/img/ajax-loader-medium.gif')
                    .attr('style', 'display:block; margin:auto;'));
            $.ajax({
                url: nbn.nbnv.api + '/taxonObservations/download/report/' + dataset,
                data: data,
                success: function(data) {
                    downloadForDataset(
                            '#nbn-downloads-div-' + dataset,
                            'nbn-downloads-table-' + dataset,
                            data);
                }
            });
        });
    }
    
    function downloadForDataset (divID, tableID, data) {
            var output = $('<table>').addClass('nbn-dataset-table')
                .attr('id', tableID)
                .append($('<thead>')
                    .append($('<tr>')
                        .append($('<th>').text('User'))
                        .append($('<th>').text('Organisation'))
                        .append($('<th>').text('Date'))
                        .append($('<th>').text('Download'))
                        .append($('<th>').text('Use Category'))
                        .append($('<th>').text('Use Reason'))
                        .append($('<th>').text('Statistics'))
                ));
            var outputBody = $('<tbody>');
            $.each(data, function(key, value){
                outputBody.append($('<tr>')
                    .append($('<td>').text(value.forename + ' ' + value.surname))
                    .append($('<td>').text(value.organisationName))
                    .append($('<td>').text(value.downloadTimeString))
                    .append($('<td>').text(value.filterText))
                    .append($('<td>').text(value.purpose))
                    .append($('<td>').text(value.reason))
                    .append($('<td>').text(value.recordCount 
                        + ' records from this dataset in this download. This is ' 
                        + (value.recordCount / value.totalRecords * 100)
                        + '% of this dataset and this compromises '
                        + (value.totalDownloaded / value.recordCount * 100)
                        + '% of the download')
                    )
                );
            });
            
            $(divID).empty().append(output.append(outputBody));
            generateDownloadReportDatatable('#' + tableID);
        }
        function generateDownloadReportDatatable(tableID) {
            $(tableID).dataTable({
                "aaSorting": [[2, "desc"]],
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
                "aoColumnDefs": [
                    {"sWidth": "10%", "aTargets": [0, 2]},
                    {"sWidth": "13%", "aTargets": [1, 4]},
                    {"sWidth": "18%", "aTargets": [3, 5, 6]}
                ]
            });
        }
    
})(jQuery);


