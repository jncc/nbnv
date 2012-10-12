<#assign tvk=URLParameters.tvk>
<#assign requestParametersExtended = RequestParameters + {"tvk":[tvk]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>

<@template.master title="NBN Grid Map" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js","/js/jquery.gridmap_utils.js"]>
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
                <td class="nbn-td-left">Resolution:</td>
                <td class="nbn-td-right">
                    <input type="submit" value="submit">
                </td>
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