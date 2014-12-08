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

        var table = $('<table>').append(
                $('<tr>').append($('<th>').text('Column'))
                    .append($('<th>').text('Column Header'))
                    .append($('<th>').text('Darwin Core Value')));

        $.each(data.mappings, function(index, value) {
            var row = $('<tr>');
            row.append($('<td>').append(value['columnNumber']));
            row.append($('<td>').append(value['columnLabel']));
            var list = $('<select>').attr('name', value['columnNumber']);

            $.each(keys, function(ind, val) {
                var option = $('<option>').val(val).text(headings[val]);
                if (value['field'] === val) {
                    option.attr('selected', 'selected');
                }
                list.append(option);
            });
            row.append($("<td>").append(list));
            table.append(row);
        });
        form.append(table);
        form.attr('job', data.jobName);
        
        var skip = $('<input>').attr('type', 'submit').attr('value', 'Process without metadata');
        var add = $('<input>').attr('type', 'submit').attr('value', 'Add metadata');
        
        skip.on('click', function(event) {
            event.stopPropagation();
            event.preventDefault();             
            
            $('#stage3-friendlyName').on('submit', pushToQueue);
            
            $('#stage2').hide();
            $('#stage3-skip').show();
            moveToStage(3);
        });
        add.on('click', function(event) {
            event.stopPropagation();
            event.preventDefault();             
            
            $('#metadata').on('submit', pushToQueueMetadata);
            
            $('#stage2').hide();
            $('#stage3-metadata').show();
            moveToStage(3);
        });
        
        form.append(skip);
        form.append(add);
        
        $('#stage1').hide();
        $('#stage2').show();
        moveToStage(2);
    }
    
    function uploadMetadataFile(event) {
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
            url: '/api/validator/' + $('#headersForm').attr('job') + '/processMetadata',
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
                    $('#title').val(data['title']);
                    $('#organisationID').val(data['organisationID']);
                    $('#description').val(data['description']);
                    $('#methods').val(data['methods']);
                    $('#purpose').val(data['purpose']);
                    $('#geographic').val(data['geographic']);
                    $('#temporal').val(data['temporal']);
                    $('#quality').val(data['quality']);
                    $('#info').val(data['info']);
                    $('#use').val(data['use']);
                    $('#access').val(data['access']);
                    $('#datasetAdminName').val(data['datasetAdminName']);
                    $('#datasetAdminPhone').val(data['datasetAdminPhone']);
                    $('#datasetAdminEmail').val(data['datasetAdminEmail']);
                    $('#geographicalRes').val(data['geographicalRes']);
                    $('#recordAtts').val(data['recordAtts']);
                    $('#recorderNames').val(data['recorderNames']);
                    
                    //datasetID
                    //datasetAdminID
                    //messages
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
    
    function pushToQueueMetadata(event) {
        event.stopPropagation();
        event.preventDefault();         
        
        var x = JSON.stringify($('#metadata').serializeObject());
        
        $.ajax({
            url: '/api/validator/' + $('#headersForm').attr('job') + '/process',
            type: 'POST',
            data: {mappings: JSON.stringify($('#headersForm').serializeObject()), metadata: JSON.stringify($('#metadata').serializeObject())},
            cache: false,
            success: function(data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    $('#stage3-metadata').hide();
                    $('#stage4').show();
                    moveToStage(4);
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

    function pushToQueue(event) {
        event.stopPropagation();
        event.preventDefault(); 

        var friendly = $('#friendlyName').val();

        $.ajax({
            url: '/api/validator/' + $('#headersForm').attr('job') + '/process',
            type: 'POST',
            data: {mappings: JSON.stringify($('#headersForm').serializeObject()), friendlyName: friendly},
            cache: false,
            success: function(data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    $('#stage3-skip').hide();
                    $('#stage4').show();
                    moveToStage(4);
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
    
    function moveToStage(stage) {
        $('.stageCounter').css('color','lightgrey');
        $('#stageCount' + stage).css('color', 'black');
    }
    
    $(document).ready(function() {
        $('input[type=file]').on('change', prepareUpload);
        $('#fileUpload').on('submit', uploadFiles);
        $('#metadataUpload').on('submit', uploadMetadataFile);
    });
})(jQuery);