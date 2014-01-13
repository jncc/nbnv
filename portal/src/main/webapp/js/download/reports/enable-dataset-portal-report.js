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
        $("#startDate").change(function() {
            if (!$(this).val())
                $("#startDateHidden").val('');
        });

        $('#endDate').datepicker({
            showOtherMonths: true,
            selectOtherMonths: true,
            altField: "#endDateHidden",
            altFormat: "yy-mm-dd",
            dateFormat: 'dd/mm/yy'
        });
        $("#endDate").change(function() {
            if (!$(this).val())
                $("#endDateHidden").val('');
        });
        
        var startDate = "";
        var endDate = "";

        $('#nbn-download-filter').validate({
            submitHandler: function(form) {
                if ($('#startDateHidden').val() !== undefined && $('startDateHidden').val() !== "") {
                    startDate = $('#startDateHidden').val();
                }
                if ($('#endDateHidden').val() !== undefined && $('endDateHidden').val() !== "") {
                    endDate = $('#endDateHidden').val();
                }

                loadTableContent(startDate, endDate);
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

        loadTableContent(startDate,endDate);
    });

    function getDatasets() {

        var d = [];
        $('.nbn-datatable').each(function(index, value) {
            d.push($(this).data('dataset'));
        });
        return d;
    }

    function loadTableContent(startDate, endDate) {
        var qString = "";
        if (startDate !== "" && endDate !== "")
            qString = "?startDate=" + startDate + "&endDate=" + endDate;

        $('.nbn-datatable').each(function(index) {
            var dataset = $(this).data('dataset');
            $('#nbn-downloads-div-' + dataset).empty().append($('<img>')
                    .attr('src', '/img/ajax-loader-medium.gif')
                    .attr('style', 'display:block; margin:auto;'));
            $.ajax({
                url: nbn.nbnv.api + '/apiViews/' + dataset + qString,
                success: function(data) {
                    downloadForDataset(
                            '#nbn-downloads-div-' + dataset,
                            'nbn-downloads-table-' + dataset,
                            data);
                }
            });
        });
    }

    function downloadForDataset(divID, tableID, data) {
        var output = $('<table>').addClass('nbn-dataset-table')
                .attr('id', tableID)
                .append($('<thead>')
                .append($('<tr>')
                .append($('<th>').text('User'))
                .append($('<th>').text('IP Address'))
                .append($('<th>').text('Filter'))
                .append($('<th>').text('Date'))
                .append($('<th>').text('Statistics'))
                ));
        var outputBody = $('<tbody>');
        $.each(data, function(key, value) {
            outputBody.append($('<tr>')
                    .append($('<td>').append($('<a>').text(value.forename + ' ' + value.surname).attr('class', 'nbn-request-username').attr('data-email', value.email).attr('data-id', value.userID).attr('href', '#')))
                    .append($('<td>').text(value.ip))
                    .append($('<td>').text(value.filterText))
                    .append($('<td>').text(value.viewTimeString))
                    .append($('<td>').text(value.recordCount
                    + ' records from this dataset in this view. This is '
                    + ((value.recordCount / value.totalDatasetRecords) * 100).toFixed(1)
                    + '% of this dataset and this comprises '
                    + ((value.recordCount / value.viewed) * 100).toFixed(1)
                    + '% of the view')
                    )
                );
        });

        $(divID).empty().append(output.append(outputBody));
        
        // Setup user information dialogs - External Dependency (dialog_utils.js)
        setupUsernameDialog(nbn.nbnv.api);
        
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
                    {"sWidth": "15%", "aTargets": [0, 1, 3]},
                    {"sWidth": "30%", "aTargets": [2]},
                    {"sWidth": "25%", "aTargets": [4]}
                ]
        });
    }

 
})(jQuery);


