window.nbn = window.nbn || {};

(function($) {   
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
            $('#nbn-further-info-org-name').text($(this).text());
            $('#nbn-further-info-org-contact-name').text($(this).data("contact-name"));
            $('#nbn-further-info-org-contact').text($(this).data("contact"));
            
            $("#nbn-further-info-org").dialog({
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
    });
})(jQuery);