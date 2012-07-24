<#include "/WEB-INF/templates/_masterVars.ftl">

<#macro master title javascripts=[] csss=[]>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <link type="text/css" rel="stylesheet" href="/css/screen.css" />
            <#list csss as css>
                <link type="text/css" rel="stylesheet" href="${css}" />
            </#list>
            <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
            <script type="text/javascript" src="/js/enable-collapsible-list.js"></script>
            <#list javascripts as javascript>
                <script type="text/javascript" src="${javascript}"></script>
            </#list>
            <title>${title}</title>
        </head>
        <body>
            <div id="nbn-page-header"><#include "_header.ftl"></div>
            <div id="nbn-page-content"><#nested></div>
            <div id="nbn-page-footer"><#include "_footer.ftl"></div>
            <div id="nbn-page-copyright">&copy; National Biodiversity Network 2012</div>
        </body>
    </html>
</#macro>