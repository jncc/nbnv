<#assign tvk=URLParameters.tvk>
<#assign requestParametersExtended = RequestParameters + {"tvk":[tvk]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>

<@template.master title="NBN Grid Map" 
    javascripts=["/js/jquery.dataset-selector-utils.js","/js/jquery.gridmap_utils.js","/js/colourpicker/colorpicker.js"]
    csss=["/css/gridmap.css","/css/colourpicker/colorpicker.css"]>
    
    <h1>Grid map for ${taxon_utils.getShortName(taxon)}</h1>
    <#assign imageSize=4>
    <form target="" id="nbn-grid-map-form" gis-server="${gis}">
        <@gridMapFilters imageSize=imageSize/>
        <@gridMapContents tvk=tvk imageSize=imageSize/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </#if>
    </form>
</@template.master>

<#macro gridMapFilters imageSize>
    <div class="tabbed" id="nbn-grid-map-filter-container">
            <h3>Controls</h3>

            <input hidden name="imagesize" value="${imageSize}">
            <input hidden id="tvk" name="tvk" value="${tvk}">

            <label for="nbn-grid-map-resolution">Resolution</label>
            <select name="resolution" id="nbn-grid-map-resolution">
                <option value="10km">10km</option>
                <option value="2km">2km</option>
                <option value="1km">1km</option>
                <option value="100m">100m</option>
            </select>
            <fieldset>
                <legend>Regions</legend>
                <label for="nbn-region-selector">Coastlines</label>
                <select name="background" id="nbn-region-selector" disabled>
                    <option value="gbi">GB and Ireland</option>
                    <option value="gb">Great Britain</option>
                    <option value="i">Ireland</option>
                </select>
                <br/>
                <label for="nbn-form-label">Vice counties</label>
                <@viceCountyDropDown/>
            </fieldset>
            <fieldset>
                <legend>Date ranges and colours</legend>
                <@yearRangeText layerNum="1" hexColour="#ff0000" checkedText="checked"/> (top)<br/>
                <@yearRangeText layerNum="2" hexColour="#ff7f00" checkedText=""/> (middle)<br/>
                <@yearRangeText layerNum="3" hexColour="#ffff00" checkedText=""/> (bottom)<br/>
                Show outline: <input type='checkbox' id='nbn-show-outline' name='showOutline' checked><span class="nbn-form-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Outline colour: </span><@colourPicker idSuffix="-outline" hexColour="#000000"/>

            </fieldset>

            <fieldset>
                <legend>Overlays and backgrounds</legend>
                <input type="checkbox" value="os" name="background">Ordnance survey<br/>
                <input type="checkbox" value="vicecounties" name="background" disabled>Vice counties (not yet available)<br/>
                <input type="checkbox" value="100kgrid" name="background" disabled>100km grid (not yet available)<br/>
                <input type="checkbox" value="10kgrid" name="background" disabled>10km grid (not yet available)<br/>
            </fieldset>
            <input type="submit" value="Submit"></td>
    </div>
</#macro>

<#macro gridMapContents tvk imageSize>
    <div class="tabbed" id="nbn-grid-map-container">
        <h3>Map</h3>
        <img src="${gis}/SingleSpecies/${tvk}/map?imagesize=${imageSize}" id="nbn-grid-map-image">
    </div>
</#macro>

<#macro yearRangeText layerNum hexColour checkedText>
    <#assign currentYear=.now?string("yyyy")>
    Date ${layerNum}: 
    <input type='checkbox' name='gridLayer${layerNum}' value='gridLayer${layerNum}' ${checkedText}>
    from 
    <input type='text' name='startYear${layerNum}' value='1600' class='nbn-year-input'> 
    to 
    <input type='text' name='endYear${layerNum}' value='${currentYear}' class='nbn-year-input'>
    <@colourPicker idSuffix='-'+layerNum hexColour=hexColour/>
</#macro>

<#macro colourPicker idSuffix hexColour>
    <div id='nbn-colour-picker${idSuffix}' class='nbn-colour-picker' title='Change colour'>
        <div style='background-color: ${hexColour}'>
            <input hidden id="value-nbn-colour-picker${idSuffix}" name="value-nbn-colour-picker${idSuffix}" value="${hexColour}">
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