<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="body" required="true"%>
<%@attribute name="head" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <link type="text/css" rel="stylesheet" href="/Documentation/css/documentation.css" />
        <script type="text/javascript" src="/Documentation/js/documentation.js"></script>
        <title>NBN Learning Zone - National Biodiversity Network</title>
        ${head}
    </head>
    <body>
        <div id="nbn-page-header" class="clearfix"><%@include file="/WEB-INF/includes/header.jspf"%></div>
            <div id="nbn-page-content" class="clearfix">
                ${body}
            </div>
        <div id="nbn-page-footer"><%@include file="/WEB-INF/includes/footer.jspf"%></div>
        <div id="nbn-page-copyright"><%@include file="/WEB-INF/includes/copyright.jspf"%></div>
    </body>
</html>