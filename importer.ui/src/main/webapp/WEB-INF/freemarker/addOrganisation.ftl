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
                    <span class="formfield"><@spring.formInput "orgForm.organisation.name" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>    
                <p>
                    <span class="formlabel"><label for="abbreviation" path="organisation">Organisation Abbreviation</label></span>
                    <span class="formfield"><@spring.formInput "orgForm.organisation.abbreviation" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="website" path="organisation">Website</label></span>
                    <span class="formfield"><@spring.formInput "orgForm.organisation.website" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>

                <p>
                    <span class="formlabel"><label for="contactName" path="organisation">Contact Name</label></span>
                    <span class="formfield"><@spring.formInput "orgForm.organisation.contactName" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p> 
                    <span class="formlabel"><label for="contactEmail" path="organisation">Contact Email</label></span>
                    <span class="formfield"><@spring.formInput "orgForm.organisation.contactEmail" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="phone" path="organisation">Phone</label></span>
                    <span class="formfield"><@spring.formInput "orgForm.organisation.phone" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="address" path="organisation">Address</label></span>
                    <span class="formfield"><@spring.formTextarea "orgForm.organisation.address" "class='wide' rows='6' cols='60'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="postcode" path="organisation">Postcode</label></span>
                    <span class="formfield"><@spring.formInput "orgForm.organisation.postcode" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="logo" path="organisation">Logo</label></span>

                    <span class="formfield">
                        <img alt="" src="imageBase/large.html"/>
                        <img alt="" src="imageBase/small.html"  />

                        <input path="imageData" type="file" name="imageData" value="imageData" />                        
                        <input type="submit" name="addImage" value="Upload Image" />

                        <#if orgForm.imageError != "">
                            <span class="error">
                                <b>${orgForm.imageError}</b>
                            </span>
                        </#if>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="summary" path="organisation">Summary</label></span>
                    <span class="formfield"><@spring.formTextarea "orgForm.organisation.summary" "class='wide' rows='6' cols='60'"/></span>
                    <@spring.showErrors "" "error" />
                </p>
                <p>
                    <span class="formlabel"><label for="allowPublicRegistration" path="organisation">Allow Public Registration</label></span>
                    <span class="formfield"><@spring.formCheckbox "orgForm.organisation.allowPublicRegistration", "" /></span>
                </p>

                <p>
                    <input type="submit" name="submit" value="Submit" />
                </p>
            </fieldset>
            
        </form>
    </body>
</html>