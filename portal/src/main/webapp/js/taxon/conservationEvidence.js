(function($){
    $(document).ready(function(){
        var taxonName = $('#externalLinksDiv').data('taxon');
        var url = 'http://www.conservationevidence.com/binomial/search?name=' + taxonName;
        
        $.ajax({
            type: 'GET',
            dataType: 'jsonp',
            url: url,
            success: function(retVal) {                
                if (retVal.total_results === 0) {
                    if ($('#externalTable tr').length === 0) {
                        $('#externalLoading').empty().text("None");
                        $('#externalLoading').show();
                    }
                } else {                
                    $('#externalLoading').empty().append($('<a>')
                                    .attr('href', retVal.results_url)
                                    .attr('target', '_blank')
                                    .text('ConservationEvidence.com has ' + retVal.total_results + ' articles on ' + $('#externalLinksDiv').data('taxonprint')));
                }
            },
            error: function() {
                $('#externalLoading').empty().text("Could not load external links from the Conservation Evidence Website");
                
            }
        })
        $('#ExternalLinksTable')
    });
        
})(jQuery);
