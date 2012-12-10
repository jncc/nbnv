<#macro dataset_table providersWithQueryStats requestParameters>
    <#if providersWithQueryStats?has_content>
        <div class="tabbed tabbed-reports-dataset-table" id="nbn-dataset-selector-container">
            <h3>Data providers and their datasets that contribute to this page (number of records)<span id="nbn-select-datasets-text">Select or deselect all datasets: <input type="checkbox" name="nbn-select-datasets-auto" id="nbn-select-datasets-auto"/></span></h3>
            <table class="nbn-simple-table" id="nbn-report-dataset-table">
                <tr><td class="nbn-dataset-table-heading"></td><td class="nbn-dataset-table-heading">Dataset title</td><td class="nbn-dataset-table-heading">Dataset access</td></tr>
                <#list providersWithQueryStats as providerWithQueryStats>
                    <tr>
                        <th>
                            <#if providerWithQueryStats.organisation.hasLogo>
                                <img src="${api}/organisations/${providerWithQueryStats.organisationID}/logo" class="nbn-provider-table-logo">
                            </#if>
                        </th>
                        <th colspan="2"><a href="/Organisations/${providerWithQueryStats.organisationID}">${providerWithQueryStats.organisation.name}</a> (${providerWithQueryStats.querySpecificObservationCount})</th>
                    </tr>
                    <#assign datasetsWithQueryStats=providerWithQueryStats.datasetsWithQueryStats>
                    <#--All datasets will be checked by default unless dataset keys are found-->
                    <#assign checked = "checked">
                    <#list datasetsWithQueryStats as datasetWithQueryStats>
                        <#assign accessPositions=json.readURL("${api}/taxonDatasets/${datasetWithQueryStats.datasetKey}/accessPositions")>
                        <tr>
                            <#if requestParameters.datasetKey?has_content>
                                <#assign checked = requestParameters.datasetKey?seq_contains(datasetWithQueryStats.datasetKey)?string("checked","")>
                            </#if>
                            <td><input type="checkbox" name="datasetKey" value="${datasetWithQueryStats.datasetKey}" ${checked}></td>
                            <td><a href="/Datasets/${datasetWithQueryStats.datasetKey}">${datasetWithQueryStats.taxonDataset.title}</a> (${datasetWithQueryStats.querySpecificObservationCount})</td>
                            <td>
                                <ul>
                                    <li>Public access: records available at ${datasetWithQueryStats.taxonDataset.publicResolution}
                                    <#if datasetWithQueryStats.taxonDataset.publicAttribute>
                                        with record attributes
                                    </#if>
                                    <#if accessPositions?has_content>
                                        <#list accessPositions as accessPosition>
                                            <li>Your enhanced access: ${accessPosition}
                                        </#list>
                                    </#if>
                                </ul>
                            </td>
                        </tr>
                    </#list>
                </#list>
            </table>
        </div>
    </#if>
</#macro>

<#macro unavailable_datasets unavailableDatasets>
    <#if unavailableDatasets?has_content>
        <div class="tabbed">
            <h3>Datasets with relevant data that you do not have access to</h3> 
            <table>
                <tr>
                    <th>Dataset</th>
                    <th>Provider</th>
                </tr>
                <#list unavailableDatasets as unavailableDataset>
                    <tr>
                        <td><a href="/Datasets/${unavailableDataset.datasetKey}">${unavailableDataset.dataset.title}</a></td>
                        <td><a href="/Organisations/${unavailableDataset.dataset.organisationID}">${unavailableDataset.dataset.organisationName}</a></td>
                    </tr>
                </#list>
            </table>
        </div>
    </#if>
</#macro>

