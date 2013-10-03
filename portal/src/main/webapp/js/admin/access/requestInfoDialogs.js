window.nbn = window.nbn || {};

(function($) {   
    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    
    $(document).ready(function() {
        $(".nbn-request-username").click(function(e){            
            $('#nbn-further-info-user-name').text($(this).text());
            $('#nbn-further-info-user-email').text($(this).data("email"));
            $('#nbn-further-info-user-orgs').empty().append($('<img>').attr('src', '/img/ajax-loader.gif').attr('style','align: center'));
            
            $.ajax({
                type: 'GET',
                url: nbn.nbnv.api + '/user/' + $(this).data("id") + '/organisations',
                success: function(data) {
                    if (data.length > 0) {
                        var orgs = $('<span>');
                        $.each(data, function(index, value) {
                           orgs.append($('<span>').text(value.name)).append($('<br />'));
                        });
                        $('#nbn-further-info-user-orgs').empty().append(orgs);
                    } else {
                        $('#nbn-further-info-user-org-desc').text('User is not a member of any organisations');
                        $('#nbn-further-info-user-orgs').empty()
                    }
                },
                error: function(data) {
                    $('#nbn-further-info-user-orgs').empty().append(
                        $('<p>').text('Could not load user organisations')
                    );
                }
            });
            
            $("#nbn-further-info-user").dialog({
                modal: true, 
                width: 650,
                buttons: { 
                    OK: function() { 
                        $(this).dialog("close"); 
                    }
                }
            });
            e.preventDefault();
        });
        
        $(".nbn-request-orgname").click(function(e){  
            var id = $(this).data('id');
            $('#nbn-further-info-org').empty().append($('<img>').attr('src','/img/ajax-loader-medium.gif'))
            
            $.ajax({
                type: 'GET',
                url: nbn.nbnv.api + "/organisations/" + id,
                success: function(data) {
                    if (data.hasLogo) {
                        var logo = $('<img>').attr('src',data.smallLogo).attr('style', 'float:left;')
                    }
                    var content = $('<table>')
                        .append($('<tr>')
                            .append($('<td>').text('Name:'))
                            .append($('<td>').append($('<span>').text(data.name).append(logo))))
                        .append($('<tr>')
                            .append($('<td>').text('Description:'))
                            .append($('<td>').text(data.summary)))
                        .append($('<tr>')
                            .append($('<td>').text('Contact:'))
                            .append($('<td>').text(data.contactName)))
                        .append($('<tr>')
                            .append($('<td>').text('Contact Email:'))
                            .append($('<td>').text(data.contactEmail)))
                    
                    var members = $('<div>').attr('id', 'nbn-further-info-org-members').append($('<br />')).append($('<img>').attr('src', '/img/ajax-loader.gif').attr('style','align: center'));
                    $('#nbn-further-info-org').empty().append(content).append(members);
                    
                    $.ajax({
                        type: 'GET',
                        url: nbn.nbnv.api + "/organisationMemberships/" + id,
                        success: function(data) {
                            var members = $('<table>').attr('id', 'nbn-further-info-org-members-table')
                                .append($('<thead>').append($('<tr>').append($('<th>').text('Member')).append($('<th>').text('Email'))));
                            var users = $('<tbody>');
                            $.each(data, function(index, value) {
                                users = users.append($('<tr>')
                                        .append($('<td>').text(value.user.forename + ' ' + value.user.surname))
                                        .append($('<td>').text(value.user.email)))
                            });
                            $('#nbn-further-info-org-members').empty().append(members.append(users));
                            $('#nbn-further-info-org-members-table').dataTable({
                                "aaSorting": [[1, "desc"]],
                                "iDisplayLength": 5,
                                "bJQueryUI": true
                            });
                        },
                        error: function(data) {
                            $('#nbn-further-info-org-members').empty().append($('<p>')).text('Could not load members of organisation');
                        }
                    });
                },
                error: function(data) {
                    $('#nbn-further-info-org').empty().append($('<p>').text('Could not load organisation data'));
                }
            });
            
            $("#nbn-further-info-org").dialog({
                modal: true, 
                width: 800,
                create: function() {
                    $(this).css("maxHeight", 600); 
                },
                buttons: { 
                    OK: function() { 
                        $(this).dialog("close"); 
                    }
                }
            });
            e.preventDefault();
        });        
    });
})(jQuery);