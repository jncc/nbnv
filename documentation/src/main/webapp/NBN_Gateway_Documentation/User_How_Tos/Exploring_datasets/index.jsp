<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Exploring datasets

Data on the NBN Gateway is supplied by a number of contributing [organisations](/Organisations) as datasets each with its own metadata. To help evaluate whether data from these datasets is fit for your intended use each organisation has its own page on the NBN Gateway. This page lists the datasets that the organisation has either supplied or contributed to, provides the contact details for the organisation and usually a link to their website for further information. On the metadata page for each dataset you can view that dataset's metadata and statistics, your level of access granted to the dataset and any constraints imposed on the use of the dataset by the organisation above the use conditions within the NBN Gateway [Terms and Conditions](/Terms). Links are also provided to view the species richness map of the dataset using the Interactive Map Tool, request better access to and download records from the dataset.

### To explore datasets

1. Click on 'Browse Datasets' and start typing in the name of the dataset in the search box. The search term can include any word in the name. If you do not know the name of the dataset then try typing in the organisation name to narrow the list of datasets to those provided by that organisation. Each column in the dataset table can be sorted by clicking on the column name.
2. Click on dataset name to go directly to the metadata page for that dataset or the organisation name to go to the organisation's page containing the list all the datasets supplied or contributed by that organisation.
3. On the metadata page you can
 1. Click on the name of the organisations providing or contributing to the dataset to view that organisation's page.
 2. View the dataset's metadata including description, summary of purpose and methods of data capture, geographical and temporal coverage, assessment of data quality and any additional information. Additionally the geographical tab displays a species richness map of the dataset and the temporal tab displays a graph of number of records per year. Click on the Map Link in the general tab to display the species richness map on the Interactive Map Tool.
 3. View the dataset's statistics of number of records and species. Record counts are further broken down by resolution on the geographical tab and by date type and year on the temporal tab. The list of species within the dataset are provided on the species tab, as well as the number of records and link to that species' species information page.
 4. View the dataset's survey metadata and statistics on the surveys tab and name and description of record attributes on the attributes tab.
 5. View your public and granted enhanced access on the access and constraints tab. This tab also includes any access or use constraints imposed on the dataset by the providing organisation. Click on the request better access link to go to the access request form for this dataset (see Filling in the Request Enhanced Access Form](../Filling_in_the_Request_Enhanced_Access_form)).
 6. Once registered and logged in (see [Getting started by creating your own user account](../Getting_started_by_creating_your_own_user_account)) you can download datasets at your level of access (see [Downloading records](../Downloading_records)) by clicking on the Download link provided on the general tab.

        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>