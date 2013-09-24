window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};

(function($) {   
    var PURPOSE_PERSONAL = 1;
    var PURPOSE_EDUCATION = 2;
    var PURPOSE_RESEARCH = 3;
    var PURPOSE_MEDIA = 4;
    var PURPOSE_COMMERCIAL_NGO = 5;
    var PURPOSE_LAND_MANAGEMENT = 6;
    var PURPOSE_DATA_COMMERCIAL = 7;
    var PURPOSE_DATA_NON_PROFIT = 8;
    var PURPOSE_SATUTORY = 9;
    
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
                    data['purposeID'] = [ $('#purposeID').val() ];
                } else {
                    data['purposeID'] = [];
                }
                if ($('#organisationID').val() > 0) {
                    data['organisationID'] = [$('#organisationID').val()];
                } else {
                    data['organisationID'] = [];
                }
                               
                data['dataset'] = { 'datasets' : getDatasets() };
                
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

        loadTableContent({'dataset' : { 'datasets' : getDatasets() }});
    });
    
    function getDatasets() {
        
        var d = [];
        $('.nbn-datatable').each(function(index, value) {
            d.push($(this).data('dataset'));
        });
        return d;
    }
    
    function buildStats(stats, user, org) {
        var records = 0;
        var downloads = 0;
        var purposes = [0,0,0,0,0,0,0,0,0];
        
        user.sort(function(a, b) { return ((a.total < b.total) ? 1 : ((a.total > b.total) ? -1 : ((a.totalAlt < b.totalAlt) ? 1 : ((a.totalAlt > b.totalAlt) ? -1 : 0))))});
        org.sort(function(a, b) { return ((a.total < b.total) ? 1 : ((a.total > b.total) ? -1 : ((a.totalAlt < b.totalAlt) ? 1 : ((a.totalAlt > b.totalAlt) ? -1 : 0))))});
        
        $.each(stats, function(index, value) {
            records += this.total;
            downloads += this.totalAlt;
            purposes[this.id - 1] = this.total;
        });
        
        var userTable = $('<table>')
                .append($('<tr>').append($('<td>')
                        .attr('colspan', '4')
                        .addClass('nbn-top-table-head')
                        .text('Top User Downloads')))
                .append($('<tr>')
                    .append($('<td>').text('#'))
                    .append($('<td>').text('Name'))
                    .append($('<td>').text('Downloads'))
                    .append($('<td>').text('Records'))
                );
        var orgTable = $('<table>')
                .append($('<tr>').append($('<td>')
                        .attr('colspan', '4')
                        .addClass('nbn-top-table-head')
                        .text('Top Organisation Downloads')))
                .append($('<tr>')
                    .append($('<td>').text('#'))
                    .append($('<td>').text('Name'))
                    .append($('<td>').text('Downloads'))
                    .append($('<td>').text('Records'))
                );
        
        $.each(user, function(index, value) {
            var i = index + 1;
            userTable.append($('<tr>')
                .append($('<td>').text(i))
                .append($('<td>').append($('<a>').attr('href', '/User/' + this.id).attr('target', '_blank').text(this.name)))
                .append($('<td>').text(this.totalAlt))
                .append($('<td>').text(this.total))
            );
        });
        
        $.each(org, function(index, value) {
            var i = index + 1;
            orgTable.append($('<tr>')
                .append($('<td>').text(i))
                .append($('<td>').append($('<a>').attr('href', '/Organisations/' + this.id).attr('target', '_blank').text(this.name)))
                .append($('<td>').text(this.totalAlt))
                .append($('<td>').text(this.total))
            );            
        });
        
        var statsTable = $('<table>');
        
        statsTable.append($('<tr>')
                .append($('<th>').text('Total Downloads'))
                .append($('<td>').text(downloads))
        );
            
        statsTable.append($('<tr>')
                .append($('<th>').text('Total Records Downloaded'))
                .append($('<td>').text(records))
        );
            
        statsTable.append($('<tr>'));
            
        statsTable.append($('<tr>')
                .append($('<th>').text('Records Downloaded per Purpose'))
                .append($('<td>').text())
        );
            
        addPurpose(statsTable, 'Personal interest', purposes, PURPOSE_PERSONAL - 1);         
        addPurpose(statsTable, 'Educational purposes', purposes, PURPOSE_EDUCATION - 1);      
        addPurpose(statsTable, 'Research and scientific analysis', purposes, PURPOSE_RESEARCH - 1);      
        addPurpose(statsTable, 'Media publication', purposes, PURPOSE_MEDIA - 1);      
        addPurpose(statsTable, 'Commercial and consultancy work', purposes, PURPOSE_COMMERCIAL_NGO - 1);      
        addPurpose(statsTable, 'Professional land management', purposes, PURPOSE_LAND_MANAGEMENT - 1);      
        addPurpose(statsTable, 'Data provision and interpretation (commercial)', purposes, PURPOSE_DATA_COMMERCIAL - 1);      
        addPurpose(statsTable, 'Data provision and interpretation (non-profit)', purposes, PURPOSE_DATA_NON_PROFIT - 1);      
        addPurpose(statsTable, 'Statutory work', purposes, PURPOSE_SATUTORY - 1);      
            
        $('#downloadStats').empty()
                .append(statsTable).append($('<br>'))
                .append($('<div>').addClass('nbn-top-table-div').append(userTable))
                .append($('<div>').addClass('nbn-top-table-div').append(orgTable));
    }
    
    function addPurpose(parent, text, purposes, index) {
        parent.append($('<tr>')
                .append($('<td>').addClass("nbn-download-purpose-span").text(text))
                .append($('<td>').text(purposes[index]))
        );   
    }
    
    
    function loadTableContent(data) {       
        var qString = '?json=' + JSON.stringify(data);
        
        $('.nbn-datatable').each(function(index) {
            var dataset = $(this).data('dataset');
            $('#nbn-downloads-div-' + dataset).empty().append($('<img>')
                    .attr('src', '/img/ajax-loader-medium.gif')
                    .attr('style', 'display:block; margin:auto;'));
            $.ajax({
                url: nbn.nbnv.api + '/taxonObservations/download/report/' + dataset + qString,
                success: function(data) {
                    downloadForDataset(
                            '#nbn-downloads-div-' + dataset,
                            'nbn-downloads-table-' + dataset,
                            data);
                }
            });
        });
        
        
        $('#downloadStats').empty().append($('<img>')
                    .attr('src', '/img/ajax-loader-medium.gif')
                    .attr('style', 'display:block; margin:auto;'));            
        $.ajax({
            url: nbn.nbnv.api + '/taxonObservations/download/report/combinedStats' + qString,
            success: function(data) {
                buildStats(data[0], data[1], data[2]);
            }
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
                    .append($('<td>').append($('<a>').attr('href', '/User/' + value.userID).attr('target', '_blank').text(value.forename + ' ' + value.surname)))
                    .append($('<td>').append($('<a>').attr('href', '/Organisations/' + value.organisationID).attr('target', '_blank').text(value.organisationName)))
                    .append($('<td>').text(value.downloadTimeString))
                    .append($('<td>').text(value.filterText))
                    .append($('<td>').text(value.purpose))
                    .append($('<td>').text(value.reason))
                    .append($('<td>').text(value.recordCount 
                        + ' records from this dataset in this download. This is ' 
                        + (value.recordCount / value.totalRecords * 100).toFixed(1)
                        + '% of this dataset and this compromises '
                        + (value.recordCount / value.totalDownloaded * 100).toFixed(1)
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


