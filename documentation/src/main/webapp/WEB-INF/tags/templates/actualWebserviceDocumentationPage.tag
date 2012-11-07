<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@attribute name="introduction" required="true"%>
<%@attribute name="request" required="true"%>
<%@attribute name="response" required="true"%>

<t:webserviceDocumentationPage>
    <jsp:attribute name="body">
        <div><h1>Introduction</h1>${introduction}</div>
        <div><h1>Request</h1>${request}</div>
        <div><h1>Response</h1>${response}</div>
    </jsp:attribute>
</t:webserviceDocumentationPage>