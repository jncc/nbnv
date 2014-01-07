window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};

(function($) {
    function setupUsernameDialog() {
        $(".nbn-request-username").click(function(e) {
            $('#nbn-further-info-user-name').text($(this).text());
            $('#nbn-further-info-user-email').text($(this).data("email"));
            $('#nbn-further-info-user-orgs').empty().append($('<img>').attr('src', '/img/ajax-loader.gif').attr('style', 'align: center'));

            $.ajax({
                type: 'GET',
                url: nbn.nbnv.api + '/user/' + $(this).data("id") + '/organisations',
                success: function(data) {
                    $('#nbn-further-info-user-org-desc').empty();
                    if (data.length > 0) {
                        var orgs = $('<span>');
                        $.each(data, function(index, value) {
                            orgs.append($('<span>').text(value.name)).append($('<br />'));
                        });
                        $('#nbn-further-info-user-orgs').empty().append(orgs);
                    } else {
                        $('#nbn-further-info-user-org-desc').text('User is not a member of any organisations');
                        $('#nbn-further-info-user-orgs').empty();
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
    }
})(jQuery);