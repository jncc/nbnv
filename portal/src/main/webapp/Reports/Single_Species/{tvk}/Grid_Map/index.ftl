<#assign tvk=URLParameters.tvk>
<#assign requestParametersExtended = RequestParameters + {"tvk":[tvk]}>
<#--<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>-->
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>

<@template.master title="NBN Grid Map" 
    javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js","/js/jquery.gridmap_utils.js","/js/colourpicker/colorpicker.js"]
    csss=["/css/colourpicker/colorpicker.css"]>
    
    <h1>Grid map for ${taxon_utils.getShortName(taxon)}</h1>
    <#assign imageSize=5>
    <div id="nbn-grid-map-filter-container">
        <@gridMapFilters imageSize=imageSize/>
    </div>
    <div id="nbn-grid-map-container">
        <@gridMapContents tvk=tvk imageSize=imageSize/>
    </div>
    <div class="nbn-clear-floating-elements"></div>
</@template.master>

<#macro gridMapFilters imageSize>
    <form target="" id="nbn-grid-map-form">
        <table class="nbn-coloured-table nbn-grid-map-table">
            <tr>
                <th colspan=2 class="nbn-th-left nbn-th-right">Controls</th>
            </tr>
            <tr>
                <td colspan="2" class="nbn-td-left nbn-td-right">
                <span class="nbn-form-label">Resolution</span>
                <span class="nbn-form-field">
                        <select name="resolution" id="nbn-grid-map-resolution">
                            <option value="10km">10km</option>
                            <option value="2km">2km</option>
                            <option value="1km">1km</option>
                            <option value="100m">100m</option>
                        </select>
                    </span>
                    <br/><br/>
                    <fieldset>
                        <legend>Regions</legend>
                        <span class="nbn-form-label">Coastlines</span>
                        <select name="background" id="nbn-region-selector" disabled>
                            <option value="gbi">GB and Ireland</option>
                            <option value="gb">Great Britain</option>
                            <option value="i">Ireland</option>
                        </select>
                        <br/><br/>
                        <span class="nbn-form-label">Vice counties</span>
                        <@viceCountyDropDown/>
                    </fieldset>
                    <br/><br/>
                    <fieldset>
                        <legend>Date ranges and colours</legend>
                        <@yearRangeText layerNum="1" hexColour="#ff0000" checkedText="checked"/> (top)<br/>
                        <@yearRangeText layerNum="2" hexColour="#ff7f00" checkedText=""/> (middle)<br/>
                        <@yearRangeText layerNum="3" hexColour="#ffff00" checkedText=""/> (bottom)<br/>
                        Show outline: <input type='checkbox' id='nbn-show-outline' name='showOutline' checked><span class="nbn-form-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Outline colour: </span><@colourPicker idSuffix="-outline" hexColour="#000000"/>

                    </fieldset>
                    <br/><br/>
                    <fieldset>
                        <legend>Overlays and backgrounds</legend>
                        <input type="checkbox" value="os" name="background">Ordnance survey<br/>
                        <input type="checkbox" value="vicecounties" name="background" disabled>Vice counties (not yet available)<br/>
                        <input type="checkbox" value="100kgrid" name="background" disabled>100km grid (not yet available)<br/>
                        <input type="checkbox" value="10kgrid" name="background" disabled>10km grid (not yet available)<br/>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-td-right nbn-td-contents-centre" colspan="2"><input type="submit" value="Submit"></td>
            </tr>
        </table>
        <input hidden name="imagesize" value="${imageSize}">
        <input hidden id="tvk" name="tvk" value="${tvk}">
    </form>
</#macro>

<#macro gridMapContents tvk imageSize>
    <#assign url="/nbnv-gis-0.1-SNAPSHOT/SingleSpecies/" + tvk + "/map?imagesize=" + imageSize>
    <table class="nbn-coloured-table">
        <tr>
            <th class="nbn-th-left nbn-th-right">Map</th>
        </tr>
        <tr>
            <td class="nbn-td-left nbn-td-right"><img src="${url}" id="nbn-grid-map-image-src"></td>
        </tr>
    </table>
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
            <option value="${viceCounty.featureID}">${viceCounty.name}</option>
        </#list>
    </select>
</#macro>