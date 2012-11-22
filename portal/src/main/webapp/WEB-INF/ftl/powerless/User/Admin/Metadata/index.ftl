<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="National Biodiversity Network Gateway">
    <h1>Select Dataset</h1>
    <div>
        <@adminPageDatasets datasets=datasets />
    </div>
</@template.master>

<#macro adminPageDatasets datasets>
    <div class="tabbed">
        <h3>Datasets</h3>
        <#if datasets?has_content>
            <table>
                <#list datasets as d>
                    <tr>
                        <td><a href="${d.key}">${d.title}</a></td>
                        <td>${d.organisationName}</td>
                        <td>${d.typeName}</td>
                        <td>${d.formattedDateUploaded}</td>
                    </tr>
                </#list>
            </table>
        <#else>
            None
        </#if>
    </div>
</#macro>