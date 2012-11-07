<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#An introductory guide

#DATASETS ON THE GATEWAY

The Gateway has been developed to display and provide access to geospatially referenced data. On the Gateway these data are divided into the following classifications:

* Species datasets - usually species observation records represented by a single point of geographic location at a minimum 10km level of precision. Species datasets can be viewed and downloaded provided you have sufficient access.
* Habitat datasets - the NBN Gateway is in the early stages of handling habitat data and further development in this area is intended. Habitat datasets can be viewed and downloaded provided you have sufficient access.
* Geographic datasets - these include digital site and administrative boundaries, for example Sites of Special Scientific Interest and Scottish Unitary Authority boundaries. These are only available as overlays to maps on the NBN Gateway.
		
#DATASET METADATA

Each dataset is given a metadata page providing basic information about the particular resource and the data it contains. An example image for a species dataset is provided below. This can be viewed by clicking the title of any dataset. 		

![Screenshot of Dataset Metadata](images/gateway2.png) 

Screenshot: Pages from the metadata section > [show me live](http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?refID=5&dsKey=GA000311) (new window opens)

##GENERAL METADATA PAGE
Information (metadata) for a species dataset includes a title, description, access and use constraints. If you are logged in it will also include your level of access to the data it contains. There are links to other summary information as illustrated above. Habitat datasets have the same General Metadata page but only links to Survey and Attributes. Geographic datasets are only given a simplified General Metadata page.

##DATASET PROVIDERS
In principle the NBN Gateway is available for anyone to share the data they hold with others. In practice the NBN Trust advises that data be submitted by an organisation or group that have sufficient resources to manage, maintain and administer a dataset effectively. The Gateway currently has datasets from government agencies, academic institutions, private companies, voluntary recording schemes and societies, and local records centres.  Every dataset on the Gateway has an associated data provider. Data providers are given an organisation description page, which can be viewed by clicking their name on the Gateway.

#GAINING ACCESS TO DATA

The NBN Gateway is unique in the way it allows data owners who put their data on the system to control exactly who can do what with their data. Some owners have chosen to make their data publicly available and these data will be visible as soon as a person accesses the website. For data that have not been made generally available, or are only available at low levels of geographical resolution, a user will need to apply directly to the relevant data owner to ask them to grant the necessary access permissions. (See Section: Access and use controls below)

These are the steps needed for a data user to gain access to data:

* Registering on the Gateway - If users are not already registered then they need to do so before a data owner can give them more detailed access to their data on the Gateway. This can be done by following the "Register on-line now" link beside the "Log in" box accessed from the top of most of the Gateway web-pages.
* Reviewing a user's access to data - Most of the pages on the website, that relies on data, have a table that lists the available data to which a user has access. They also list those datasets that the user does not have access to. In addition, by following the "My account" link from the top of a page and clicking "Datasets you have access to" the user can see a list of which datasets they have access to and what levels of access they have to each.
* Identifying the datasets a user wants to use - Using the lists of datasets available to a user, the user can identify which other datasets they might like to use, or to which they would like to gain fuller access. In these lists, the title of the dataset is linked to a full metadata description of the dataset which will also help identify those datasets that will be of most use. Some datasets may indicate access restrictions in their metadata, for example when a dataset has no general access until testing has been carried out.
* Deciding what level of access a user needs - Having gone through the previous steps, the user should have an idea which datasets they would like to use and how to gain access to them through the Gateway. The next step is to decide what level of access is needed; that is: what spatial resolution is needed for access to the data and whether just viewing the data as dots on a map is enough, or whether they need to actually see the records behind these.
* Contacting the dataset's administrator - The user is now ready to contact the owner or administrator of the dataset. This can be done either by e-mailing them directly or by using the link to "Apply for access" under the heading "Your access to this dataset" from the web-page with the dataset description. This generates an automated e-mail access request. When asking for access to a dataset the user will need to give the owner the title of the dataset, what access level is needed (the spatial resolution and whether they need to access the actual data), together with as much additional information as possible to allow the owner to make a decision to grant access.
* Checking whether a user has access - If the dataset owner decides to grant access they should contact the applicant to let them know it has been done. The data owner is able to grant and withdraw access immediately using on-line administration tools (see Section: Dataset administration for information on this). A data user can check that they have access by following the "My account" link from the top of the Gateway pages and clicking "Datasets you have access to"

#ACCESS AND USE CONTROLS

The NBN Trust do not own any of the datasets made available through the NBN Gateway. The data providers control access to and use of their own data. The NBN Gateway helps them do this in two ways.

Firstly there is a framework of data access controls that Data Providers can use to restrict access to their data. The following controls can be applied differently for public users, registered organisations and individual users:

![Record Resolution](images/gateway3.png) 

* Set the spatial resolution at which they can view a dataset on a distribution map and as a grid-reference. The control affects the detail at which the locality associated with each record within a dataset can be seen. This can be set at 10km, 2km, 1 km or Full resolution (displayed as 100m on maps). 
* Allow access to records flagged as sensitive within a dataset.
* Grant ability to download data from the NBN Gateway. This is for a single species only and is limited to certain products. Full dataset download is under development. Downloaded data is always at the spatial resolution given above.
* Grant access to attributes associated with the data (e.g. sex, abundance, life-stage etc.). This can only be given once full spatial resolution is granted. Without attribute access, users only get the species name, locality (at resolution granted) and date. Attribute access excludes recorder and determiner's names.
* Grant access to any recorder and determiner names associated with each record.
* Grant permission to access online data validation tools which allow comments on specific records to be sent to the data provider.

