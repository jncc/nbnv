<#assign tvk=URLParameters.tvk>
<#assign taxon=json.readURL("${api}/taxa/${tvk}/ptvk")>
<#assign requestParametersExtended = RequestParameters + {"ptvk":[taxon.ptaxonVersionKey]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign unavailableDatasets=json.readURL("${api}/taxonObservations/unavailableDatasets",requestParametersExtended)>

<@template.master title="NBN Grid Map" 
    javascripts=["/js/jquery.dataset-selector-utils.js","/js/jquery.gridmap_utils.js","/js/report_utils.js","/js/colourpicker/colorpicker.js"]
    csss=["/css/report.css","/css/gridmap.css","/css/colourpicker/colorpicker.css","/css/smoothness/jquery-ui-1.8.23.custom.css"]>
    
    <h1 style="margin:0;">Grid map for ${taxon_utils.getLongName(taxon)}</h1>
    <#if tvk != taxon.ptaxonVersionKey>
        <#assign originalTaxon=json.readURL("${api}/taxa/${tvk}")>
        <h3 style="margin:0 0 20px 0;">Redirected from ${taxon_utils.getLongName(originalTaxon)} (${tvk})</h3>
    </#if>
    <form target="" id="nbn-grid-map-form" gis-server="${gis}" api-server="${api}">
        <@gridMapFilters/>
        <@gridMapContents tvk=taxon.ptaxonVersionKey/>
        <@mdcontent.smallCaveat/>
        <div style="width: 100%">
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </div>
    </form>
    <br><br>
    <@report_utils.unavailable_datasets unavailableDatasets=unavailableDatasets/>
</@template.master>

<#macro gridMapFilters>
    <div class="tabbed" id="nbn-grid-map-filter-container">
            <h3>Controls</h3>

            <input type="hidden" id="tvk" name="tvk" value="${taxon.ptaxonVersionKey}">

            <fieldset>
                <legend>Resolution</legend>
                <label for="nbn-grid-map-resolution">Resolution of squares displayed</label>
                <select name="resolution" id="nbn-grid-map-resolution">
                    <option value="10km">10km</option>
                    <option value="2km">2km</option>
                </select>
            </fieldset>
            <fieldset>
                <legend>Zoom to area</legend>
                <label for="nbn-region-selector">Region</label>
                <select name="nationalextent" id="nbn-region-selector">
                    <option value="gbi">GB and Ireland</option>
                    <option value="gb">Great Britain</option>
                    <option value="ireland">Ireland</option>
                </select>
                <br/>
                <label for="nbn-form-label">Vice county</label>
                <@viceCountyDropDown/>
            </fieldset>
            <fieldset>
                <legend>Date ranges and colours</legend>
                <@yearRangeText layerNum="1" hexColour="#ffff00" checkedText="checked"/> (bottom)<br/>
                <@yearRangeText layerNum="2" hexColour="#ff7f00" checkedText=""/> (middle)<br/>
                <@yearRangeText layerNum="3" hexColour="#ff0000" checkedText=""/> (top)<br/>
                Show outline: <input type='checkbox' id='nbn-show-outline' name='showOutline' checked colourPickerId='nbn-colour-picker-outline'><span class="nbn-form-label">&nbsp;&nbsp;&nbsp;&nbsp;Outline colour: </span><@colourPicker idSuffix="-outline" hexColour="#000000"/>

            </fieldset>
            <fieldset>
                <legend>Overlays and backgrounds</legend>
                <div id="nbn-background-checkbox-group">
                    <input type="checkbox" id="nbn-grid-map-coastline" value="gbi" name="background" checked>Coastline<br/>
                    <input type="checkbox" id="nbn-grid-map-os" value="os" name="background">Ordnance survey<br/>
                    <input type="checkbox" id="nbn-grid-map-vicecounty" value="vicecounty" name="background">Vice counties
                </div>
                <div>
                    <input type="checkbox" id="nbn-grid-map-100k-grid" value="gbi100kextent" name="background">100km grid<br/>
                    <input type="checkbox" id="nbn-grid-map-10k-grid" value="gbi10kextent" name="background">10km grid
                </div>
            </fieldset>

            <fieldset>
                <legend>Download</legend>
                <button id="nbn-grid-map-squares-download">Download</button> <span id="nbn-grid-map-resolution-download-text">10km</span> squares within selected dates
                <button id="nbn-download-observations-button">Download</button> Download Records
                <@report_utils.downloadTermsDialogue/>
            </fieldset>

            <fieldset>
                <legend>Other Options</legend>
                <a id="nbn-interactive-map" href="#">View on Interactive Map</a><br />
                <a id="nbn-request-better-access" href="#">Request Better Access</a><br />
                <#if tvk != taxon.ptaxonVersionKey>
                    <a id="nbn-request-better-access" href="/Taxa/${tvk}/Grid_Map/Actual">View Grid Map for Original Taxon Version Key</a>
                </#if>
            </fieldset>

    </div>
</#macro>

<#macro gridMapContents tvk>
    <div class="tabbed nbn-map-image-container-startup-height" id="nbn-grid-map-container">
        <h3>Map</h3>
        <img id="nbn-grid-map-busy-image" src='/img/ajax-loader-medium.gif'>
        <img id="nbn-grid-map-image" class="nbn-centre-element">
        <@report_utils.OSCopyright/>
    </div>
</#macro>

<#macro yearRangeText layerNum hexColour checkedText>
    <#assign currentYear=.now?string("yyyy")>
    Date ${layerNum}
    <input type='checkbox' name='gridLayer${layerNum}' value='gridLayer${layerNum}' ${checkedText} colourPickerId='nbn-colour-picker-${layerNum}'>
    from 
    <input type='text' name='startYear${layerNum}' value='1600' class='nbn-year-input'> 
    to 
    <input type='text' name='endYear${layerNum}' value='${currentYear}' class='nbn-year-input'>
    <@colourPicker idSuffix='-'+layerNum hexColour=hexColour/>
</#macro>

<#macro colourPicker idSuffix hexColour>
    <div id='nbn-colour-picker${idSuffix}' class='nbn-colour-picker' title='Change colour'>
        <div style='background-color: ${hexColour}'>
            <input type="hidden" id="value-nbn-colour-picker${idSuffix}" name="value-nbn-colour-picker${idSuffix}" value="${hexColour}">
        </div>
    </div>
</#macro>

<#macro viceCountyDropDown>
    <#assign viceCounties=json.readURL("${api}/siteBoundaryDatasets/GA000344/siteBoundaries")>
    <select name="feature" id="nbn-vice-county-selector" class="nbn-report-filter-dropdown">
        <option value="none">None</option>
        <#list viceCounties as viceCounty>
            <option value="${viceCounty.identifier}">${viceCounty.name}</option>
        </#list>
    </select>
</#macro>

