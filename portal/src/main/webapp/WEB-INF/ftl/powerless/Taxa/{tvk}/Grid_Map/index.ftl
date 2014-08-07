<#assign tvk=URLParameters.tvk>
<#assign requestParametersExtended = RequestParameters + {"ptvk":[tvk]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>
<#assign unavailableDatasets=json.readURL("${api}/taxonObservations/unavailableDatasets",requestParametersExtended)>
<@template.master title="NBN Grid Map" 
    javascripts=["/js/jquery.dataset-selector-utils.js","/js/jquery.gridmap_utils.js","/js/report_utils.js","/js/colourpicker/colorpicker.js"]
    csss=["/css/report.css","/css/gridmap.css","/css/colourpicker/colorpicker.css","/css/smoothness/jquery-ui-1.8.23.custom.css"]>
    
    <h1>Grid map for ${taxon_utils.getLongName(taxon)}</h1>
    <form target="" id="nbn-grid-map-form" gis-server="${gis}" api-server="${api}">
	<div id="nbn-grid-filters-container">
	    <@gridMapFilters/>
	    <@gridMapExtras/>
	</div>
        <@gridMapContents tvk=tvk/>
        <@mdcontent.smallCaveat/>
        <div style="width: 100%">
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </div>
    </form>
    <br><br>
    <@report_utils.unavailable_datasets unavailableDatasets=unavailableDatasets/>
</@template.master>

<#macro gridMapFilters>
    <div class="tabbed" id="nbn-grid-filter-container">
            <h3>Controls</h3>

            <input type="hidden" id="tvk" name="tvk" value="${tvk}">

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
		<div class="nbn-tabs">
		    <ul>
                        <li><a href="#tabs-1" id="nbn-verification-tab">Verification</a></li>			
                        <li><a href="#tabs-2" id="nbn-date-tab">Dates</a></li>
		    </ul>
		    <div id="tabs-1">
			<@verificationStatus status="Verified" value=1 hexColour="#0000ff" checkedText="checked"/><br/>
			<@verificationStatus status="Incorrect" value=2 hexColour="#ff0000" checkedText=""/><br/>
			<@verificationStatus status="Uncertain" value=3 hexColour="#cccccc" checkedText="checked"/><br/>
			<@verificationStatus status="Unverified" value=4 hexColour="#cccccc" checkedText="checked"/><br/>
			<div><span class="nbn-quality-layer-label">Outline:</span> <input type='checkbox' id='nbn-verification-outline-checkbox' name='showOutlineVerification' checked colourPickerId='nbn-colour-picker-outline-verification'><span class='nbn-quality-color-picker'><@colourPicker idSuffix="-outline-verification" hexColour="#000000"/></span></div>
		    </div>
		    <div id="tabs-2">
			<@yearRange layerNum="1" hexColour="#ffff00" checkedText="checked" layerPosition="Top"/><br/>
			<@yearRange layerNum="2" hexColour="#ff7f00" checkedText="" layerPosition="Middle"/><br/>
			<@yearRange layerNum="3" hexColour="#ff0000" checkedText="" layerPosition="Bottom"/><br/>
			<div><span class="nbn-date-layer-label">Outline:</span> <input type='checkbox' id='nbn-show-outline' name='showOutline' checked colourPickerId='nbn-colour-picker-outline'><@colourPicker idSuffix="-outline" hexColour="#000000"/></div>
		    </div>
		</div>
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
    </div>
</#macro>

<#macro gridMapExtras>
    <div class="tabbed" id="nbn-grid-filter-container">
            <h3>Extras</h3>
            <fieldset>
                <legend>Downloads</legend>
                <button id="nbn-grid-map-squares-download">Download <span id="nbn-grid-map-resolution-download-text">10km</span> squares</button>
                <button id="nbn-download-observations-button">Download records</button>
                <@report_utils.downloadTermsDialogue/>
            </fieldset>

            <fieldset>
                <legend>Other Options</legend>
                <a id="nbn-interactive-map" href="#">View on Interactive Map</a><br />
                <a id="nbn-request-better-access" href="#">Request Better Access</a>
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

<#macro yearRange layerNum hexColour checkedText layerPosition>
    <#assign currentYear=.now?string("yyyy")>
    <span class="nbn-date-layer-label">${layerPosition}:</span>
    <input type='checkbox' name='gridLayer${layerNum}' value='gridLayer${layerNum}' ${checkedText} colourPickerId='nbn-colour-picker-${layerNum}'>
    from 
    <input type='text' name='startYear${layerNum}' value='1600' class='nbn-year-input'> 
    to 
    <input type='text' name='endYear${layerNum}' value='${currentYear}' class='nbn-year-input'>
    <@colourPicker idSuffix='-'+layerNum hexColour=hexColour/>
</#macro>

<#macro verificationStatus status value hexColour checkedText>
    <span class="nbn-quality-layer-label">${status}:</span>
<!--    <input type='checkbox' name='gridLayerStatus${status}' value='gridLayerStatus${status}' ${checkedText} colourPickerId='nbn-colour-picker${status}'>-->
    <input type='checkbox' name='verificationCheckBox' value='${value}' ${checkedText} colourPickerId='nbn-colour-picker-${status}'>
    <span class='nbn-quality-color-picker'><@colourPicker idSuffix='-'+status hexColour=hexColour/></span>
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

