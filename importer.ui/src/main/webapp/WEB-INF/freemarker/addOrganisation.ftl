<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Add an Organisation</title>
        <link rel="stylesheet" type="text/css" href="/importer/importer.css" />
    </head>
    <body>
        <form method="post" enctype="multipart/form-data" action="organisationProcess.html">
            <fieldset>
                <legend>Add A New Organisation</legend>
                <p>
                    <span class="formlabel"><label for="organisationName" path="organisation">Organisation Name</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.organisationName" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>    
                <p>
                    <span class="formlabel"><label for="abbreviation" path="organisation">Organisation Abbreviation</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.abbreviation" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="website" path="organisation">Website</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.website" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>

                <p>
                    <span class="formlabel"><label for="contactName" path="organisation">Contact Name</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.contactName" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p> 
                    <span class="formlabel"><label for="contactEmail" path="organisation">Contact Email</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.contactEmail" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="phone" path="organisation">Phone</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.phone" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="address" path="organisation">Address</label></span>
                    <span class="formfield"><@spring.formTextarea "model.organisation.address" "class='wide' rows='6' cols='60'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="postcode" path="organisation">Postcode</label></span>
                    <span class="formfield"><@spring.formInput "model.organisation.postcode" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="logo" path="organisation">Logo</label></span>

                    <span class="formfield">
                        <@spring.bind "model.organisation.logo" />
                        <img alt="" src="${spring.status.value?default("")}"/>

                        <@spring.formHiddenInput "model.organisation.logo" "class='wide' length='1000'"/>

                        <@spring.bind "model.organisation.logoSmall" />
                        <img alt="" src="${spring.status.value?default("")}"  />

                        <@spring.formHiddenInput "model.organisation.logoSmall" "class='wide' length='1000'"/>

                        <input path="imageData" type="file" name="imageData" value="imageData" />                        
                        <input type="submit" name="addImage" value="Upload Image" />

                        <#if model.imageError != "">
                            <span class="error">
                                <b>${model.imageError}</b>
                            </span>
                        </#if>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="summary" path="organisation">Summary</label></span>
                    <span class="formfield"><@spring.formTextarea "model.organisation.summary" "class='wide' rows='6' cols='60'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="allowPublicRegistration" path="organisation">Allow Public Registration</label></span>
                    <span class="formfield"><@spring.formCheckbox "model.organisation.allowPublicRegistration", "" /></span>
                </p>

                <p>
                    <@spring.formHiddenInput "model.metadataForm.metadata.title" "length='200'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.description" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.methods" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.purpose" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.geographic" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.temporal" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.quality" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.info" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.access" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.use" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.datasetAdminName" "length='200'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.datasetAdminPhone" "length='200'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.datasetAdminEmail" "length='200'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.geographicalRes" "length='10'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.recordAtts" "length='10'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.recorderNames" "length='10'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.datasetAdminID" "length='65535'"/>
                    <@spring.formHiddenInput "model.metadataForm.metadata.organisationID" "length='65535'"/>
                </p>

                <p>
                    <input type="submit" name="submit" value="Submit" />
                </p>
            </fieldset>
            
        </form>
    </body>
</html>