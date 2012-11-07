<%@ page import="net.searchnbn.webservices.taxonomyservice.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.taxonomydata.Taxon"%>
<%@ page import="net.searchnbn.webservices.taxonomyquery.TaxonomySearchRequest"%>
<%@ page import="net.searchnbn.webservices.taxonomydata.TaxonomySearchList"%>
<%@ page import="net.searchnbn.webservices.taxonomydata.TaxonList"%>
<%@ page import="net.searchnbn.webservices.taxonomydata.TaxonName"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<!--
@author Richard Ostler, CEH Monks Wood
@date   28/02/2006
-->
<html>
<head><title>NBN Taxonomy Search Web Service</title></head>
<body>
<h1>NBN Gateway Taxonomy Search Results</h1>
<%
    GatewayWebService gws = new GatewayWebService();
    TaxonomySearchRequest ts = new TaxonomySearchRequest();
    String tvk = request.getParameter("tvk");
    String sn = request.getParameter("sn");

    if (tvk.length() > 0) {
        ts.setTaxonVersionKey(tvk);
    }
    if (sn.length() > 0) {
        ts.setSpeciesName(sn);
    }

    TaxonomySearchList taxonSearchList = gws.getNBNTaxonomySoapPort().getTaxonomySearch(ts);
  
    List<Taxon> list = taxonSearchList.getTaxon();
            %>
<p>Your search term: <%=tvk%><%=sn%></p>
<%
    int size = list.size();
    if (size > 0) {
        out.print("<strong>" + size + " matches found:</strong>");
        for (int i = 0; i < size; i++) {
            Taxon t = list.get(i);

            %>
            <h3><i><%=((TaxonName)(t.getTaxonName())).getValue()%></i>, <%=t.getAuthority()%> - <%=t.getTaxonVersionKey()%> - <%=t.getTaxonReportingCategory()%></h3>
            <% if (t.getSynonymList() != null && t.getSynonymList().getTaxon().size() > 0) { %>
                <h4>Synonyms</h4>
                <ul>
                <%
               TaxonList taxonList = t.getSynonymList();
               if(taxonList != null)
                   {
               List<Taxon> list2 = taxonList.getTaxon();
                for (int j = 0; j < list2.size(); j++) { %>
                    <li><i><%=((TaxonName)(list2.get(j).getTaxonName())).getValue()%></i>, <%=list2.get(j).getAuthority()%> - <%=list2.get(j).getTaxonVersionKey()%></li>
                <% }
                    }  %>
                </ul>
            <% }
            TaxonList nbnlowerTaxonList = t.getLowerTaxaList();
            if(nbnlowerTaxonList != null)
                {
                List<Taxon> list2 = nbnlowerTaxonList.getTaxon();
                if (list2  != null && list2 .size() > 0) { %>
                    <h4>Lower Taxa</h4>
                    <ul>
                    <%
                    for (int j = 0; j < list2.size(); j++) { %>
                        <li><i><%=((TaxonName)(list2.get(j).getTaxonName())).getValue()%></i>, <%=list2.get(j).getAuthority()%> - <%=list2.get(j).getTaxonVersionKey()%></li>
                    <% } %>
                    </ul>
                <% } 
                }
            TaxonList nbnTaxonAggregate = t.getAggregateList();
            if (nbnTaxonAggregate != null)
                {
                List<Taxon> list3 = nbnTaxonAggregate.getTaxon();
                     if (list3 != null && list3.size() > 0) { %>
                    <h4>Aggregates</h4>
                    <ul>
                    <%
                    for (int j = 0; j < list3.size(); j++) { %>
                        <li><i><%=((TaxonName)(list3.get(j).getTaxonName())).getValue()%></i>, <%=list3.get(j).getAuthority()%> - <%=list3.get(j).getTaxonVersionKey()%></li>
                    <% }
                }         %>
                    </ul>
                <% }  %>
            <hr size="1" noshade="noshade" />
        <% }
    } else { %>
        <p>Your search did not produce any hits.</p>
<%    }%>
</body>
</html>