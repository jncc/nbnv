(function ($) {
    // Human readable headings for column data types
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
    // Descriptions for column data types
    var descriptions = {
        OCCURRENCEID: 'This should ideally be the primary key associated with the occurrence or biotope record in your database. If your records do not have a primary key an alternative is to use sequential numbers instead (i.e. 1,2,3,4 ….).',
        EVENTDATE: 'The date-time or interval during which an Event occurred. For occurrences, this is the date-time when the event was recorded. Not suitable for a time in a geological context. Recommended best practice is to use an encoding scheme, such as ISO 8601:2004(E).',
        TAXONID: 'A Taxon Version Key, this is compulsory for species records. In addition, you may optionally supply the taxon name, as an extra field. This will allow us to check that each taxon-versionkey is associated with their correct taxon.',
        LOCATIONID: 'A key to identify unique sites within the dataset.',
        LOCALITY: 'The specific description of the place. Less specific geographic information can be provided in other geographic terms (higherGeography, continent, country, stateProvince, county, municipality, waterBody, island, islandGroup). This term may contain information modified from the original to correct perceived errors or standardize the description.',
        VERBATIMLATITUDE: 'The verbatim original latitude of the Location. The coordinate ellipsoid, geodeticDatum, or full Spatial Reference System (SRS) for these coordinates should be stored in verbatimSRS and the coordinate system should be stored in verbatimCoordinateSystem.',
        VERBATIMLONGITUDE: 'The verbatim original longitude of the Location. The coordinate ellipsoid, geodeticDatum, or full Spatial Reference System (SRS) for these coordinates should be stored in verbatimSRS and the coordinate system should be stored in verbatimCoordinateSystem.',
        VERBATIMSRS: 'The ellipsoid, geodetic datum, or spatial reference system (SRS) upon which coordinates given in verbatimLatitude and verbatimLongitude, or verbatimCoordinates are based. Recommended best practice is use the EPSG code as a controlled vocabulary to provide an SRS, if known. Otherwise use a controlled vocabulary for the name or code of the geodetic datum, if known. Otherwise use a controlled vocabulary for the name or code of the ellipsoid, if known. If none of these is known, use the value "unknown".',
        RECORDEDBY: 'The name or list of names for one or more recorders for the species or habitat record.',
        IDENTIFIEDBY: 'The name or list of names for one or more determiners for the species or habitat record.',
        OCCURRENCESTATUS: 'This column indicates whether or not the record represents a confirmed absence (T | F).',
        COLLECTIONCODE: 'The name, acronym, coden, or initialism identifying the collection or data set from which the record was derived.',
        EVENTID: 'An identifier for the set of information associated with an Event (something that occurs at a place and time). May be a global unique identifier or an identifier specific to the data set.',
        EVENTDATETYPECODE: 'Event Date Type Code (D,DD,O,OO,Y,YY, -Y, ND or U)',
        EVENTDATESTART: 'The start date of a date range for this recording or survey (Date Formats allowed DD/MM/YYYY or YYYY-MM-DD)',
        EVENTDATEEND: 'The end date of a date range for this recording or survey (Date Formats allowed DD/MM/YYYY or YYYY-MM-DD)',
        GRIDREFERENCE: 'Grid references should be in the typical Ordnance Survey "Landranger" format. Grid references should not contain any spaces or "-" (10km, 2km, 1km or 100m)',
        GRIDREFERENCETYPE: 'Grid Reference Type (OSGB | OSI)',
        GRIDREFERENCEPRECISION: 'Grid Reference Precision (10km | 2km | 1km | 100m)',
        SITEFEATUREKEY: 'A key to identify unique sites within the dataset.',
        SENSITIVEOCCURRENCE: 'Whether the record is sensitive or not (default is F) (T | F).',
        DYNAMICPROPERTIES: 'Dynamic Properties',
        TAXONNAME: 'In addition, you may optionally supply the taxon name, as an extra field. This will allow us to check that each taxon-versionkey is associated with their correct taxon.',
        ATTRIBUTE: 'One or more attribute columns can be included in your dataset.'
    };    
    
    // Global to store uploaded
    var files;    

    // Grab the files and set them to our variable
    function prepareUpload(event) {
        files = event.target.files;
    }

    // Catch the form submit and upload the files
    function uploadFiles(event)
    {
        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        // Kick off waiting screen
        $('#stage1').hide();
        $('#waitingDiv').show();

        // Create a formdata object and add the files
        var data = new FormData();
        $.each(files, function (key, value)
        {
            data.append('file', value);
        });

        // POST file to API
        $.ajax({
            url: '/api/validator',
            type: 'POST',
            data: data,
            cache: false,
            dataType: 'json',
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function (data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    fileUploaded(event, data);
                }
                else
                {
                    $('#waitingDiv').hide();
                    $('#errorDiv').show();
                    $('#errorSpan').text('ERRORS: ' + data.error);
                }
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                $('#waitingDiv').hide();
                $('#errorDiv').show();
                $('#errorSpan').text('ERRORS: ' + textStatus);
            }
        });
    }

    function fileUploaded(event, data) {
        var form = $('#headersForm');
        var keys = Object.keys(headings);

        var table = $('<table>').append(
                $('<tr>').append($('<th>').text('Column'))
                .append($('<th>').text('Column Header'))
                .append($('<th>').text('Darwin Core Value'))
                .append($('<th>').text('Description')));

        $.each(data.mappings, function (index, value) {
            var row = $('<tr style="height:45px;">');
            row.append($('<td style="width: 5%">').append(value['columnNumber']));
            row.append($('<td style="width: 10%">').append(value['columnLabel']));
            var list = $('<select>').attr('name', value['columnNumber']).attr('class', 'list');

            $.each(keys, function (ind, val) {
                var option = $('<option>').val(val).text(headings[val]);
                if (value['field'] === val) {
                    option.attr('selected', 'selected');
                }
                list.append(option);
            });
            row.append($('<td style="width: 15%">').append(list));
            row.append($('<td style="width: 70%, text-overflow:hidden;">').attr('id', 'desc' + value['columnNumber']));
            table.append(row);
            list.change(function() {
               $('#desc' + $(this).attr('name')).text(descriptions[$(this).val()]);
            });
        });
        
        form.append(table);
        form.attr('job', data.jobName);
        
        $('.list').trigger('change');

        var skip = $('<input>').attr('type', 'submit').attr('value', 'Process without metadata');
        //var add = $('<input>').attr('type', 'submit').attr('value', 'Add metadata');

        skip.on('click', function (event) {
            event.stopPropagation();
            event.preventDefault();

            $('#stage3-friendlyName').on('submit', pushToQueue);

            $('#stage2').hide();
            $('#stage3-skip').show();
            moveToStage(3);
        });
//        add.on('click', function (event) {
//            event.stopPropagation();
//            event.preventDefault();
//
//            $('#metadata').on('submit', pushToQueueMetadata);
//
//            $('#stage2').hide();
//            $('#stage3-metadata').show();
//            moveToStage(3);
//        });

        form.append(skip);
        //form.append(add);

        $('#waitingDiv').hide();
        $('#stage2').show();
        moveToStage(2);
    }

    function uploadMetadataFile(event) {
        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        $('#stage3-metadata').hide();
        $('#waitingDiv').show();

        // Create a formdata object and add the files
        var data = new FormData();
        $.each(files, function (key, value)
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
            success: function (data, textStatus, jqXHR)
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
                }
                else
                {
                    $('#waitingDiv').hide();
                    $('#errorDiv').show();
                    $('#errorSpan').text('ERROR: ' + data.error);
                }
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                $('#waitingDiv').hide();
                $('#errorDiv').show();
                $('#errorSpan').text('ERROR: ' + textStatus);
            }
        });
    }

    function pushToQueueMetadata(event) {
        event.stopPropagation();
        event.preventDefault();

        var x = JSON.stringify($('#metadata').serializeObject());
        
        $('#stage3-metadata').hide();
        $('#waitingDiv').show();

        $.ajax({
            url: '/api/validator/' + $('#headersForm').attr('job') + '/process',
            type: 'POST',
            data: {mappings: JSON.stringify($('#headersForm').serializeObject()), metadata: JSON.stringify($('#metadata').serializeObject())},
            cache: false,
            success: function (data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    $('#waitingDiv').hide();
                    $('#stage4').show();
                    moveToStage(4);
                }
                else
                {
                    $('#waitingDiv').hide();
                    $('#errorDiv').show();
                    $('#errorSpan').text('ERROR: ' + data.error);
                }
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                $('#waitingDiv').hide();
                $('#errorDiv').show();
                $('#errorSpan').text('ERROR: ' + textStatus);
            }
        });
    }

    function pushToQueue(event) {
        event.stopPropagation();
        event.preventDefault();

        var friendly = $('#friendlyName').val();
        
        $('#stage3-skip').hide();
        $('#waitingDiv').show();

        $.ajax({
            url: '/api/validator/' + $('#headersForm').attr('job') + '/process',
            type: 'POST',
            data: {mappings: JSON.stringify($('#headersForm').serializeObject()), friendlyName: friendly},
            cache: false,
            success: function (data, textStatus, jqXHR)
            {
                if (typeof data.error === 'undefined')
                {
                    // Success so call function to process the form
                    $('#waitingDiv').hide();
                    $('#stage4').show();
                    moveToStage(4);
                }
                else
                {
                    $('#waitingDiv').hide();
                    $('#errorDiv').show();
                    $('#errorSpan').text('ERROR: ' + data.error);
                }
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                    $('#waitingDiv').hide();
                    $('#errorDiv').show();
                    $('#errorSpan').text('ERROR: ' + textStatus);
            }
        });
    }

    $.fn.serializeObject = function ()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
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
        $('.stageCounter').css('color', 'lightgrey');
        $('#stageCount' + stage).css('color', 'black');
    }

    function setupForm() {
        $('#datasetAdminEmail').autocomplete({
            source: '/api/user/search',
            minLength: 3,
            select: function(event, ui) {
                //event.preventDefault();
                $('datasetAdminName').val(ui.item.forename + ' ' + ui.item.surname);
                $('datasetAdminName').text(ui.item.forename + ' ' + ui.item.surname);
                $('datasetAdminEmail').val(ui.item.email);
                $('datasetAdminEmail').text(ui.item.email);
                $('datasetAdminID').val(ui.item.id);                
            }
        })
        .data('autocomplete')._renderItem = function(ul, item) {
            var re = new RegExp(this.term, 'i');
            return $('<li></li>')
                    .data('item.autocomplete', item)
                    .append('<a><strong style="font-size: small;">' + replaceTerm(item.forename + ' ' + item.surname, re) + '</strong><br><span style="font-size: smaller;">' + replaceTerm(item.email, re) + '</span></a>')
                    .appendTo(ul);
        };        
    }
    
    function replaceTerm(input, re) {
        return input.replace(re, '<span style="font-weight:bold;">' + '$&' + '</span>');
    }    

    $(document).ready(function () {
        $('input[type=file]').on('change', prepareUpload);
        $('#fileUpload').on('submit', uploadFiles);
        $('#metadataUpload').on('submit', uploadMetadataFile);
        setupForm();
    });
})(jQuery);