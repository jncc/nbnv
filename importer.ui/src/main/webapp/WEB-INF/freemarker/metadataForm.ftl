<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Metadata Entry</title>
        <link rel="stylesheet" type="text/css" href="/importer/importer.css" />
        <script type="text/javascript" src="/importer/jquery.js"></script>
        <script type="text/javascript" src="/importer/metadata.js"></script>
    </head>
    <body>
        <div id="optionHead">
            <div id="uploadMetadata">
                <form method="post" enctype="multipart/form-data" action="metadata.html">
                    <fieldset class="optionHead">
                        <legend>Upload Metadata Form</legend>
                        <p>
                            <span class="formlabel"><label for="fileData" path="fileData">Form</label></span>
                            <span class="formfield"><input path="fileData" type="file" id="fileData" name="fileData"/></span>
                        </p>
                        <p>
                            <input type="submit" id="uploadMetadataButton" name="uploadMetadata" value="Upload Metadata" />
                            <img id="uploadSpinner" src="/importer/images/1-1.gif" />
                        </p>
                    </fieldset>
                </form>
            </div>
            <div id="updateDataset">
                <form method="post" enctype="multipart/form-data" action="metadata.html">
                    <fieldset class="optionHead">
                        <legend>Update Existing Dataset</legend>
                        <p>
                            <span class="formlabel"><label for="datasetID" path="metadata">Dataset</label></span>
                            <span class="formfield">
                                <@spring.formSingleSelect "metadataForm.metadata.datasetID", metadataForm.datasets, " " />
                            </span>
                            <#if metadataForm.datasetError>
                                <span class="error">Please select a valid dataset ID</span>
                            </#if>
                        </p>
                        <p>
                            <input type="submit" name="updateDataset" value="Get Dataset Metadata" />
                        </p>
                    </fieldset>            
                </form>
            </div>
        </div>
        <div id="metadata">
            <#if metadataForm.errors?has_content>
                <fieldset>
                    <legend>Errors</legend>
                    <div class="errors">
                        <#list metadataForm.errors as error>
                            <p>${error}</p>
                        </#list>
                    </div>
                </fieldset>
            </#if>

            <form method="post" enctype="multipart/form-data" action="metadataProcess.html">
                <fieldset>
                    <legend>Dataset Metadata</legend>
                    <p>
                        <span class="formlabel"><label for="title" path="metadata">Title</label></span>
                        <@spring.formInput "metadataForm.metadata.title" "class='wide' length='200'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="organisationID" path="metadata">Organisation</label></span>
                        <span class="formfield">
                            <select path="metadata" name="organisationID" id="organisationID">
                                <option value="-404" <#if metadataForm.metadata.organisationID==-1>selected="selected"</#if>></option>
                                <#list metadataForm.organisationList as org>
                                <option value="${org.id}" <#if metadataForm.metadata.organisationID==org.id>selected="selected"</#if>>${org.name}</option>
                                </#list>
                            </select>
                        </span>
                        <#if metadataForm.orgError == true>
                            <span class="error">Please select an organisation</span>
                        </#if>

                        <#if metadataForm.storedOrg == true>
                            <input type="submit" name="importOrganisation" value="Import Organisation" />
                        <#else>
                            <input type="submit" name="addOrganisation" value="Add Organisation" />
                        </#if>
                        <p id="selectedOrgID">No Organisation Selected</p>
                    </p>
                    <p>
                        <span class="formlabel"><label for="description" path="metadata">Description</label></span>
                        <@spring.formTextarea "metadataForm.metadata.description" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="methods" path="metadata">Methods of Data Capture</label></span>
                        <@spring.formTextarea "metadataForm.metadata.methods" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="purpose" path="metadata">Purpose of Data Capture</label></span>
                        <@spring.formTextarea "metadataForm.metadata.purpose" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="geographic" path="metadata">Geographical Coverage</label></span>
                        <@spring.formTextarea "metadataForm.metadata.geographic" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="temporal" path="metadata">Temporal Coverage</label></span>
                        <@spring.formTextarea "metadataForm.metadata.temporal" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="quality" path="metadata">Data Quality</label></span>
                        <@spring.formTextarea "metadataForm.metadata.quality" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="info" path="metadata">Additional Information</label></span>
                        <@spring.formTextarea "metadataForm.metadata.info" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="use" path="metadata">Use Constraints</label></span>
                        <@spring.formTextarea "metadataForm.metadata.use" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>
                    <p>
                        <span class="formlabel"><label for="access" path="metadata">Access Constraints</label></span>
                        <@spring.formTextarea "metadataForm.metadata.access" "class='wide' rows='6' cols='60'"/>
                        <@spring.showErrors "" "error" />
                    </p>

                    <fieldset>
                        <legend>Dataset Administrator Details</legend>
                        <p>
                            <span class="formlabel"><label for="datasetAdminName" path="metadata">Name</label></span>
                            <@spring.formInput "metadataForm.metadata.datasetAdminName" "class='wide' length='200'" />
                            <@spring.showErrors "" "error" />
                        </p>

                        <p>
                            <span class="formlabel"><label for="datasetAdminPhone" path="metadata">Phone Number</label></span>
                            <@spring.formInput "metadataForm.metadata.datasetAdminPhone" "class='wide' length='200'" />
                            <@spring.showErrors "" "error" />
                        </p>

                        <p>
                            <span class="formlabel"><label for="datasetAdminEmail" path="metadata">E-mail Address</label></span>
                            <@spring.formInput "metadataForm.metadata.datasetAdminEmail" "class='wide' length='200'" />
                            <@spring.showErrors "" "error" />
                        </p>

                        <@spring.formHiddenInput "metadataForm.metadata.datasetAdminID" "length='100'"/>
                    </fieldset>

                    <fieldset>
                        <legend>Level of Public Access</legend>
                        <br />
                        <span class="formlabel">Geographic Resolution</span>
                        <span class="formfield">
                            <@spring.formRadioButtons 'metadataForm.metadata.geographicalRes', referenceData.geoMap, ' ' />
                            <@spring.showErrors "" "error" />
                        </span> <br /> <br />
                        <span class="formlabel">Record Attributes</span>
                        <span class="formfield">
                            <@spring.formRadioButtons 'metadataForm.metadata.recordAtts', referenceData.recAtts, ' ' />
                            <@spring.showErrors "" "error" />
                        </span> <br /> <br />
                        <span class="formlabel">Recorder Names</span>
                        <span class="formfield">
                            <@spring.formRadioButtons 'metadataForm.metadata.recorderNames', referenceData.recNames, ' ' />
                            <@spring.showErrors "" "error" />
                        </span> <br /> <br />
                    </fieldset>                

                    <fieldset>
                        <legend>Insert Type</legend>
                        <span class="formfield">
                            <@spring.formRadioButtons 'metadataForm.insertType', referenceData.insertType, ' ' />
                            <@spring.showErrors "" "error" />
                        </span>
                    </fieldset>

                    <p>
                        <input type="submit" name="submit" value="Submit Metadata" />
                        <input type="submit" name="clearForm" value="Clear Form" />

                        <#if metadataForm.datasetUpdate>
                            <span class="info">Updating Dataset ${metadataForm.metadata.datasetID}</span>
                        </#if>
                    </p>
                </fieldset>
            </form>
        </div>

        <script>
            $(document).ready(function() {
                $("#uploadSpinner").hide();
                
                if ($("#metadata\\.geographicalRes1").prop('checked') ||
                    $("#metadata\\.geographicalRes2").prop('checked') ||
                    $("#metadata\\.geographicalRes3").prop('checked') ||
                    $("#metadata\\.geographicalRes4").prop('checked')) {
                    disableOptions();
                }
            });

            $("#uploadMetadataButton").click(function() {
                $("uploadSpinner").show();
            });

            function disableOptions() {
                $("#metadata\\.recordAtts0").attr('disabled', 'disabled');
                $("#metadata\\.recorderNames0").attr('disabled', 'disabled');
                $("#metadata\\.recordAtts2").attr('disabled', 'disabled');
                $("#metadata\\.recorderNames2").attr('disabled', 'disabled');
                $("#metadata\\.recordAtts0").prop('checked', false);
                $("#metadata\\.recorderNames0").prop('checked', false);
                $("#metadata\\.recordAtts1").prop('checked', false);
                $("#metadata\\.recorderNames1").prop('checked', false);
                $("#metadata\\.recordAtts2").prop('checked', false);
                $("#metadata\\.recorderNames2").prop('checked', false);
                
                // Pre-Select No
                $("#metadata\\.recordAtts1").prop('checked', true);
                $("#metadata\\.recorderNames1").prop('checked', true);
            }

            function enableOptions() {
                $("#metadata\\.recorderNames0").removeAttr('disabled');
                $("#metadata\\.recordAtts0").removeAttr('disabled');
                $("#metadata\\.recorderNames2").removeAttr('disabled');
                $("#metadata\\.recordAtts2").removeAttr('disabled');
            }

            $("#metadata\\.geographicalRes0").click(function() {
                enableOptions();
            });
            $("#metadata\\.geographicalRes1").click(function() {
                disableOptions();
            });
            $("#metadata\\.geographicalRes2").click(function() {
                disableOptions();
            });
            $("#metadata\\.geographicalRes3").click(function() {
                disableOptions();
            });
            $("#metadata\\.geographicalRes4").click(function() {
                disableOptions();
            });

        </script>
    </body>
</html>