<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="body" required="true"%>
<%@attribute name="head" %>
<%@attribute name="showContentDiv" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>

<t:navigationDocumentationPage>
    <jsp:attribute name="navigation"><nbn:navigation context="<%=config.getServletContext()%>" root="/Web_Services"/></jsp:attribute>
    <jsp:attribute name="body">${body}</jsp:attribute>
    <jsp:attribute name="head">${head}</jsp:attribute>
    <jsp:attribute name="showContentDiv">${showContentDiv}</jsp:attribute>
</t:navigationDocumentationPage>