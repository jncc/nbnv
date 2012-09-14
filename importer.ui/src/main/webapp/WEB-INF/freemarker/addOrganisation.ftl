<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Add an Organisation</title>
        <link rel="stylesheet" type="text/css" href="importer.css" />
    </head>
    <body>
        <form method="post" enctype="multipart/form-data" action="addOrganisationProcess.html">
            <fieldset>
                <legend>Add A New Organisation</legend>
                <p>
                    <span class="formlabel"><label for="organisationName" path="organisation">Organisation Name</label></span>
                    <@spring.formInput "model.organisation.organisationName" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>    
                <p>
                    <span class="formlabel"><label for="abbreviation" path="organisation">Organisation Abbreviation</label></span>
                    <@spring.formInput "model.organisation.abbreviation" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="website" path="organisation">Website</label></span>
                    <@spring.formInput "model.organisation.website" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>

                <p>
                    <span class="formlabel"><label for="contactName" path="organisation">Contact Name</label></span>
                    <@spring.formInput "model.organisation.contactName" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p> 
                    <span class="formlabel"><label for="contactEmail" path="organisation">Contact Email</label></span>
                    <@spring.formInput "model.organisation.contactEmail" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="phone" path="organisation">Phone</label></span>
                    <@spring.formInput "model.organisation.phone" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="address" path="organisation">Address</label></span>
                    <@spring.formTextarea "model.organisation.address" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="postcode" path="organisation">Postcode</label></span>
                    <@spring.formInput "model.organisation.postcode" "class='wide' length='200'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="logo" path="organisation">Logo</label></span>

                    <@spring.bind "model.organisation.logo" />
                    <img alt="" src="${spring.status.value?default("")}"/>

                    <@spring.formHiddenInput "model.organisation.logo" "class='wide' length='1000'"/>
                    <@spring.showErrors "" "error" />

                    <@spring.bind "model.organisation.logoSmall" />
                    <img alt="" src="${spring.status.value?default("")}"  />

                    <@spring.formHiddenInput "model.organisation.logoSmall" "class='wide' length='1000'"/>
                    <@spring.showErrors "" "error" />
                        
                    <input path="imageData" type="file" name="imageData" value="imageData" />                        
                    <input type="submit" name="addImage" value="Upload Image" />
                </p>
                <p>
                    <span class="formlabel"><label for="summary" path="organisation">Summary</label></span>
                    <@spring.formTextarea "model.organisation.summary" "class='wide' rows='6' cols='60'"/>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="allowPublicRegistration" path="organisation">Allow Public Registration</label></span>
                    <@spring.formCheckbox "model.organisation.allowPublicRegistration", "" />
                </p>

                <p>
                    <input type="submit" name="submit" value="Submit" />
                </p>
            </fieldset>
            
        </form>
    </body>
</html>