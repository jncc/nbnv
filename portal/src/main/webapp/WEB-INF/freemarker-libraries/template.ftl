<#macro master title javascripts=[] csss=[]>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <link type="text/css" rel="stylesheet" href="/css/screen.css" />
            <#list csss as css>
                <link type="text/css" rel="stylesheet" href="${css}" />
            </#list>
            <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
            <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/jquery-ui.min.js"></script>
            <script type="text/javascript" src="/js/enable-collapsible-list.js"></script>
            <script type="text/javascript" src="/js/enable-maphilight.js"></script>
            <script type="text/javascript" src="/js/jquery.maphilight.min.js"></script>
            <script type="text/javascript" src="/js/jquery.query_string.js"></script>
            <script type="text/javascript" src="/js/jquery.pagination.js"></script>
            <script type="text/javascript" src="/js/jquery.nbn_search.js"></script>
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


<#macro error title> 
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>${title}</title>
            <link rel="stylesheet" type="text/css" href="/css/unavailable.css">
        </head>
        <body><div id="container"><#nested></div></body>
    </html>
</#macro>
