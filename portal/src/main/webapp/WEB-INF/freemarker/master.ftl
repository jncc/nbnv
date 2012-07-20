<#macro master title>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <link type="text/css" rel="stylesheet" href="/portal/css/screen.css" />
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