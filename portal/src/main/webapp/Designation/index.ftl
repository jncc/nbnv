<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - designation">

    <#assign designation=json.readURL("${api}designations/${RequestParameters.desig}")>
    <#assign designationCategory=json.readURL("${api}designationCategories/${designation.designationCategoryID}")>

    This page provides information about the designation: ${designation.name}

    <div id="nbn-designation-content">
        <table>
            <tr>
                <th>Designation name:</th>
                <td>${designation.name}</td>
            </tr>
            <tr>
                <th>Abbreviation:</th>
                <td>${designation.label}</td>
            </tr>
            <tr>
                <th>Description:</th>
                <td>${designation.description}</td>
            </tr>
            <tr>
                <th>Parent category:</th>
                <td>${designationCategory.label}</td>
            </tr>
            <tr>
                <th>Map of species with this designation:</th>
                <td><a href="http://data.nbn.org.uk/imt/?mode=DESIGNATION&designation=${designation.label}" target="_blank">Interactive map of species density</a></td>
            </tr>
            <tr>
                <th>Groups that have species with this designation:</td>
                <td>[TODO] SERVICES NOT YET AVAILABLE TO SUPPORT THIS</td>
            </tr>
        </table>
    </div>

</@master>
