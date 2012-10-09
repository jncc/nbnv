<#macro dataset_table providersWithQueryStats requestParameters>
    <table class="nbn-coloured-table">
        <tr><th class="nbn-th-left nbn-th-right" colspan="3">Data providers and their datasets that contribute to this page (number of records)<span id="nbn-select-datasets-text">Select or deselect all datasets: <input type="checkbox" name="nbn-select-datasets-auto" id="nbn-select-datasets-auto"/></span></th></tr>
        <#list providersWithQueryStats as providerWithQueryStats>
            <tr class="nbn-emphasise-row">
                <td class="nbn-td-left"><img src="${api}/organisations/${providerWithQueryStats.organisationID}/logo" class="nbn-provider-table-logo"></td>
                <td class="nbn-td-right" colspan="2"><a href="/Provider/${providerWithQueryStats.organisationID}">${providerWithQueryStats.organisation.name}</a> (${providerWithQueryStats.querySpecificObservationCount})</td>
            </tr>
            <#assign datasetsWithQueryStats=providerWithQueryStats.datasetsWithQueryStats>
            <#--All datasets will be checked by default unless dataset keys are found-->
            <#assign checked = "checked">
            <#list datasetsWithQueryStats as datasetWithQueryStats>
                <tr class="nbn-de-emphasise-row">
                    <#if requestParameters.datasetKey?has_content>
                        <#assign checked = requestParameters.datasetKey?seq_contains(datasetWithQueryStats.datasetKey)?string("checked","")>
                    </#if>
                    <td class="nbn-td-left" ><input type="checkbox" name="datasetKey" value="${datasetWithQueryStats.datasetKey}" ${checked}></td>
                    <td><a href="/Datasets/${datasetWithQueryStats.datasetKey}">${datasetWithQueryStats.dataset.name}</a> (${datasetWithQueryStats.querySpecificObservationCount})</td>
                    <td  class="nbn-td-right" >Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</td>
                </tr>
            </#list>
        </#list>
    </table>
</#macro>

<#macro site_report_filters location isSiteBoundaryReport requestParameters args={}>
    <#assign startYear=requestParameters.startYear?has_content?string(requestParameters.startYear[0]!"1600","1600")>
    <#assign endYear=requestParameters.endYear?has_content?string(requestParameters.endYear[0]!.now?string("yyyy"),.now?string("yyyy"))>
    <#assign spatialRelationship=requestParameters.spatialRelationship?has_content?string(requestParameters.spatialRelationship[0]!"overlap","overlap")>
    <#assign designations=json.readURL("${api}/designations")>
    <div class="nbn-report-data-container">

        <table class="nbn-coloured-table">
            <tr>
                <th class="nbn-th-left nbn-th-right" colspan="2">
                    Your current filters
                </th>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Location:</td>
                <td class="nbn-td-right">${location}</td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Year range:</td>
                <td class="nbn-td-right"><@yearRangeText requestParameters=requestParameters/></td>
            </tr>
            <#if isSiteBoundaryReport>
                <tr>
                    <td class="nbn-td-left nbn-filter-name">Spatial relationship:</td>
                    <td class="nbn-td-right"><@spatialRelationshipText requestParameters=requestParameters/></td>
                </tr>
            </#if>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Designation:</td>
                <td class="nbn-td-right"><@designationText requestParameters=requestParameters/></td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Datasets:</td>
                <td class="nbn-td-right"><@datasetText requestParameters=requestParameters/></td>
            </tr>
            <#if args.taxonOutputGroup??>
                <tr>
                    <td class="nbn-td-left nbn-filter-name">Species group:</td>
                    <td class="nbn-td-right">${args.taxonOutputGroup.taxonGroupName?cap_first}</td>
                </tr>
            </#if>
            <#if args.taxon??>
                <tr>
                    <td class="nbn-td-left nbn-filter-name">Species:</td>
                    <td class="nbn-td-right">${args.taxon.name}</td>
                </tr>
            </#if>
            <tr>
                <th class="nbn-td-left nbn-td-right" colspan="2">
                    Change your filters
                </th>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Year range:</td>
                <td class="nbn-td-right"><input name="startYear" id="startYear" type="textbox" value="${startYear}" class="nbn-year-input"/>
                                         to <input name="endYear" id="endYear" type="textbox" value="${endYear}" class="nbn-year-input"/></td>
            </tr>
            <#if isSiteBoundaryReport>
                <tr>
                    <td class="nbn-td-left nbn-filter-name">Spatial relationship: </td>
                    <td class="nbn-td-right">
                        <select name="spatialRelationship" id="spatialRelationship" class="nbn-report-filter-dropdown">
                            <#if requestParameters.spatialRelationship?has_content && requestParameters.spatialRelationship[0]=="within" >
                                <option value="within" selected="selected">Records within site</option>
                                <option value="overlap">Records within or overlapping site</option>
                            <#else>
                                <option value="within">Records completely within site</option>
                                <option value="overlap" selected="selected">Records within or overlapping site</option>
                            </#if>
                        </select><br/>
                    </td>
                </tr>
            </#if>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Designation: </td>
                <td class="nbn-td-right">
                    <select name="designation" id="designation" class="nbn-report-filter-dropdown">
                        <option selected="selected" value="">None selected</option>
                        <#list designations as designation>
                            <#assign selected = requestParameters.designation?seq_contains(designation.code)?string("selected","")>
                            <option value="${designation.code}" ${selected}>${designation.name}</option>
                        </#list>
                    </select><br/>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Datasets:</td>
                <td class="nbn-td-right">Select from table below</td>
            </tr>
            <tr>
                <td class="nbn-td-left"></td>
                <td class="nbn-td-right"><input type="submit"/></td>
            </tr>
        </table>
    </div>
</#macro>

<#macro designationText requestParameters>
    <#assign designationText="None selected">
    <#if requestParameters.designation?has_content && requestParameters.designation[0] != "">
        <#assign designation=json.readURL("${api}/designations/${requestParameters.designation[0]}")>
        <#assign designationText="<a href=\"/Designations/${designation.code}\">${designation.name}</a>">
    </#if>
    ${designationText}
</#macro>

<#macro datasetText requestParameters>
    <#assign datasetText="All available">
    <#if requestParameters.datasetKey?has_content>
        <#assign datasetText=requestParameters.datasetKey?size + " datasets selected">
    </#if>
    ${datasetText}
</#macro>

<#macro yearRangeText requestParameters>
    <#assign startYear=requestParameters.startYear?has_content?string(requestParameters.startYear[0]!"1600","1600")>
    <#assign endYear=requestParameters.endYear?has_content?string(requestParameters.endYear[0]!.now?string("yyyy"),.now?string("yyyy"))>
    ${startYear} to ${endYear}
</#macro>

<#macro spatialRelationshipText requestParameters>
    <#assign spatialRelationshipText="Records within or partially overlapping the site">
    <#if requestParameters.spatialRelationship?has_content && requestParameters.spatialRelationship[0]=="within" >
        <#assign spatialRelationshipText="Records completely within the site">
    </#if>
    ${spatialRelationshipText}
</#macro>

<#function getQueryParameterText RequestParameters>
    <#assign toReturn="">
    <#if RequestParameters?has_content>
        <#assign toReturn = core.queryString(RequestParameters)>
    </#if>
    <#return toReturn>
</#function>