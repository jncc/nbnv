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
<#--        <@gridMapContents tvk=tvk imageSize=imageSize/>-->
    </div>
    <div class="nbn-clear-floating-elements"></div>
</@template.master>

<#macro gridMapFilters imageSize>
    <form target="" id="nbn-grid-map-form">
        <table class="nbn-coloured-table">
            <tr>
                <th colspan=2 class="nbn-th-left nbn-th-right">Controls</th>
            </tr>
            <tr>
                <td class="nbn-td-left">Region:</td>
                <td class="nbn-td-right">
                    <select name="region">
                        <option value="gbi">GB and Ireland</option>
                        <option value="gb">Great Britain</option>
                        <option value="i">Ireland</option>
                    </select>
                </td>
            </tr>
            </tr>
                <td class="nbn-td-left">Vice counties:</td>
                <td class="nbn-td-right">
                    <select name="vcNum">
                        <option value="1">Dummy</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left">Resolution:</td>
                <td class="nbn-td-right">
                    <select name="resolution" id="nbn-grid-map-resolution">
                        <option value="10km">10km</option>
                        <option value="2km">2km</option>
                        <option value="1km">1km</option>
                        <option value="100m">100m</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-td-contents-top">Date ranges:</td>
                <td class="nbn-td-right">
                    ${getYearRangeText("1", "#ff0000")}
                    ${getYearRangeText("2", "#ff7f00")}
                    ${getYearRangeText("3", "#ffff00")}
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-td-contents-top">Overlays and backdrops:</td>
                <td class="nbn-td-right">
                    <input type="checkbox" value="os">Ordnance survey<br/>
                    <input type="checkbox" value="os">Vice counties<br/>
                    <input type="checkbox" value="os">100km grid<br/>
                    <input type="checkbox" value="os">10km grid<br/>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-td-right nbn-td-contents-centre" colspan="2"><input type="submit" value="Submit"></td>
            </tr>
        </table>
        <input hidden id="nbn-grid-map-imagesize" value="${imageSize}">
    </form>
</#macro>

<#macro gridMapContents tvk imageSize>
    <#assign url="/nbnv-gis-0.1-SNAPSHOT/SingleSpecies/" + tvk + "/map?imagesize=" + imageSize + "&background=os">
    <table class="nbn-coloured-table">
        <tr>
            <th class="nbn-th-left nbn-th-right">Map</th>
        </tr>
        <tr>
            <td class="nbn-td-left nbn-td-right"><img src="${url}" id="nbn-grid-map-image-src"></td>
        </tr>
    </table>
</#macro>

<#function getYearRangeText layerNum hexColour>
    <#assign currentYear=.now?string("yyyy")>
    <#return "<input type='checkbox' name='gridLayer${layerNum}' value='startYear${layerNum}'> from <input type='text' name='startYear${layerNum}' value='1600' class='nbn-year-input'> to <input type='text' name='endYear${layerNum}' value='${currentYear}' class='nbn-year-input'><div id='nbn-colour-picker${layerNum}' class='nbn-colour-picker'><div style='background-color: ${hexColour}'></div></div><br/>">
</#function>