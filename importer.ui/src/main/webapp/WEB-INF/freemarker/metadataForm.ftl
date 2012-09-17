<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Metadata Entry</title>
        <link rel="stylesheet" type="text/css" href="importer.css" />
    </head>
    <body>
        <#if !model.processed>
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
            </form>
        </#if>
        <form method="post" enctype="multipart/form-data" action="metadataProcess.html">
            <fieldset>
                <legend>Dataset Metadata</legend>
                <p>
                    <span class="formlabel"><label for="title" path="metadata">Title</label></span>
                    <@spring.formInput "model.metadata.title" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="organisationID" path="metadata">Organisation</label></span>
                    <span class="formfield">
                        <select path="metadata" name="organisationID" id="organisationID">
                            <#list model.organisationList as org>
                                <option value="${org.organisationID}" <#if model.metadata.organisationID==org.organisationID>selected="selected"</#if>>${org.organisationName}</option>
                            </#list>
                        </select>
                    </span>
                    <input type="submit" name="addOrganisation" value="Add Organisation" />
                </p>
                <p>
                    <span class="formlabel"><label for="description" path="metadata">Description</label></span>
                    <@spring.formTextarea "model.metadata.description" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="methods" path="metadata">Methods of Data Capture</label></span>
                    <@spring.formTextarea "model.metadata.methods" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="purpose" path="metadata">Purpose of Data Capture</label></span>
                    <@spring.formTextarea "model.metadata.purpose" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="geographic" path="metadata">Geographical Coverage</label></span>
                    <@spring.formTextarea "model.metadata.geographic" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="temporal" path="metadata">Temporal Coverage</label></span>
                    <@spring.formTextarea "model.metadata.temporal" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="quality" path="metadata">Data Quality</label></span>
                    <@spring.formTextarea "model.metadata.quality" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="info" path="metadata">Additional Information</label></span>
                    <@spring.formTextarea "model.metadata.info" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="use" path="metadata">Use Constraints</label></span>
                    <@spring.formTextarea "model.metadata.use" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="access" path="metadata">Access Constraints</label></span>
                    <@spring.formTextarea "model.metadata.access" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>

                <p>
                    <input type="submit" name="submit" value="submit" />
                </p>
            </fieldset>
        </form>
    </body>
</html>