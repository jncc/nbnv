<html>
    <head>
        <title>Metadata Entry</title>
        <link rel="stylesheet" type="text/css" href="importer.css" />
    </head>
    <body>
        <#if model.errors?has_content>
            <div class="errors">
                <ul>
                    <#list model.errors as error>
                        <li>${error}</li>
                    </#list>
                </ul>
            </div>
        </#if>
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
                    <span class="formfield"><input class="wide" path="metadata" type="text" id="title" name="title" length="200" value="${model.metadata.title}"/></span>
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
                </p>
                <p>
                    <span class="formlabel"><label for="description" path="metadata">Description</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="description" name="description" rows="6" cols="60">${model.metadata.description}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="methods" path="metadata">Methods of Data Capture</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="methods" name="methods" rows="6" cols="60">${model.metadata.methods}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="purpose" path="metadata">Purpose of Data Capture</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="purpose" name="purpose" rows="6" cols="60">${model.metadata.purpose}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="geographic" path="metadata">Geographical Coverage</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="geographic" name="geographic" rows="6" cols="60">${model.metadata.geographic}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="temporal" path="metadata">Temporal Coverage</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="temporal" name="temporal" rows="6" cols="60">${model.metadata.temporal}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="quality" path="metadata">Data Quality</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="quality" name="quality" rows="6" cols="60">${model.metadata.quality}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="info" path="metadata">Additional Information</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="info" name="info" rows="6" cols="60">${model.metadata.info}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="use" path="metadata">Use Constraints</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="use" name="use" rows="6" cols="60">${model.metadata.use}</textarea></span>
                </p>
                <p>
                    <span class="formlabel"><label for="access" path="metadata">Access Constraints</label></span>
                    <span class="formfield"><textarea class="wide" path="metadata" id="access" name="access" rows="6" cols="60">${model.metadata.access}</textarea></span>
                </p>

                <p>
                    <input type="submit" />
                </p>
            </fieldset>
        </form>
    </body>
</html>