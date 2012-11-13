<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Habitat Metadata</title>
        <link rel="stylesheet" type="text/css" href="/importer/importer.css" />
    </head>
    <body>
        <form method="post" enctype="multipart/form-data" action="habitatProcess.html">
            <fieldset>
                <legend>Habitat Layer Info</legend>
                    <p>
                        <span class="formlabel"><label for="category" path="metadata">Category</label></span>
                        <span class="formfield">
                            <select path="metadata" name="metadata.category" id="category">
                                <#list habitatForm.categories as cat>
                                <option value="${cat.id}" <#if habitatForm.metadata.category==cat.id>selected="selected"</#if>>${cat.name}</option>
                                </#list>
                            </select>
                        </span>
                    </p>
                <p>
                    <input type="submit" name="submit" value="Submit" />
                </p>
            </fieldset>
            
        </form>
    </body>
</html>