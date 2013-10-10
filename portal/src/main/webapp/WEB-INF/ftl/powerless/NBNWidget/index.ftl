<#setting url_escaping_charset='ISO-8859-1'>

<#assign wmsURL = "${gis}/SingleSpecies/">
<#assign wmsArgs = "?abundance=presence&LAYERS=OS-Scale-Dependent,Vice-counties,Grid-10km,Selected-Feature&FORMAT=image%2Fpng&TRANSPARENT=TRUE&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&SRS=EPSG%3A27700&BBOX=155994,155994,877191,877191&WIDTH=2024&HEIGHT=2024">

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>


<#if RequestParameters.tvk?has_content >
    <body>
        ${(wmsURL + RequestParameters.tvk) + wmsArgs}
        <img src=${(wmsURL + RequestParameters.tvk) + wmsArgs}/>
    </body>
<#else>
    <body>
       You must supply a tvk 
    </body>
</#if>

</html>