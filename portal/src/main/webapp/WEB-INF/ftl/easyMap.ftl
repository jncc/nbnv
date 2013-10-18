<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<body>
<#if errors??>
    Your request could not be completed because of the following errors.<br/>
    <#list errors as error>
         <li>${error}</li>
    </#list>
<#else>
    https://staging-gis.nbn.org.uk/SingleSpecies/${tvk}?${wmsParameters}
    <img src=${gis}/SingleSpecies/${tvk}?${wmsParameters}/>
    <br/>
    Dataset List
    <br/>
     <#list datasets as datset>
         <li>${dataset}</li>
    </#list>
</#if>
</body>
</html>