<#macro site_report_filters location requestParameters isSpatialRelationshipNeeded=true isDesignationNeeded=true isDatasetNeeded=true isDownloadButtonNeeded=false args={}>
    <#assign startYear=requestParameters.startYear?has_content?string(requestParameters.startYear[0]!"","")>
    <#assign endYear=requestParameters.endYear?has_content?string(requestParameters.endYear[0]!"","")>
    <#assign spatialRelationship=requestParameters.spatialRelationship?has_content?string(requestParameters.spatialRelationship[0]!"overlap","overlap")>
    <#assign designations=json.readURL("${api}/designations")>
    <div class="tabbed" id="nbn-site-report-filter-container">
        <h3>Info. and options</h3>
            Location: ${location}
            <fieldset>
                <legend>Options</legend>
                Year range: <input name="startYear" id="startYear" type="textbox" value="${startYear}" class="nbn-year-input"/>
                             to <input name="endYear" id="endYear" type="textbox" value="${endYear}" class="nbn-year-input"/><br/>
                <#if isSpatialRelationshipNeeded>
                    Spatial relationship: 
                        <select name="spatialRelationship" id="spatialRelationship" class="nbn-report-filter-dropdown">
                            <#if requestParameters.spatialRelationship?has_content && requestParameters.spatialRelationship[0]=="within" >
                                <option value="within" selected="selected">Records within site</option>
                                <option value="overlap">Records within or overlapping site</option>
                            <#else>
                                <option value="within">Records completely within site</option>
                                <option value="overlap" selected="selected">Records within or overlapping site</option>
                            </#if>
                        </select><br/>
                </#if>
                <#if isDesignationNeeded>
                        Designation: 
                            <select name="designation" id="designation" class="nbn-report-filter-dropdown">
                                <option selected="selected" value="">None selected</option>
                                <#list designations as designation>
                                    <#assign selected = requestParameters.designation?seq_contains(designation.code)?string("selected","")>
                                    <option value="${designation.code}" ${selected}>${designation.name}</option>
                                </#list>
                            </select><br/>
                </#if>
            </fieldset>
            <#if isDownloadButtonNeeded>
                <fieldset>
                    <legend>Download</legend>
                    <button id="nbn-site-report-download-button">Download</button> species list
                </fieldset>
            </#if>
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

<#macro spatialRelationshipText requestParameters>
    <#assign spatialRelationshipText="Records within or partially overlapping the site">
    <#if requestParameters.spatialRelationship?has_content && requestParameters.spatialRelationship[0]=="within" >
        <#assign spatialRelationshipText="Records completely within the site">
    </#if>
    ${spatialRelationshipText}
</#macro>

<#function getQueryString RequestParameters>
    <#assign toReturn="">
    <#if RequestParameters?has_content>
        <#assign toReturn = core.queryString(RequestParameters)>
    </#if>
    <#return toReturn>
</#function>

<#macro siteImage locationName locationID imageURL>
    <div class="tabbed" id="nbn-site-report-map">
        <h3>Map of ${locationName}</h3>
        <img src="${imageURL}" id="nbn-site-map-image">
        <@OSCopyright/>
    </div>
</#macro>

<#function getSiteBoundaryImageURL locationID showBoundary>
    <#assign toReturn="${gis}/SiteReport/${locationID}?mode=map&LAYER=OS-Scale-Dependent">
    <#if showBoundary>
        <#assign toReturn = toReturn + "&LAYER=Selected-Feature">
    </#if>
    <#return toReturn>
</#function>

<#function getSiteSpeciesImageURL locationID ptvk startYear endYear datasets spatialRelationship showBoundary>
    <#assign toReturn="${gis}/SiteReport/${locationID}/${ptvk}?mode=map&LAYER=OS-Scale-Dependent&LAYERS=Records&startyear=${startYear}&endyear=${endYear}&spatialRelationship=${spatialRelationship}">
    <#if datasets??>
        <#assign datasetKeys="">
        <#list datasets as dataset>
            <#assign datasetKeys = datasetKeys + dataset.key>
            <#if dataset_has_next>
                <#assign datasetKeys = datasetKeys + ",">
            </#if>
        </#list>
        <#assign toReturn = toReturn + "&" + datasetKeys>
    </#if>
    <#if showBoundary>
        <#assign toReturn = toReturn + "&LAYER=Selected-Feature">
    </#if>
    <#return toReturn>
</#function>

<#macro noRecordsInfoBox>
    <div class="nbn-information-panel">No records were found for your current options</div>
</#macro>

<#macro downloadTermsDialogue>
    <div id="nbn-download-terms" title="Data download terms and conditions">
        <@mdcontent.dataDownloadTerms/>
    </div>
</#macro>

<#macro OSCopyright>
        <span id="nbn-site-image-copyright">&copy; Crown copyright and database rights 2011 Ordnance Survey [100017955]</span>
</#macro>