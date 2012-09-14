<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Add an Organisation</title>
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
        <form method="post" enctype="multipart/form-data" action="addOrganisationProcess.html">
            <fieldset>
                <legend>Add A New Organisation</legend>
                <p>
                    <span class="formlabel"><label for="organisationName" path="organisation">Organisation Name</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.organisationName" />
                        <input class="wide" path="organisation" type="text" id="organisationName" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        
                    </span>
                </p>    
                <p>
                    <span class="formlabel"><label for="abbreviation" path="organisation">Organisation Abbreviation</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.abbreviation" />
                        <input class="wide" path="organisation" type="text" id="abbreviation" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="website" path="organisation">Website</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.website" />
                        <input class="wide" path="organisation" type="text" id="website" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                    </span>
                </p>

                <p>
                    <span class="formlabel"><label for="contactName" path="organisation">Contact Name</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.contactName" />
                        <input class="wide" path="organisation" type="text" id="contactName" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                    </span>
                </p>
                <p> 
                    <span class="formlabel"><label for="contactEmail" path="organisation">Contact Email</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.contactEmail" />
                        <input class="wide" path="organisation" type="text" id="contactEmail" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="phone" path="organisation">Phone</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.phone" />
                        <input class="wide" path="organisation" type="text" id="phone" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="address" path="organisation">Address</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.address" />
                        <textarea class="wide" path="organisation" type="text" id="address" name="${spring.status.expression}" rows="6" cols="60">${spring.status.value?default("")}</textarea>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="postcode" path="organisation">Postcode</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.postcode" />
                        <input class="wide" path="organisation" type="text" id="postcode" name="${spring.status.expression}" length="10" value="${spring.status.value?default("")}"/>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="logo" path="organisation">Logo</label></span>
                    <span class="formfield">

                        <@spring.bind "model.organisation.logo" />
                        <img alt="" src="${spring.status.value?default("")}"/>

                        <@spring.bind "model.organisation.logo" />
                        <input class="wide" path="organisation" type="hidden" id="logo" name=${spring.status.expression} value="${spring.status.value?default("")}" />

                    </span>
                    <span class="formfield">

                        <@spring.bind "model.organisation.logoSmall" />
                        <img alt="" src="${spring.status.value?default("")}"  />

                        <@spring.bind "model.organisation.logoSmall" />
                        <input class="wide" path="organisation" type="hidden" id="logoSmall" name=${spring.status.expression} value="${spring.status.value?default("")}" />

                    </span>
                    <span class="formfield">
                        
                        <input path="imageData" type="file" name="imageData" value="imageData" />
                        
                    </span>
                        <input type="submit" name="addImage" value="Upload Image" />
                </p>
                <p>
                    <span class="formlabel"><label for="summary" path="organisation">Summary</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.summary" />
                        <textarea class="wide" path="organisation" type="text" id="summary" name="${spring.status.expression}" rows="6" cols="60">${spring.status.value?default("")}</textarea>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="allowPublicRegistration" path="organisation">Allow Public Registration</label></span>
                    <span class="formfield">
                        <@spring.formCheckbox "model.organisation.allowPublicRegistration", "" />
                    </span>
                </p>

                <p>
                    <input type="submit" name="submit" value="Submit" />
                </p>
            </fieldset>
        </form>
    </body>
</html>