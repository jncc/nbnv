<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:guidebookPage>
    <jsp:attribute name="body">
        <nbn:markdown>
#Overview

The following example shows an integration of MS Virtual Earth and the Web services
of the NBN Gateway with the following features:

* A user is able to define an polygon area on the map of MS Virtual Earth
* There are additional search options available, e.g. search only species found 
in the polygon, or overlapping the polygon
* After submitting, a javascript does an asynchronous call to the web services 
and gets all the species categories back
* The user can select a category, and a javascript does an asynchronous call to 
the web services and gets all the species belonging to this category back
* Finally, the user can select a species, and the records found will be displayed
on the map of MS Virtual Earth

Try the example here: [Example Webpage](examples/NBNAjaxSearchHelloWorld.html)
        </nbn:markdown>
    </jsp:attribute>
</t:guidebookPage>