Secondly there are terms and conditions that govern use of all material made available through the NBN Gateway. The Gateway Terms and Conditions can be viewed via a link at the bottom of every page of the website. Users are asked to read and agree to them when they register. Public users have to agree the terms before they are able to complete a data download. Data Providers may identify Additional Terms within the Use Constraints field of the metadata pages that support their dataset.

#REGISTERING & LOG IN

##REGISTERED INDIVIDUALS
Anyone can register as an individual user on the NBN Gateway. Registration gives you an individual user account. You can use this account to request new or better access to datasets and to apply for membership of Registered Organisations on the Gateway.

##REGISTERED ORGANISATIONS
In addition to individual user accounts it is also possible to register an organisation account on the Gateway. Individual users can apply to become a member of an organisation with an account on the Gateway. By doing so individual users can inherit the level of access given to the organisation by data providers. This enables a recognised group or organisation to build a coherent level of access for its members or staff.  For example, the Scottish Environment Protection Agency has an organisation account on the NBN Gateway. A member of SEPA staff administers this account. This individual has access to online administration controls that enable them to add and remove individual users as members.

Also see "[How to register and use your account](../How_to_Register_and_use_your_Account)" in the section "how to use the Gateway"

#NAVIGATING THE GATEWAY WEBSITE

From the home page  there are three ways to navigate around the site:

![Navigating the NBN Gateway](images/gateway4.png) 

* Typing a word or phrase into the search box. If the user is interested in a particular species or locality and knows its name, the fastest way to find out what is available is simply to type its name into the search box and click "Search". This will return a list of Gateway web-pages which are likely to be of interest You will find a search box at the top of every nbn.org.uk pages except the Forum
* Browsing through lists. We have provided list-based navigation to help when a user does not know what name to use or just wants to browse the website. The leads into these lists are to the left hand side of the Gateway home page. Click a suitable list topic and follow it through to get a list of relevant Gateway web-pages.
* Using map based navigation. If a user wants to explore data for a geographical area, the map on the home page may be a good starting point (under "Geosearching"). Begin the search by clicking on a 100km square on the map of Great Britain. This will lead to a map of the 10km squares within it. Click on a 10km square to view a species report.

#GATEWAY SEARCH FUNCTIONS

The NBN Gateway has a number of separate, but integrated search functions that present data and information in different ways, depending what the user wants to see, and what they are allowed to gain access to (see Section: Gaining access to data). The different search functions supported by the Gateway are:

##MAPPING THE DISTRIBUTION OF A SPECIES
This function allows a user to view a dot-distribution map of a species using datasets that they have selected which the Gateway has identified as containing the species. They can:

* Plot the distribution of a species on a grid, the scale of which can be selected from the available range of 10km, 2km, 1km and 100m squares, based on the Ordnance Survey national grid;
* Define which datasets are to be used;
* Define up to three date ranges, which will appear as different shades of a range of colours that can be selected;
* Limit the broad geographical area to be mapped, from a choice of Great Britain, Ireland or a specific Watsonian 'vice-county' in Britain (i.e. the standard biological recording areas adopted by biologists since the 1860's).
* For example, this Gateway function could be used to produce a distribution map of a species, within a vice-county, mapped on a 2km square ("tetrad") grid, with records from 1900-1950 shown in light blue and recent records in dark blue.

##INTERACTIVELY EXPLORING THE SPECIES DATA UNDERPINNING A DISTRIBUTION MAP
This page allows a user to explore the records of a species with respect to different backgrounds and overlays. They can:

* Zoom into and move around a distribution map
* Select records and view their underlying data
* Change the species being mapped
* Display records at different spatial resolutions
* Change the background, choosing currently from Ordnance Survey/OS Northern Ireland and Broad Habitats from the Land Cover Map 2000
* Overlay administrative or other boundaries (e.g. SSSIs, Wildlife Sites)
* Change datasets contributing to the map

#VIEWING TAXONOMIC INFORMATION FOR THE NAME OF AN ORGANISM

The taxonomy used by the Gateway is based on the NBN Species Dictionary and while some taxonomic areas are quite complete and accurate, with others there is much more work to be done. This page exposes the taxonomy the Gateway is using for a particular name including:

* The currently accepted name, with its authority
* What sort of organism it is (for example a dragonfly)
* Other names that the organism is known by, including miss-spelt names
* Taxa below the species in the taxonomic hierarchy (e.g. subspecies)
* A facility to feed any comments or errors directly back to the Natural History Museum, which manages the Species Dictionary

#LISTING THE SITES THAT A SPECIES HAS BEEN RECORDED ON

This allows a user to obtain a list of sites of a particular type that a species has been recorded on. The function only operates with those sites that the Gateway currently has available, which includes the main protected site designations across the UK. A user can:

* Include or exclude grid squares which overlap the site or just those that definitely fall within it
* Filter the records returned to a particular year range
* Select the datasets required to be included or excluded from the results of the search

#VIEWING A SPECIES LIST FOR A SITE

For those site boundaries that have been provided to the Gateway (for example the boundaries of all of the protected sites in the UK) this facility allows the user to view records that have been recorded within any given boundary. The function allows a user to:

* Limit the list of species returned to include only priority species listed under the UK Biodiversity Action Plan
* Include or exclude grid squares which overlap the site or just those that definitely fall within it
* Filter the records returned to a particular year range
* Select the datasets required to be included or excluded from the results of the search
        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
