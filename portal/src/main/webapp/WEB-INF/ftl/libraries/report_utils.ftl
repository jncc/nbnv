<#macro dataset_table providersWithQueryStats requestParameters>
    <#if providersWithQueryStats?has_content>
        <div class="tabbed tabbed-reports-dataset-table" id="nbn-dataset-selector-container">
            <h3>Data providers and their datasets that contribute to this page (number of records)<span id="nbn-select-datasets-text"><input id="nbn-select-all-datasets" type="button" value="Select All"/><input id="nbn-deselect-all-datasets" type="button" value="Deselect All"/></span><br />Sort by: <select id="nbn-select-datasets-orderby"><option value="1">Number of Records</option><option value="2">Organisation Name</option></select></span> </h3>
                <div id="nbn-dataset-ordered-table-byrecord">
                    <@ordered_datasets providersWithQueryStats=providersWithQueryStats?sort_by('querySpecificObservationCount')?reverse requestParameters=requestParameters />
                </div>
                <div id="nbn-dataset-ordered-table-byname">
                    <@ordered_datasets providersWithQueryStats=providersWithQueryStats?sort_by(['organisation', 'name']) requestParameters=requestParameters />
                </div>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#nbn-select-datasets-orderby').change(function() {
                    if($('#nbn-select-datasets-orderby').val() == 1) {
                        $('#nbn-dataset-ordered-table-byrecord').show();
                        $('#nbn-dataset-ordered-table-byname').hide();
                    }
                    if($('#nbn-select-datasets-orderby').val() == 2) {
                        $('#nbn-dataset-ordered-table-byrecord').hide();
                        $('#nbn-dataset-ordered-table-byname').show();
                    }
                });
                    $('#nbn-dataset-ordered-table-byname').hide();
            });
        </script>
    </#if>
</#macro>

<#macro ordered_datasets providersWithQueryStats requestParameters>
    <#assign selected = []>
    <#if requestParameters.selectedDatasets?has_content>
        <#assign selected = requestParameters.selectedDatasets?split(",")>
    </#if>
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
            <tr>
                <#if selected?has_content>
                    <#assign checked = selected?seq_contains(datasetWithQueryStats.datasetKey)?string("checked","")>
                </#if>
                <td><input type="checkbox" name="datasetKey" value="${datasetWithQueryStats.datasetKey}" ${checked}></td>
                <td><a href="/Datasets/${datasetWithQueryStats.datasetKey}">${datasetWithQueryStats.taxonDataset.title}</a> (${datasetWithQueryStats.querySpecificObservationCount})</td>
                <td>
                    <@datasetAccessPositions dataset=datasetWithQueryStats.taxonDataset/>
                </td>
            </tr>
        </#list>
    </#list>
            </table>

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
                        <td><a href="/Datasets/${unavailableDataset.datasetKey}">${unavailableDataset.taxonDataset.title}</a></td>
                        <td><a href="/Organisations/${unavailableDataset.taxonDataset.organisationID}">${unavailableDataset.taxonDataset.organisationName}</a></td>
                    </tr>
                </#list>
            </table>
        </div>
    </#if>
</#macro>

<#macro site_report_filters location requestParameters isSpatialRelationshipNeeded=true isDesignationNeeded=true isDatasetNeeded=true isDownloadButtonNeeded=false isMapLinkNeeded=false isRequestBetterAccessLinkNeeded=false args={}>
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
                    <button id="nbn-site-report-download-button">Download</button> species list<br />
                    <button id="nbn-download-observations-button">Download</button> observations
                </fieldset>
            </#if>
            <#if isMapLinkNeeded || isRequestBetterAccessLinkNeeded >
                <fieldset>
                    <legend>Other Options</legend>
                    <#if isMapLinkNeeded>
                        <a href="#" id="nbn-interactive-map">View on Interactive Map</a><br />
                    </#if>
                    <#if isRequestBetterAccessLinkNeeded>
                        <a href="#" id="nbn-request-better-access">Request Better Access</a>
                    </#if>
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
    <#assign toReturn="${gis}/SiteReport/${locationID}/${ptvk}?mode=map&LAYER=OS-Scale-Dependent&LAYERS=Records&spatialRelationship=${spatialRelationship}">
    <#if datasets??>
        <#assign datasetKeys="">
        <#list datasets as dataset>
            <#assign datasetKeys = datasetKeys + dataset.datasetKey>
            <#if dataset_has_next>
                <#assign datasetKeys = datasetKeys + ",">
            </#if>
        </#list>
        <#assign toReturn = toReturn + "&" + datasetKeys>
    </#if>
    <#if startYear?? && startYear?has_content>
        <#assign toReturn = toReturn + "&startYear=" + startYear>
    </#if>
    <#if endYear?? && endYear?has_content>
        <#assign toReturn = toReturn + "&endYear=" + endYear>
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

<#macro datasetAccessPositionByDatasetKey dataset taxonDataset>
    <#if dataset.typeName == "Taxon">
        <@datasetAccessPositions dataset=taxonDataset/>
    </#if>
</#macro>

<#macro datasetAccessPositions dataset>
    <#assign accessPositions=json.readURL("${api}/datasets/${dataset.key}/accessPositions")>
    <ul>
        <li>Public access: 
        <#if dataset.typeName == "Taxon">
            <#if dataset.publicResolution == "None">
                No access
            <#else>
                records available at ${dataset.publicResolution}
            </#if>
            <#if dataset.publicRecorder>
                <#if dataset.publicAttribute>
                    with recorder names and attributes
                <#else>
                    with recorder names 
                </#if>
            <#else>
                <#if dataset.publicAttribute>
                    with attributes
                </#if>
            </#if>
        <#else>
            all polygons
        </#if>
        <#if accessPositions?has_content>
            <#list accessPositions as accessPosition>
                <li>${accessPosition.owner} enhanced access: ${accessPosition.filterText}
            </#list>
        </#if>
    </ul>
</#macro>