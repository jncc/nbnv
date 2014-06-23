<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#NBN Gateway Web Services Examples
This tutorial will provide a guided tour of the Gateway web services and examples of how to use them in a variety of programming languages (JavaScript, C#, Java, PHP). Along the way other technologies will be introduced that will help you to build powerful web pages that will help you to make the most of the web services. The examples will illustrate common uses of the NBN Gateway web services.

#Contents
 * REST Web Services
  * [Overview of the NBN Gateway REST web services](Overview)
  * [The NBN client class](NBN_Client)
  * [10km square species list](Ten_KM_Species_List)
  * [Site species list](Site_Species_List)
  * [Taxonomy search](Taxon_Search)
  * [Records for a 10km square](Ten_KM_Records)
  * [Records for a user-defined polygon](Records_for_Polygon)
  * [Records for a point and buffer](Point_and_Buffer)  

        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
