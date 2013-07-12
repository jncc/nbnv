function displaySendingRequestDialog(text) {
    $('<div class="ui-spinner-containing-div"><span><span class="ui-loading"></span>' + text + '</span></div>').insertBefore('.ui-dialog-buttonset');
    $(".ui-dialog-buttonpane button").button("disable");
}