<#assign tvk=URLParameters.tvk>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>
<#assign ptaxon=json.readURL("${api}/taxa/${taxon.ptaxonVersionKey}")>
<#assign requestParametersExtended = RequestParameters + {"ptvk":[taxon.ptaxonVersionKey]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign startYear=RequestParameters.startYear?has_content?string(RequestParameters.startYear[0]!"","")>
<#assign endYear=RequestParameters.endYear?has_content?string(RequestParameters.endYear[0]!"","")>
<#assign spatialRelationship=RequestParameters.spatialRelationship?has_content?string(RequestParameters.spatialRelationship[0]!"overlap","overlap")>

<@template.master title="NBN Site Report" 
    csss=["/css/species-site-list.css","http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]
    javascripts=["/js/jqueryui.simple-table-style.js","/js/report_utils.js","/js/species_site_list.js","/js/jquery.dataset-selector-utils.js","/js/jquery.dataTables.min.js"]>
    <h1>List of site boundaries for ${taxon_utils.getShortName(ptaxon)}</h1>
    <form id="nbn-species-site-list-form" api-server="${api}" ptvk="${ptaxon.taxonVersionKey}" taxonOutputGroupKey="${ptaxon.taxonOutputGroupKey}">
        <div id="nbn-site-list-filter-container">
            <fieldset>
                <legend>Adjust which species records are used to create the site boundary list</0.
legend>
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
            </fieldset>
        </div>
        <div class="tabbed" id="nbn-species-site-list-container">
            <h3>Site boundaries</h3>
            <div id="nbn-species-site-list-data"></div>
        </div>
    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
    </form>
</@template.master>
