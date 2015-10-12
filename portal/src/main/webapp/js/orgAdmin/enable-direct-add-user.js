/** 
 * This javascript is used to turn a list of users in a table into a Jquery plugin DataTable 
 * The first column will be invisible and The table id must be 'nbn-users-datatable'
 */

(function($) {
    $(document).ready(function() {
        $('#nbn-org-add-user').autocomplete({
            source: $('#nbn-org-add-user').data('url'),
            minLength: 3,
            select: function(event, ui) {
                event.preventDefault();
                $('#nbn-org-add-user').val(ui.item.forename + ' ' + ui.item.surname);
                $('#nbn-org-add-user').text(ui.item.forename + ' ' + ui.item.surname);
                $('#nbn-org-add-user-id').val(ui.item.id);
            }
        })
        .data('uiAutocomplete')._renderItem = function(ul, item) {
            var re = new RegExp(this.term, 'i');
            return $('<li></li>')
                    .data('item.autocomplete', item)
                    .append('<a><strong style="font-size: small;">' + replaceTerm(item.forename + ' ' + item.surname, re) + '</strong><br><span style="font-size: smaller;">' + replaceTerm(item.email, re) + '</span></a>')
                    .appendTo(ul);
        };

        $('#nbn-org-add-user-submit').click(function(e) {
            $('#nbn-org-add-user-dialog-name').text($('#nbn-org-add-user').text());

            $('#nbn-org-add-user-dialog').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    'Add User': function() {
                        displaySendingRequestDialog('Adding User');
                        $.ajax({
                            type: 'POST',
                            contentType: "application/json; charset=utf-8",
                            url: $('#nbn-org-add-user-submit').data('url'),
                            data: JSON.stringify({userID: $('#nbn-org-add-user-id').val()}),
                            dataType: "json",
                            success: function() {
                                location.reload();
                            },
                            error: function() {
                                $('#nbn-org-add-user-dialog').dialog('close');
                                alert("ERROR: Could not add user " + $('#nbn-org-add-user').text());
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                }
            });
        });
    });

    function replaceTerm(input, re) {
        return input.replace(re, '<span style="font-weight:bold;">' + '$&' + '</span>');
    }
})(jQuery);