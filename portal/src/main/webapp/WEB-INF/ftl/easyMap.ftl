<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <#if css??>
        <link type="text/css" rel="stylesheet" href="${css}" />
    </#if>
</head>
<body>
<#if errors??>
    Your request could not be completed because of the following errors.<br/>
    <#list errors as error>
         <li>${error}</li>
    </#list>
<#else>
    <div><span id="lblDistribution">
        <#if pageTitle??>
            <h3>${pageTitle}</h3>
        </#if>
        <#if showTC??>
            <p>The National Biodiversity Network records are shown on the map below. (See <a href="/Terms" target="_blank">terms and conditions</a>)</p>
        </#if>
        <img src="${gis}/SingleSpecies/${tvk}?${wmsParameters}"/>
        <#if imtLinkParms??>
            <br>
            <a href="/imt?${imtLinkParms}" target="_blank">Open interactive map in new window</a>
        </#if>
        <#if datasets??>
            <p>The following datasets are included:</p>
            <#list datasets as dataset>
                 <li>${dataset}</li>
            </#list>
        </#if>
        <#if showLogo??>
            <a href="/"><img src="/images/NBNPower.gif"></a>
        </#if>
    </span></div>
</#if>
</body>
</html>