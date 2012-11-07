<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#HOW TO USE THE GEOSEARCHING TOOL

The NBN Gateway website provides access to a substantial amount of UK biodiversity data. A wide range of public, private and voluntary bodies contribute this data to the NBN Gateway within distinct datasets. Data providers control the availability of their own datasets to the public, registered individuals and organisations.

The NBN Gateway has a range of tools to help you learn more about these biodiversity datasets as well as tools to help you find and interrogate any relevant data that they contain. This section will introduce the main tools currently available to you on the NBN Gateway and their functionality.

##A SPECIES REPORT FOR A 10KM SQUARE

The Geosearching tool provides a quick route in to find records for a particular 10km square within the national grids for Britain and Ireland. The tool presents a list of species groups that the Gateway holds records for within that 10km square. You can work down to map a single species in that 10km square.

![A SPECIES REPORT FOR A 10KM SQUARE](images/gateway7.png)

###How to Use the Geosearching Tool
1. Locate the Geosearching map ON the NBN Gateway home page. 

2. Click the 100km square and then, on the next page, the 10km square you are interested in.  
	Notice how the backdrop OS map changes scale as you zoom in to your target area.

3. Click the species group you are interested in. (The species groups and names are sourced from the NBN Species Dictionary hosted by the Natural History Museum)  
	The datasets used to generate this list of species groups are named at the bottom of the page. Those datasets to which you do not have sufficient access to see species level records are also given. Lists greater than 25 species are given over more than one page.

4. To download the entire list of species click the MS Excel or XML link at the bottom of the blue information box at the top left of the page. If you have download access to any of the datasets used you will see a download Species records link. You can use this to download a copy of all species records you have access to within your 10km square.  
	At the top of the page there are several record filters available. The first is a drop down list that allows you to limit your search to various priority species lists, e.g. BAP species only. The next allows you to change the species group selected. There is also a date filter for you to define a period of interest. You can also select whether you wish to view the results using Scientific or English names. 

5. Once you are happy with the filter settings click "refresh report".
	Please note that the species list available may change as a result of the filter. 

6. To report on a single species click the species name you are interested in.  
	You will see the records held appear in the map at the resolution available to you. The actual records are given in a table below the title of each dataset they are from, and a summary of the access you have. 

7. A download Species records link will be available if you have download access to the datasets used for your search. This link will appear at the bottom of the blue information box at the top left of the page.  
	After clicking the link you will be asked to reconfirm your agreement to the Gateway Terms and Conditions and then the email address you would like use to download the records.
        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
