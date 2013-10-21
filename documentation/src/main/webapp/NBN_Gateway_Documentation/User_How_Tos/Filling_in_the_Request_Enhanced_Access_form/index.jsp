<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Filling in the Request Enhanced Access form

Use this [form](/AccessRequest/Create) to apply for additional enhanced access to the full records covering your species or geographically area of interest. Click on each of the following seven sections to fill in the information required for your request.

### [REQUIRED] **Access Details**  (Supply information for your request)

1. Select either yourself or an organisation you are requesting enhanced access on behalf of.
2. Select one of the predefined request purposes.
3. Add a detailed description of purpose for your request. This information will be sent on with your request to help the dataset administrators decide whether to accept or reject your request.

### [OPTIONAL] **Geographical Area** (Restrict request to an area)

 1. If you do not wish to restrict your request to a specific area leave the 'All areas' option selected.
 2. Otherwise select either 'Records that are 'within' or 'overlapping and within' option for a 10KM grid square or site boundary. For a 10KM grid square type in the required grid square. For a site choose one of the supplied site lists and then select the required individual site.

### [OPTIONAL] **Species** (Restrict request to species, designated list or taxon group)

1. If you do not wish to restrict your request to a specific species or group of species leave the 'All species' option selected.
2. Otherwise restrict your request by selecting one of the following four options
 - Single species or taxon. Type in the name and select the required species or taxon. A taxon at a higher taxonomic level (for example genus or family) with include all the child taxa (for example all species within a genus)
 - Select one of the supplied designated lists (see [Exploring designated lists](../Exploring_designated_lists))
 - Select one of the organisation supplied lists. These are supplied list of taxa maintained by an organisation for the purpose of requesting access to and downloading records from the NBN Gateway.
 - Select one of the supplied species groups.

### [OPTIONAL] **Year Range** (Restrict request to range of years)

1. If you do not wish to restrict your request to a specific year range leave the 'All years' option selected.
2. Otherwise select and supply the start and end years within the required year range (YYYY Format). 

### [OPTIONAL] **Datasets** (Restrict request to one or more datasets)

1. If you do not wish to restrict your request to a specific dataset or list of datasets leave the 'All datasets' option selected.
2. Otherwise restrict your request to one or more datasets by selecting one of the following two options
 - Select and type in the name of a single dataset.
 - Select one or more datasets from the dataset list. For this option you will also need to have restricted your request to either a geographical and/or species restriction. Tick include additional sensitive datasets if you wish to request access to additional relevant sensitive datasets not displayed in the dataset list.

### [OPTIONAL] **Request Access Until** (Request access for a limited time period)

1. If do not wish your request to be granted for a specific period of time then leave the 'Indefinitely' option selected
2. Otherwise select and fill in the last day you wish enhanced access to these records for. This may be up to 3 years in the future. After the final day your enhanced access to these records will automatically be revoked, reducing your access to public level access.

### [REQUIRED] **Confirm Access Request**  (Confirm and submit your access request)

1. You may need to go back through the form and complete any missing information highlighted in red before submitting your request.
2. Once each section has been correctly completed click and read the NBN Gateway [Terms and Conditions](/Terms)  to make sure your request and subsequent use of data complies with these terms and conditions.
3. Click on the 'Submit' button to submit your request.Your request will automatically be sent to the relevant datasets administrators so that they may respond to your request.You will be notified of their decision (either accepting or rejecting your request) via email. The current status of your request is available from 'Your Account' section of your account page by clicking on ['View Your Access Requests'](/AccessRequest).
        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>