<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Documentation under review

Please note that the documentation is still being reviewed at this time and as such some documentation may be out of date or missing, we apologise for this and are working to update the documentation right now!
            
# Retrieving Records

To log into the API you first need to register as a user on the gateway (see [Getting started by creating your own user account](/Documentation/NBN_Gateway_Documentation/User_How_Tos/index#create_user_account).

Once you have your user credentials you can log into the API using the [user resource](../resources/restapi/resource_UserResource.html#path__user_login.html) and its login function detailed in the [Logging In](/Documentation/NBN_Gateway_Documentation/Web_Services-REST/Logging_In), for example if you are using JAVA to log into the API the following code snippet should log onto the API.

Once you have logged in and have your login cookie you can simply call the API one of the appropriate methods in the [TaxonObservation Resource](../resources/restapi/resource_TaxonObservationResource.html) with the appropriate filters to get access to the individual records.

You can request ehanched access to records through the [Request Access From](../../../NBN_Gateway_Documentation/User_How_Tos/Getting_better_access_to_records) on the Gateway itself

**Please be aware that all calls to the API are logged against your username and you have to be logged in to reach these endpoints.**

## Get records by dataset

You can grab all records in a dataset using the [Dataset endpoint in the Taxon Observation Resource](../resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_-datasetKey%20-.html)

Once you are logged in you can call the endpoint with a dataset key to return all observations at your users access level;

https://data.nbn.org.uk/api/taxonObservations/GA000466

## Get records by Taxon Version Key

You can grab all records for a particular Taxon Version Key through the <a href="../resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_-ptvk%20-.html">PTVK endpoint in the Taxon Observation Resource</a>

Once you are logged in you can call the endpoint with a valid TVK to return all observations at your users access level;

https://data.nbn.org.uk/api/taxonObservations/NBNSYS0000184793

## Get records by Taxon Observation ID

You can grab specific records from the gateway via the records ID using the the <a href="../resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_-id%20-.html">ID endpoint in the Taxon Observation Resource</a>

Once you are logged in you can call the endpoint with a valid record ID to return that observation at your users access level;

https://data.nbn.org.uk/api/taxonObservations/305857142

## Get records by combined filter

You can make a more complex query by supplying the api with the needed filters (defined below) using the [Filtered Taxon Observations Endpoint](../resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations.html)

Once you are logged in you can call the endpoint with a set of filters to return the records that you need and have access to.

There are some restrictions here, you must supply at least one of the following filters;

 * A Taxonomic Filter
 * Spatial Filter (A valid Polygon filter **does not** count as a spatial filter for this purpose) 
 * Dataset filter (**Must** include a taxonomic or spatial filter to select more than one dataset)
 
### Filters

The filters applicable to this resource are as follows;

 * Year Filters
  * ***startYear*** - A year indicating the start of a range, may be used without an end date to indicate records from this year onwards (i.e. ...&AMP;startYear=2000&AMP;...)
  * ***endYear*** - A year indicating the end of a range, may be used without a start date to indicate records until this year (i.e. ...&AMP;endYear=2012&AMP;...)
 * Taxonomic Filters
  * ***ptvk*** - A list of Taxon Version Key's i.e. (....&AMP;ptvk=[NHMSYS0020706144, NBNSYS0000176784,....]&AMP;....)
  * ***designation*** - A Taxon Designation Code (i.e. ...&AMP;designation=BERN-A1&AMP;...)
  * ***taxonOutputGroup*** - A Taxon Output Group ID (i.e. ...&AMP;taxonOutputGroup=NHMSYS0000079976&AMP;...)
 * Dataset Filters
  * ***datasetKey*** - A list of dataset keys i.e. (...&AMP;datasetKey=[GA000466,....]&AMP;...)
 * Spatial Filters
  * ***featureID*** AND ***spatialRelationship*** - Match observations depending on the ID of a feature and the spatial relationship (within / overlaps) (i.e. ...&AMP;featureID=GA0008850&AMP;spatialRelationship=within&AMP;...)
  * ***gridRef*** - A Grid Reference matching a supported grid i.e. OSGB, OSI, ... supports 10km down to 100m sqaures (i.e. ...&AMP;gridRef=TA2172&AMP;...)
  * ***polygon*** - WKT WGS-84 polygon string representing a custom location polygon (i.e. ...&AMP;polygon=...&AMP;...)
 * Other Filters
  * ***absence*** - If we want to only include absence records or not in our output (i.e. ...&AMP;absence=true&AMP;...)
  * ***sensitive*** - If we want to only include sensitive records in our query (i.e. ...&AMP;sensitive=true&AMP;...)

### Example

To download all Mollusc records (Taxon Output Group NHMSYS0000080088) within the 'Nene Valley' (GA00110783807) between 1970 and 2010 you could use the following url;

https://data.nbn.org.uk/taxonObservations?taxonOutputGroup=NHMSYS0000080088&AMP;featureID=GA00110783807  
&AMP;spatialRelationship=within&AMP;startYear=1970&AMP;endYear=2010

        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
