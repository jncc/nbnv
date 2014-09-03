<#assign tvk=URLParameters.tvk>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>
<#assign ptaxon=json.readURL("${api}/taxa/${taxon.ptaxonVersionKey}")>
<#assign requestParametersExtended = RequestParameters + {"ptvk":[taxon.ptaxonVersionKey]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign startYear=RequestParameters.startYear?has_content?string(RequestParameters.startYear[0]!"","")>
<#assign endYear=RequestParameters.endYear?has_content?string(RequestParameters.endYear[0]!"","")>
<#assign spatialRelationship=RequestParameters.spatialRelationship?has_content?string(RequestParameters.spatialRelationship[0]!"overlap","overlap")>

<@template.master title="NBN Site Report" 
    csss=["/css/report.css","/css/species-site-list.css","/css/smoothness/jquery-ui-1.8.23.custom.css"]
    javascripts=["/js/report_utils.js","/js/species_site_list.js","/js/jquery.dataset-selector-utils.js","/js/jquery.dataTables.min.js"]>
    <h1>List of sites for ${taxon_utils.getLongName(ptaxon)}</h1>
    <form id="nbn-species-site-list-form" api-server="${api}" ptvk="${ptaxon.taxonVersionKey}" taxonOutputGroupKey="${ptaxon.taxonOutputGroupKey}">
        <div id="nbn-site-list-filter-container">
            <fieldset>
                <legend>Adjust which species records are used to create the site list</legend>
                <div>
                <div id="nbn-year-range-filter">
                    Year range: <input name="startYear" id="startYear" type="textbox" value="${startYear}" class="nbn-year-input"/>
                                to <input name="endYear" id="endYear" type="textbox" value="${endYear}" class="nbn-year-input"/>
                 </div>
                 <div id="nbn-spatial-relationship-filter">
                    Spatial relationship: 
                    <select name="spatialRelationship" id="spatialRelationship">
                        <#if spatialRelationship=="within" >
                            <option value="within" selected="selected">Records within site</option>
                            <option value="overlap">Records within or overlapping site</option>
                        <#else>
                            <option value="within">Records completely within site</option>
                            <option value="overlap" selected="selected">Records within or overlapping site</option>
                        </#if>
                    </select><br/>
                </div>
                <div style="float: left; width: 25%;">
                    <div style="float:left; width:30%;">Verification:</div>
                    <div style="float:left; width:70%">
                        <input id="verifiedSelector" name="verification" value="VERIFIED" type="checkbox" title="Record has been accepted as true by a verifier or acceptable system."/><label for="verifiedSelector">Verified</label><br/>
                        <input id="incorrectSelector" name="verification" value="INCORRECT" type="checkbox" title="Record has been accepted as false by a verifier or acceptable system."/><label for="incorrectSelector">Incorrect</label><br/>
                        <input id="uncertainSelector" name="verification" value="UNCERTAIN" type="checkbox" title="Record has some uncertainty as to its validity."/><label for="uncertainSelector">Uncertain</label><br/>
                        <input id="unverifiedSelector" name="verification" value="UNVERIFIED" type="checkbox" title="Record has no commentary or validity reports."/><label for="unverifiedSelector">Unverified</label><br/>
                    </div>
                </div>
                </div>
            </fieldset>
            <fieldset>
                <legend>Download</legend>
                <button id="nbn-site-report-download-button">Download</button> site list
            </fieldset>
            <fieldset>
                <legend>Other Options</legend>
                <a id="nbn-request-better-access" href="#">Request Better Access</a>
            </fieldset>
        </div>
        <div class="tabbed" id="nbn-species-site-list-container">
            <h3>Site list</h3>
            <div id="nbn-species-site-list-data"></div>
        </div>
    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
    </form>
    <@report_utils.downloadTermsDialogue/>
    <div id="nbn-download-error" style="display: none;" title="An error has occurred">
        <span id="nbn-download-error-text" />
    </div>
</@template.master>
