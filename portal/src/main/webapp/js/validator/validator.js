(function($) {

    // Variable to store your files
    var files;

    var headings = {
        OCCURRENCEID: 'Occurence ID',
        EVENTDATE: 'Event Date',
        TAXONID: 'Taxon ID',
        LOCATIONID: 'Location ID',
        LOCALITY: 'Locality',
        VERBATIMLATITUDE: 'Verbatim Latitude',
        VERBATIMLONGITUDE: 'Verbatim Longitude',
        VERBATIMSRS: 'Verbatim SRS',
        RECORDEDBY: 'Recorded By',
        IDENTIFIEDBY: 'Identified By',
        OCCURRENCESTATUS: 'Occurence Status',
        COLLECTIONCODE: 'Collection Code',
        EVENTID: 'Event ID',
        EVENTDATETYPECODE: 'Event Date Type Code',
        EVENTDATESTART: 'Event Date Start',
        EVENTDATEEND: 'Event Date End',
        GRIDREFERENCE: 'Grid Reference',
        GRIDREFERENCETYPE: 'Grid Reference Type',
        GRIDREFERENCEPRECISION: 'Grid Reference Precision',
        SITEFEATUREKEY: 'Site Feature Key',
        SENSITIVEOCCURRENCE: 'Sensitive Occurence',
        DYNAMICPROPERTIES: 'Dynamic Properties',
        TAXONNAME: 'Taxon Name',
        ATTRIBUTE: 'Attribute'
    };

    // Grab the files and set them to our variable
    function prepareUpload(event) {
        files = event.target.files;
    }

    // Catch the form submit and upload the files
    function uploadFiles(event)
    {
        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        // START A LOADING SPINNER HERE

        // Create a formdata object and add the files
        var data = new FormData();
        $.each(files, function(key, value)
        {
            data.append('file', value);
        });

        $.ajax({
            url: '/api/validator',
            type: 'POST',
            data: data,
            cache: false,
            dataType: 'json',
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    fileUploaded(event, data);
                }
                else
                {
                    // Handle errors here
                    console.log('ERRORS: ' + data.error);
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                // Handle errors here
                console.log('ERRORS: ' + textStatus);
                // STOP LOADING SPINNER
            }
        });
    }

    function fileUploaded(event, data) {
        var form = $('#headersForm');
        var keys = Object.keys(headings);

        $.each(data.mappings, function(index, value) {
            form.append(value['columnNumber'] + ' - ' + value['columnLabel']);
            var list = $('<select>').attr('name', value['columnNumber']);

            $.each(keys, function(ind, val) {
                var option = $('<option>').val(val).text(headings[val]);
                if (value['field'] === val) {
                    option.attr('selected', 'selected');
                }
                list.append(option);
            });
            form.append(list);
            form.append($('<br />'));
        });
        form.attr('job', data.jobName);
        form.append($('<input>').attr('type', 'submit'));
    }

    function pushToQueue(event) {
        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening
        
        var map = {
            
        };

        $.ajax({
            url: '/api/validator/' + $('#headersForm').attr('job') + '/process',
            type: 'POST',
            data: {mappings: JSON.stringify($('#headersForm').serializeObject())},
            cache: false,
            success: function(data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    fileUploaded(event, data);
                }
                else
                {
                    // Handle errors here
                    console.log('ERRORS: ' + data.error);
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                // Handle errors here
                console.log('ERRORS: ' + textStatus);
                // STOP LOADING SPINNER
            }
        });
    }

    $.fn.serializeObject = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    
    $(document).ready(function() {
        $('input[type=file]').on('change', prepareUpload);
        $('#fileUpload').on('submit', uploadFiles);
        $('#headersForm').on('submit', pushToQueue);
    });
})(jQuery);