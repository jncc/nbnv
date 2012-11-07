<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:navigationDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Introduction
Welcome to the NBN Gateway's documentation pages. Here you will find help with
using the various products that the NBN Gateway has to offer.

The documentation has been split up into the following sections:
        </nbn:markdown>
        <nbn:navigation context="<%=config.getServletContext()%>" createTitle="false" depth="1"/>
    </jsp:attribute>

    <jsp:attribute name="navigation">
        <nbn:navigation context="<%=config.getServletContext()%>" createTitle="false" depth="2"/>
    </jsp:attribute>
</t:navigationDocumentationPage>
