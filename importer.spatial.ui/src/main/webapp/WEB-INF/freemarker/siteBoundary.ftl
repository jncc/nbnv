<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Add an Organisation</title>
        <link rel="stylesheet" type="text/css" href="/importer/importer.css" />
    </head>
    <body>
        <form method="post" enctype="multipart/form-data" action="organisationProcess.html">
            <fieldset>
                <legend>Site Boundary Info</legend>
                    <p>
                        <span class="formlabel"><label for="category" path="metadata">Category</label></span>
                        <span class="formfield">
                            <select path="metadata" name="category" id="category">
                                <#list siteBoundaryForm.categories as cat>
                                <option value="${cat.id}" <#if siteBoundaryForm.metadata.category==cat.id>selected="selected"</#if>>${cat.name}</option>
                                </#list>
                            </select>
                        </span>
                    </p>
                    <p>
                        <span class="formlabel"><label for="type" path="metadata">Type</label></span>
                        <span class="formfield">
                            <select path="metadata" name="type" id="type">
                                <#list siteBoundaryForm.types as t>
                                <option value="${t.id}" <#if siteBoundaryForm.metadata.type==t.id>selected="selected"</#if>>${t.siteTypeName}</option>
                                </#list>
                            </select>
                        </span>
                    </p>
                <p>
                    <span class="formlabel"><label for="name" path="metadata">Name Field</label></span>
                    <span class="formfield"><@spring.formInput "siteBoundaryForm.metadata.name" "class='wide' length='200'"/></span>
                    <@spring.showErrors "" "error" />
                </p>    
                <p>
                    <input type="submit" name="submit" value="Submit" />
                </p>
            </fieldset>
            
        </form>
    </body>
</html>