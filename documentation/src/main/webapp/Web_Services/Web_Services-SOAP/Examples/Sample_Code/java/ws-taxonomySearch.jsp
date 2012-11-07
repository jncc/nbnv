<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
@author Richard Ostler, CEH Monks Wood
@date   28/02/2006
-->
<html>
<head><title>NBN Taxonomy Search Web Service</title></head>
<body>
<h1>NBN Gateway Taxonomy Search Web Service</h1>
<p>Use this web service to search for species information using either an NHM Taxon Version Key or a species name.</p>
<fieldset>
    <legend>Enter search term</legend>
    <form method="post" action="ws-taxonomySearchResults.jsp">
        <p>Taxon version key: <input type="text" name="tvk" /></p>
        <p><b>OR</b></p>
        <p>Species name: <input type="text" name="sn" /></p>
        <p><input type="submit" value="search" /></p>
    </form>
</fieldset>
</body>
</html>