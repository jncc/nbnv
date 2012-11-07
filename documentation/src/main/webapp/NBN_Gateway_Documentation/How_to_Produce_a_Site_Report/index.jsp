<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#PRODUCING A SITE REPORT

The NBN Gateway can be used to search for species records for a particular site. This functionality is referred to as a site report. The Geographic datasets held by the Gateway form the basis for site reports. You can submit digitised boundaries that are important to you to the Gateway as Geographic datasets. There are two shortcuts to information on the NBN Gateway homepage that produce site reports. You can use these to find a site report by name or map. 

#How to Find a Site Report by:
##NAME
1. Click "Explore Sites" and then "Browse Sites" on the NBN Gateway home page.
2. Select the type of site you are interested in and then, on the next page, use the alphabetized menu to find and click the name of the site in which you are interested.

##MAP
1. Click "Explore Sites" and then "Find site on the map" on the NBN Gateway home page.
2. You will be presented with a UK map you can use to zoom into and click a site polygon, after selecting the type of site you are interested in on the right hand side.  
	You will now be presented with the digitised site boundary against an OS map backdrop. You will also see a list of species groups for which records are held that may relate to that site.
3. Click the species group you are interested in.  
	The datasets used to generate this list of species groups are named at the bottom of the page. You can add and remove datasets from the report by using the tick box and refreshing the report. Datasets that are not available to you are listed separately at the bottom. Lists greater than 25 species are given over more than one page.
4. To download the entire list of species click the MS Excel or XML link at the bottom of the blue information box at the top left of the page. If you have download access to any of the datasets used you will see a download Species records link. You can use this to download a copy of all species records you have access to within your 10km square.  
	At the top of the page there are several record filters available. The first is a drop down list that allows you to limit your search to various priority species lists, e.g. BAP species only. The second allows you to specify whether you want only records that definitely fall within the site, or whether you are happy to include those that overlap the site boundary. 1km to 10km records often overlap smaller sites. The next allows you to change the species group selected. The forth filter allows you to set the resolution at which you would like the records to be mapped, but your access levels may limit the results of this. There is also a date filter for you to define a period of interest. You can also select whether you wish to view the results using Scientific or English names. 
5. Once you are happy with the filter settings click "refresh report".
	Please note that the species list available may change as a result of the filter. 
6. To report on a single species click the species name you are interested in.   
	You will see the records held appear in the map at the resolution available to you. The map will fit the size of the record rather than site if its resolution is larger than the site. The actual records are given in a table below the title of each dataset they are from, and a summary of the access you have. 
7. A download Species records link will be available if you have download access to the datasets used for your search. This link will appear at the bottom of the blue information box at the top left of the page.  
	After clicking the link you will be asked to reconfirm your agreement to the Gateway Terms and Conditions and then the email address you would like use to download the records.		
        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
