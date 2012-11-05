<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Spatial Metadata Entry</title>
        <link rel="stylesheet" type="text/css" href="/importer/importer.css" />
        <script type="text/javascript" src="/importer/jquery.js"></script>
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
                            <input type="submit" name="uploadMetadata" value="Upload Metadata" />
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
                        <p>There were errors with the input form, please correct them</p>
                        <#list metadataForm.errors as error>
                            <p>${error}</p>
                        </#list>
                    </div>
                </fieldset>
            </#if>

            <form method="post" enctype="multipart/form-data" action="metadataProcess.html">
                <fieldset>
                    <legend>Spatial Dataset Type</legend>
                    <p>
                        <span class="formlabel"><label for="datasetTypeKey" path="metadata">Dataset Type</label></span>
                        <span class="formfield">
                            <select path="metadata" name="datasetTypeKey" id="datasetTypeKey">
                                <option value=" " <#if metadataForm.metadata.datasetTypeKey==''>selected="selected"</#if>></option>
                                <option value="S" <#if metadataForm.metadata.datasetTypeKey=='A'>selected="selected"</#if>>Site Boundary</option>
                                <option value="H" <#if metadataForm.metadata.datasetTypeKey=='H'>selected="selected"</#if>>Habitat</option>
                            </select>
                        </span>
                        <#if metadataForm.spatialError == true>
                            <span class="error">Please select a Spatial Type</span>
                        </#if>
                    </p>
                </fieldset>
                <br />
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
                        <input type="submit" name="submit" value="Submit Metadata" />
                        <input type="submit" name="clearForm" value="Clear Form" />

                        <#if metadataForm.datasetUpdate>
                            <span class="info">Updating Dataset ${metadataForm.metadata.datasetID}</span>
                        </#if>
                    </p>
                </fieldset>
            </form>
        </div>
    </body>
</html>