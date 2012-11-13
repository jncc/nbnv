<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Web Services Resources

Below are some web service resources which you may find useful when developing with the NBN web services

## Logo for implentations of NBN web services
This logo [![NBN powered by web services logo](resources/powered-by-NBN3-70-wide.jpg)](resources/powered-by-NBN3-70-wide.jpg) 
should be used on all implementations of NBN web services (see the document below for all implementation rules)

## Web services implementation rules
[This document](resources/NBN-Gateway-Web-Service-Implementation-Rules-v1.pdf)  sets out the rules that 
must be followed by anyone presenting data from the NBN Gateway to others, for example through a 
website or intranet system.

## Date Filtering
[This document](resources/DateFiltering.pdf) details how date filtering is performed on The NBN Gateway.

The NBN Gateway uses the concept of vague dates to store date information. A vague 
date specifies a start date and an end date of a date range together with a date 
type code identifying the form of the date type. This concept provides great 
flexibility when storing date information allowing anything from an exact date 
(i.e. 20/08/1976) to a year range (i.e. 1980-1990) or even a pre or post date (i.e. before 1950).

## Designation Types 
Used as part of the [designation query](../Actual_Services/Designation_List/) attribute. 
These are the values you should use when filtering by a taxon designation.

These can be obtained from this [csv file](resources/taxonDesignationTypes.csv)

## Taxon Groups
List of NBN Taxon Reporting Category names and keys. The taxon group keys are 
those you should use when filtering on a taxon group. 

These can be obtained from this [csv file](resources/taxonGroups.csv)
        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>