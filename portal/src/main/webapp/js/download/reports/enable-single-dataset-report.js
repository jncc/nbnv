(function($) {
    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function() {
        nbn.portal.reports.downloads.utils.generateDownloadReportDatatable('#datatable');
    });
})(jQuery);


