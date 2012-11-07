<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="body" required="true"%>
<%@attribute name="head" %>
<%@attribute name="navigation" required="true"%>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<nbn:toc toc="toc" fragment="frag" depth="1">${body}</nbn:toc>

<t:documentationPage>
	<jsp:attribute name="body">
        <div id="nbn-page-navigation-menu">${navigation}</div>
        <div id="nbn-page-main-content">
            <div id="nbn-page-breadcrumbs"><%@include file="/WEB-INF/includes/breadcrumbs.jspf"%></div>
            <div id="nbn-page-main-content-toc"><h2>Contents</h2>${toc}</div>
            ${frag}
        </div>
    </jsp:attribute>
    <jsp:attribute name="head">${head}</jsp:attribute>
</t:documentationPage>