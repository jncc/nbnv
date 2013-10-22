<#macro master title javascripts=[] csss=[]>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <link type="text/css" rel="stylesheet" href="/css/screen.css" />
            <#list csss as css>
                <link type="text/css" rel="stylesheet" href="${css}" />
            </#list>
            <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
            <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/jquery-ui.min.js"></script>
            <script type="text/javascript" src="/js/json2.js"></script> <!-- IE7 support -->
            <script type="text/javascript" src="/js/enable-collapsible-list.js"></script>
            <script type="text/javascript" src="/js/enable-maphilight.js"></script>
            <script type="text/javascript" src="/js/jquery.maphilight.min.js"></script>
            <script type="text/javascript" src="/js/jquery.query_string.js"></script>
            <script type="text/javascript" src="/js/nbn_namespace.js"></script>
            <script type="text/javascript" src="/js/jquery.slidorion.min.js"></script>
            <script type="text/javascript" src="/js/jquery.vticker-min.js"></script>
            <script type="text/javascript" src="/js/enable-slidorion.js"></script>
            <script type="text/javascript" src="/js/enable-vTicker.js"></script>
            <script type="text/javascript" src="/js/jquery.cookiesdirective.js"></script>
            <#list javascripts as javascript>
                <script type="text/javascript" src="${javascript}"></script>
            </#list>
            <script type="text/javascript">
                $(document).ready(function() {
                    $.cookiesDirective({
                        message: 'We use cookies on this site to enhance your user experience.',
                        explicitConsent: false, // false allows implied consent
                        position: 'top', // top or bottom of viewport
                        privacyPolicyUri: '/Terms/Privacy'
                    });
                });
            </script>
            <title>${title}</title>
        </head>
        <body>
            <div id="nbn-page-header"><#include "_header.ftl"></div>
            <#include "_breadcrumbs.ftl">
            <div id="nbn-page-content"><#nested></div>
            <div id="nbn-page-footer"><#include "_footer.ftl"></div>
            <div id="nbn-page-copyright">&copy; National Biodiversity Network 2012-2013</div>
            <script type="text/javascript">
                <!-- Google Classic Analytics -->
                var _gaq = _gaq || [];
                _gaq.push(['_setAccount', '${gaProp}']);
                _gaq.push(['_trackPageview']);

                (function() {
                    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
                })();
                <!-- End Google Classic Analytics -->

                <!-- Google Universal Analytics -->
                (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

                ga('create', '${gauProp}', 'nbn.org.uk');
                ga('send', 'pageview');
                <!-- End Google Universal Analytics -->
            </script>    
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
