(function($){
    $(document).ready(function(){
        $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
        renderDatasetAdmin('nbn-dataset-admin');
    });
    
    function renderDatasetAdmin(elementForRender) {
        $('#nbn-dataset-add-admin').autocomplete({
            source: $('#nbn-dataset-add-admin').data('url'),
            minLength: 3,
            select: function(event, ui) {
                event.preventDefault();
                $('#nbn-dataset-add-admin').val(ui.item.forename + ' ' + ui.item.surname);
                //$('#nbn-dataset-add-admin').text(ui.item.forename + ' ' + ui.item.surname);
                $('#nbn-dataset-add-admin-id').val(ui.item.id);
            }
        })
        .data('autocomplete')._renderItem = function(ul, item) {
            var re = new RegExp(this.term, 'i');
            return $('<li></li>')
                    .data('item.autocomplete', item)
                    .append('<a><strong style="font-size: small;">' + replaceTerm(item.forename + ' ' + item.surname, re) + '</strong><br><span style="font-size: smaller;">' + replaceTerm(item.email, re) + '</span></a>')
                    .appendTo(ul);
        };

        $('#nbn-dataset-add-admin-submit').click(function(e) {
            $('#dialog-dataset-admin-text').text("Are you sure you would like to add this user as a Dataset Administrator?");

            $('#dialog-dataset-admin').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    'Add User': function() {
                        displaySendingRequestDialog('Adding User');
                        $.ajax({
                            type: 'POST',
                            contentType: "application/json; charset=utf-8",
                            url: $('#nbn-dataset-add-admin-submit').data('url'),
                            data: JSON.stringify({userID: $('#nbn-dataset-add-admin-id').val()}),
                            dataType: "json",
                            context: $(this),
                            success: function() {
                                location.reload();
                            },
                            error: function() {
                                $('#dialog-dataset-admin').dialog('close');
                                alert('ERROR: Could not add user ' + $('#nbn-dataset-add-admin').text());
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                }
            });
        });
        
        $('.nbn-dataset-admin-remove').click(function(e) {
            e.preventDefault();
            
            var name = $(this).data('name');
            var id = $(this).data('id');

            $('#dialog-dataset-admin-text').text(
                    'Are you sure you want to revoke this user\'s (' + name + 
                    ') Dataset Administration Privileges?');

            $('#dialog-dataset-admin').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Revoke Admin Rights": function() {
                        displaySendingRequestDialog("Revoking Admin Rights");
                        $.ajax({
                            type: "POST",
                            contentType: "application/json; charset=utf-8",
                            url: $("#nbn-dataset-admin").data("remove"),
                            data: JSON.stringify({userID: id}),
                            dataType: "json",
                            success: function(result) {
                                location.reload();
                            },
                            error: function(result) {
                                $("#dialog-dataset-admin").dialog("close");
                                alert("ERROR: Could not Revoke rights for the user " + name + ", please try again later");
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        });

        $('#' + elementForRender).dataTable({
            "bJQueryUI": true,
            "iDisplayLength": 10,
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aaSorting": [[ 1, "asc" ]],
            "aoColumnDefs": [
                {
                    "aTargets": [0],
                    "bVisible": false 
                }
            ]
        });
        $('#' + elementForRender).width("100%");
    }
    
    function replaceTerm(input, re) {
        return input.replace(re, '<span style="font-weight:bold;">' + '$&' + '</span>');
    }
})(jQuery);