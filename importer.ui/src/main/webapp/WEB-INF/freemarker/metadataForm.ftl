<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Metadata Entry</title>
        <link rel="stylesheet" type="text/css" href="/importer/importer.css" />
    </head>
    <body>
        <form method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>Upload Metadata Form</legend>
                <p>
                    <span class="formlabel"><label for="fileData" path="fileData">Form</label></span>
                    <span class="formfield"><input path="fileData" type="file" id="fileData" name="fileData"/></span>
                </p>
                <p>
                    <input type="submit" />
                </p>
            </fieldset>
            <#if metadataForm.errors?has_content>
                <div class="errors">
                    <ul>
                        <#list metadataForm.errors as error>
                            <li>${error}</li>
                        </#list>
                    </ul>
                </div>
            </#if>
        </form>
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
                            <#list metadataForm.organisationList as org>
                                <option value="${org.id}" <#if metadataForm.metadata.organisationID==org.id>selected="selected"</#if>>${org.name}</option>
                            </#list>
                        </select>
                    </span>
                    <input type="submit" name="addOrganisation" value="Add Organisation" />
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
                        <#if metadataForm.defaultName?has_content><span class="error">${metadataForm.defaultName}</span></#if>
                    </p>

                    <p>
                        <span class="formlabel"><label for="datasetAdminPhone" path="metadata">Phone Number</label></span>
                        <@spring.formInput "metadataForm.metadata.datasetAdminPhone" "class='wide' length='200'" />
                        <@spring.showErrors "" "error" />
                        <#if metadataForm.defaultPhone?has_content><span class="error">${metadataForm.defaultPhone}</span></#if>
                    </p>

                    <p>
                        <span class="formlabel"><label for="datasetAdminEmail" path="metadata">E-mail Address</label></span>
                        <@spring.formInput "metadataForm.metadata.datasetAdminEmail" "class='wide' length='200'" />
                        <@spring.showErrors "" "error" />
                        <#if metadataForm.defaultEmail?has_content><span class="error">${metadataForm.defaultEmail}</span></#if>
                    </p>

                    <@spring.formHiddenInput "metadataForm.metadata.datasetAdminID" "length='100'"/>
                </fieldset>

                <fieldset>
                    <legend>Level of Public Access</legend>
                    <br />
                    <span class="formlabel">Maximum Public Geographic Resolution</span>
                    <span class="formfield">
                        <@spring.formRadioButtons 'metadataForm.metadata.geographicalRes', referenceData.geoMap, ' ' />
                        <@spring.showErrors "" "error" />
                    </span> <br /> <br />
                    <span class="formlabel">Record Attributes</span>
                    <span class="formfield">
                        <@spring.formRadioButtons 'metadataForm.metadata.recordAtts', referenceData.recAtts, ' ' />
                        <@spring.showErrors "" "error" />
                    </span> <br /> <br />
                    <span class="formlabel">Can user see Recorder Names?</span>
                    <span class="formfield">
                        <@spring.formRadioButtons 'metadataForm.metadata.recorderNames', referenceData.recNames, ' ' />
                        <@spring.showErrors "" "error" />
                    </span> <br /> <br />

                </fieldset>                

                <p>
                    <input type="submit" name="submit" value="submit" />
                </p>
            </fieldset>
        </form>
    </body>
</html>