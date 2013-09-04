(function($) {
    namespace("nbn.portal.reports.downloads.utils", {
        generateDownloadReportDatatable: function(tableID) {
            $(tableID).dataTable({
                "aaSorting": [[2, "desc"]],
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
                "aoColumnDefs": [
                    {"sWidth": "10%", "aTargets": [0, 1, 2, 4]},
                    {"sWidth": "20%", "aTargets": [3, 5, 6]}
                ]
            });
        }
    });
})(jQuery);