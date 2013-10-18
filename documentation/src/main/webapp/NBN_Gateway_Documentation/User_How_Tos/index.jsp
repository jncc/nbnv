<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Gateway User How To's

#Documentation under review

Please note that the documentation is still being reviewed at this time and as such some documentation may be out of date or missing, we apologise for this and are working to update the documentation right now!

* [Getting started by creating your own user account](#create_user_account)
* [Getting better access to records](#getting_better_access)
* [Filling in the Request Enhanced Access form](#filling_enhanced_access)
* [Exploring species](#exploring_species)
* [Using the Interactive Map Tool](#interactive_map)
* [Exploring a site or 10KM grid square](#exploring_sites)
* [Exploring designated lists](#exploring_designated_lists)
* [Exploring datasets](#exploring_datasets)
* [Downloading records](#downloading_records)
* [Sending my suggestions and feedback](#feedback)


## <a id="create_user_account"></a> Getting started by creating your own user account

1. Click on ["Login: Need an account? Sign up free"](/User/Register) from home page.
2. Enter a username, password and account details to register account.
3. Read [Terms and Conditions](/Terms).
4. You will be sent a verification e-mail. Follow the link to setup your account.
5. Login to access your [account page](/User). Click on ['Can't access your account?'](/User/Recovery) if you want your username or password to be emailed to you.
6. You can change your user details by clicking on ['Change Your User Details'](/User/Modify) in the 'Your Account' section on your account page.

## <a id="getting_better_access"></a> Getting better access to records

Once registered you have access to records at the resolution granted to the public by the data providers. If this public resolution is not adequate then you can request enhanced access to the full records through the NBN Gateway. Your account page provides the links to request and review the status of your requests.

Click on ['Create New Access Request'](/AccessRequest/Create) to go to the access request form. Guidance on how to fill out this form is provided below (see [Filling in the Request Enhanced Access Form](#filling_enhanced_access))
Click on ['View Your Access Requests'](/AccessRequest) to see the current status of your access requests

Remember to login to automatically receive your additional enhanced access when using the NBN Gateway to map and view species records. Links to the access request form are also provided directly from a number of NBN Gateway pages so that you can request enhanced access for your current search criteria.

If you are a member of an organisation on the NBN Gateway then this organisation can also request enhanced access on your behalf. Your account page provides the links to request and review your status of organisation membership on the NBN Gateway.

Click on ['Join an Organisation'](/Organisations/Join) to request to become a member of an organisation on the NBN Gateway.
View the status of your organisation membership in 'Your Organisation Membership' section.

## <a id="filling_enhanced_access"></a> Filling in the Request Enhanced Access form

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
 - Select one of the supplied designated lists (see [Exploring designated lists](#exploring_designated_lists))
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

 
## <a id="exploring_species"></a> Exploring species
 
The NBN Gateway provides a number of reporting pages to explore species' records and information supplied by [organisations](/Organisations). These pages provide information on the species taxonomy, current designation status, list of sites, national and local maps of species records, species reports for sites and 10KM grid squares.

Each reporting page also provides a link to [request better access](/AccessRequest/Create) (see [Getting better access to records](#getting_better_access) and [download](/Download) the species records (see [Downloading records](#downloading_records))

### To explore species

1. From the main menu click on 'Browse Species' and enter the name of the species or higher taxon, for example genus, in the search box. Names can either be the currently used scientific name, a common name, other previously used scientific names, a higher taxon, for example genus, or the species code (first 2 letters of the genus and first 3 letters of the species name).
2. The name should appear towards the top of the Browse species table. The preferred name, as used in the NBN Gateway, and the number of records are also displayed. Click on the link of the species or taxon name to go to its information page.
3. The information page summarises the taxonomic information and current designation status for the species, as well as listing the organisations and datasets that have contributed records for that species. A map of these species records is displayed and relevant links to external sites for further information may be available. The 'Explore and Download Records' section provides access to the reporting pages and download wizard for that species or taxon. For a higher level taxon these reports and download contain the corresponding lower level taxa, for example records for all the species within a genus.
4. Click on '**Grid Map**' to go to the report displaying the grid squares for the species or taxon on a map, along with list of the contributing datasets and your level of access. On this page you can
 1. Select or deselect datasets contributing to the report using the datasets tick boxes. Organisations can be sorted alphabetically by selecting 'Organisation Name' in the Sort by box above the list of datasets. Click on the title of the dataset to view the metadata for that particular dataset (see [Exploring datasets](#exploring_datasets)).
 2. Display up to three date groups by providing the year ranges for these groups in the Date ranges and colours control. Click on the colour square for that particular date group to change colour of the grid squares displayed on the map. Tick the date group to display the grid squares for that your range on the map. 
 3. Add a 100KM or 10KM grid, vice-countries boundaries or Ordnance Survey map background within the Overlays and backgrounds controls.
 4. Zoom to Great Britain, Ireland or a vice-county in the Zoom to area control. The displayed grid square resolution can be increased up to 1KM resolution when zoomed in to a vice-county using the Resolution Control.
 5. Click on 'Request Better Access' in the Other Options to go to the access request form (see [Filling in the Request Enhanced Access Form](#filling_enhanced_access))
 6. Click Download button to download either the list of grid squares displayed on the map into a csv file, along with the datasets' metadata, or the records contributing to the map (see [Downloading records](#downloading_records)). 
 7. Click 'View on Interactive Map' to display the species directly in the Interactive Map Tool (see [Using the Interactive Map Tool](#interactive_map))
5. Click on '**Interactive Map**' to display and explore the species records using the Interactive Map Tool  (see [Using the Interactive Map Tool](#interactive_map)).
6. Click on '**List of sites**' to go to the report listing the sites for the species. The sites, ranging from small local nature reserves up to the much larger vice-counties, have been supplied by organisations. In addition to including records that are wholly within the site the list of sites may also include sites where all the records overlap but are not necessary within the sites. On this page you can
 1. Select or deselect datasets contributing to the report using the datasets tick boxes. Organisations can be sorted alphabetically by selecting 'Organisation Name' in the Sort by box above the list of datasets. Click on the title of the dataset to view the metadata for that particular dataset (see [Exploring datasets](#exploring_datasets)).
 2. Select 'Records completely within site' in the Spatial relationship option to restrict the list of sites to sites where at least one record of the species occurs wholly within the site.
 3. Provide a year range (start and end year in YYYY format) to restrict the list of sites for species records occurring within the defined year range.
 4. Search for specific site or site type by typing in the name in the search list box.
 5. Click on 'Request Better Access' in the Other Options to go to the access request form (see [Filling in the Request Enhanced Access Form](#filling_enhanced_access))
 6. Click the Download button to download the list sites for the species into a csv file, along with the datasets' metadata.
 7. Click on the name of a site to view the records for the species within that site's site reporting page (see [Exploring datasets](#exploring_datasets)).
7. Click on '**Download Records**' to download the species records using the download wizard form (see [Downloading records](#downloading_records))

## <a id="interactive_map"></a> Using the Interactive Map Tool

The Interactive Map Tool allows the exploration of species records, habitats, datasets and site boundaries.

### To use the Interactive Map Tool

1. Enter the name of the species, habitat, designated list, dataset or site in the text box at the top of the page. Species names can either be the currently used scientific name, a common name, other previously used scientific names, a higher taxon, for example genus, or the species code (first 2 letters of the genus and first 3 letters of the species name).
2. On entering a species the 10KM grid squares and legend will be displayed. Further changes can be made to this map
 1. Click on the spanner icon to edit the colour and opacity of the grid squares, apply a year filter and to turn on or off datasets contributing to the map. Absence records are displayed as cross hatched grid squares.
 2. Change the background map by hovering over and selecting the background from the small map image in the bottom left hand corner.
 3. Add additional species, habitats, designated list, dataset or site boundaries. These will be added as a separate layer on the map with a new entry in the legend.
 4. Zoom in using the control at the top left of the page. Alternatively you can draw a bounding box whilst holding down the shift key, enter a site name or 10KM grid square into the text box to zoom directly to a defined area.
 5. On zooming in on the map the resolution of the grid square will automatically increase to an appropriate resolution level for the map displayed. This can be changed to a fixed resolution display by editing the layer using the spanner tool and changing the grid the species should be mapped at. If the displayed resolution is higher than the actual record resolution or your access to the record then the grid square will not appear on the map. To display the grid square change to a lower fixed resolution using the edit control for the layer.
 6. The order that layers are displayed on the map can be changed by moving layers up or down on the legend. This can be done by dragging and dropping the layer with the topmost layer in the list appearing above the lower listed layers.
 7. The layer can be removed from the map by clicking the bin icon.
3. On entering a site or 10KM grid square the map will automatically zoom into that area. The site boundary can be added by entering the corresponding site layer, for example National Nature Reserves in England. Further changes can be made to the site boundary using the spanner icon next to the site layer in the legend.
4. Habitat layers can be added to the map by entering the name of the layer in the text box. Further changes can be made to the habitat boundary using the spanner icon next to the habitat layer in the legend.
5. Dataset and designated list species richness maps can be produced by entering the dataset or designated list name in the text box. Click on the spanner icon to apply a year filter, turn on or off datasets contributing to the map and to change the resolution of the grid squares.
6. Datasets contributing to the map are listed under each organisation in the legend Datasets tab. Datasets selection may be changed for each layer through their edit control by clicking on the spanner icon.
7. To view records contributing to the map of species, designated list or dataset you will need to be registered and logged in. Click on the Login link on the top right hand corner to log in and then select the Records tab in the legend. Records within a drawn bounding box will appear in the legend under the specific layer. The records are searchable using the search box provided on the Records tab. Click 'Redraw Selection'  to draw a new bounding box for record selection.
8. The web address at the top of the page contains the information required to recreate the map including your added layers and zoomed in area. Use this address for future visits to go directly to your customised map.

## <a id="exploring_sites"></a> Exploring a site or 10KM grid square

The NBN Gateway provides a reporting page for each site provided by [organisations](/Organisations) and each 10KM grid square. On this site report page you can view and download a list of species and records that occur within or overlap that site. Each page provides a link to [request better access](/AccessRequest/Create) to the records for the site. 

### To explore a sites or 10KM grid square

1. Click on 'Browse Sites' and select the appropriate site layer containing your chosen site. Zoom into the site displayed on the map using the control at the top left of the page, by drawing a bounding box whilst holding down the shift key or by double clicking on the site. Click on the name of the site appearing in in the table to go to its site report.
2. Alternatively if you know the name of the site, you can enter the name or 10KM grid square in the 'Search the NBN Gateway' general search box and click on the link for your required site in the search results table.
3. On the site report page you can
 1. View the boundary of the site overlaid on an Ordnance Survey map.
 2. Select or deselect datasets contributing to the report using the datasets tick boxes. Organisations can be sorted alphabetically by selecting 'Organisation Name' in the Sort by box above the list of datasets. Click on the title of the dataset to view the metadata for that particular dataset (see [Exploring datasets](#exploring_datasets)).
 3. Provide a year range (start and end year in YYYY format) restricting the site report to species records occurring within that year range.
 4. Select 'Records completely within site' in the Spatial relationship option to restrict the site report to species records occurring wholly within and not overlapping the site boundary.
 5. Select a designation to restrict the site report to species occurring within chosen designated list.
 6. Click on a Species Group link to restrict the site report to that species group.
 7. Click on 'Request Better Access' in the Other Options to go to the access request form (see [Filling in the Request Enhanced Access Form](#filling_enhanced_access)) for the site.
 8. Click Download button to download either the list of species for that site into a csv file, along with the contributing dataset metadata, or to download the records for the site (see [Downloading records](#downloading_records)). This download may contain contain species or records that do not occur within the site but overlap the site boundary if the spatial relationship 'Records within or overlapping site' option is used.
 9. Click on the species name to display the records for that species. Species may be displayed on the Interactive Map by clicking on the 'View on Interactive Map' link. (see [Using the Interactive Map Tool](#interative_map))

## <a id="exploring_designated_lists"></a>Exploring designated lists

The NBN Gateway incorporates the current designated lists collated by Joint Nature Conservation Committee ([JNCC](http://jncc.defra.gov.uk/page-5546)). The information on these lists along with the species they include can be viewed and mapped on the NBN Gateway. In addition these lists be used within site reports (see [Exploring a site or 10KM grid square](#exploring_sites)), to request enhanced access (see [Filling in the Request Enhanced Access Form](#filling_enhanced_access)) and download records (see [Downloading records](#downloading_records)).

### To explore designated lists

1. Click on 'Browse Designations' and expand the appropriate designation category to select the required designated list. Click on the designated list to go to its information page.
2. Alternatively, enter the name or part of the name of the designated list in the 'Search the NBN Gateway' general search box and click on the link for your required designated list in the search results table.
3. On the designated list information page you can 
 1. View the information for the designated list.
 2. Map the species richness map for the designated list on the Interactive Map Tool (see [Using the Interactive Map Tool](#interactive_map))
 3. View the species groups and species that make up the designated list. Click on the species to see it's species information page. The Designations section in this page lists all the current designations for that species.
 4. Click the download link to download records for species within the designated list (see Downloading records](#downloading_records)).
 5. Click on the Joint Nature Conservation Committee link to see further information on the collation of these designated lists. The JNCC web page also includes a link to download the master Conservation Designations Spreadsheet.


## <a id="exploring_datasets"></a> Exploring datasets

Data on the NBN Gateway is supplied by a number of contributing [organisations](/Organisations) as datasets each with its own metadata. To help evaluate whether data from these datasets is fit for your intended use each organisation has its own page on the NBN Gateway. This page lists the datasets that the organisation has either supplied or contributed to, provides the contact details for the organisation and usually a link to their website for further information. On the metadata page for each dataset you can view that dataset's metadata and statistics, your level of access granted to the dataset and any constraints imposed on the use of the dataset by the organisation above the use conditions within the NBN Gateway [Terms and Conditions](/Terms). Links are also provided to view the species richness map of the dataset using the Interactive Map Tool, request better access to and download records from the dataset.

### To explore datasets

1. Click on 'Browse Datasets' and start typing in the name of the dataset in the search box. The search term can include any word in the name. If you do not know the name of the dataset then try typing in the organisation name to narrow the list of datasets to those provided by that organisation. Each column in the dataset table can be sorted by clicking on the column name.
2. Click on dataset name to go directly to the metadata page for that dataset or the organisation name to go to the organisation's page containing the list all the datasets supplied or contributed by that organisation.
3. On the metadata page you can
 1. Click on the name of the organisations providing or contributing to the dataset to view that organisation's page.
 2. View the dataset's metadata including description, summary of purpose and methods of data capture, geographical and temporal coverage, assessment of data quality and any additional information. Additionally the geographical tab displays a species richness map of the dataset and the temporal tab displays a graph of number of records per year. Click on the Map Link in the general tab to display the species richness map on the Interactive Map Tool.
 3. View the dataset's statistics of number of records and species. Record counts are further broken down by resolution on the geographical tab and by date type and year on the temporal tab. The list of species within the dataset are provided on the species tab, as well as the number of records and link to that species' species information page.
 4. View the dataset's survey metadata and statistics on the surveys tab and name and description of record attributes on the attributes tab.
 5. View your public and granted enhanced access on the access and constraints tab. This tab also includes any access or use constraints imposed on the dataset by the providing organisation. Click on the request better access link to go to the access request form for this dataset (see Filling in the Request Enhanced Access Form](#filling_enhanced_access)).
 6. Once registered and logged in (see [Getting started by creating your own user account](#create_user_account)) you can download datasets at your level of access (see [Downloading records](#downloading_records)) by clicking on the Download link provided on the general tab.


## <a id="downloading_records"></a> Downloading records
 
Once registered you have can download records at your level of access (see [Getting better access to records](#getting_better_access)) using the download wizard. From this wizard you can download records for a species or taxon,  a site, a 10KM grid square or a dataset. Records may be additionally filtered by a year range, geographical area, designated or organisation supplied list of species or a selection of datasets.

The download wizard may be accessed from a number of NBN Gateway pages. 
- For downloading records for a species browse for the required species (see [Exploring species](#exploring_species)) and click on download link in either the species information or grid map page. Species records may also be displayed on the Interactive Map Tool (see [Using the Interactive Map Tool](#interactive_map)).
- For downloading records for a site or 10KM grid square browse for the required site or grid square (see [Exploring a site or 10KM grid square](#exploring_sites)) and click on the download link in the site or grid square report page. Records for a single species with this site or 10KM grid square may also be displayed within this report.
- For downloading records from a dataset browse for the dataset (see [Exploring datasets](#exploring_datasets)) and click on the download link in the dataset's metadata page.

## To use the download wizard
1. Make sure you have registered and logged in to download records (see [Getting better access to records](#getting_better_access)).
2. Click on the download link available within the appropriate NBN Gateway reporting page.
3. Read and accept the download terms and conditions and important note on data access.
4. In the download wizard click on each of the following seven sections to fill or change the required information.

### [REQUIRED] **Download Details**  (Supply the purpose of your download)
1. elect either yourself or an organisation you are downloading records on behalf of.
2. Select one of the predefined download purposes.
3. Add a detailed description of purpose for your download. This information will be accessible by the dataset administrators so that they are aware of who and for what purpose records from their datasets are being downloaded.
4. Tick include record attributes if you want attribute fields to be included in the download. These optional fields are in addition to the sitename, recorder or determiner names fields which are automatically supplied with the records, provided that this information has been supplied and you have a sufficient level of access. The attribute fields are listed for each dataset within the dataset's metadata page (see [Exploring datasets](#exploring_datasets)) and are available to include in the download only if you have been granted full access to the record details. 

### [REQUIRED] **Sensitive Records** (Include or exclude sensitive records)
1. Sensitive records as flagged by the data providers will be included if you have been granted access to them by the data provider for that particular dataset. You can restrict the download to non-sensitive records only by selecting the non-sensitive drop down option.

### [OPTIONAL] **Geographical Area** (Restrict download to an area)
1. If you do not wish to restrict your download to a specific area select the 'All areas' option..
2. Otherwise select either 'Records that are 'within' or 'overlapping and within'' option for a 10KM grid square or site boundary. For a 10KM grid square type in the required grid square. For a site choose one of the supplied site lists and then select the required individual site.

### [OPTIONAL] **Species** (Restrict download to species, designated list or taxon group)
1. If you do not wish to restrict your download to a specific species or group of species select the 'All species' option.
2. Otherwise restrict your download by selecting one of the following four options
 - Single species or taxon. Type in the name and select the required species or taxon. A taxon at a higher taxonomic level (for example genus or family) with include all the child taxa (for example all species within a genus).
 - Select one of the supplied designated lists (see [Exploring designated lists](#exploring_designated_lists)).
 - Select one of the organisation supplied lists. These are supplied list of taxa maintained by an organisation for the purpose of requesting access to and downloading records from the NBN Gateway.
 - Select one of the supplied species groups.

### [OPTIONAL] **Year Range** (Restrict download to range of years)
1. If you do not wish to restrict your download to a specific year range select the 'All years' option.
2. Otherwise select and supply the start and end years within the required year range (YYYY Format).

### [OPTIONAL] **Datasets** (Restrict download to one or more datasets)
1. If you do not wish to restrict your download to a specific dataset or list of datasets select the 'All datasets' option.
2. Otherwise restrict you download to one or more datasets by selecting one of the following two options
 - Select and type in the name of a single dataset.
 - Select one or more datasets from the dataset list.

### [REQUIRED] **Download Selected Records**  (Download the records)
1. You may need to go back through the form and complete any missing information highlighted in red before downloading the records.
2. Click the Download button to start downloading records. The records will be downloaded in a zipped file containing the following 3 files
 1. Records. A csv file containing the records
 2. Metadata. A text file listing the datasets contributing to the download, along with description and use constraints for each dataset. Further information on the dataset can be found on the dataset's metadata page (see [Exploring datasets](#exploring_datasets)).
 3. Read me. Text file summarising your download criteria.
3. The details of your download, along with your username, email address and purpose of download will be available to the data administrators to view within their account page. Dataset administrators may also elect to receive an automated email alerting them to your download.
4. When using downloading records ensure you comply with the NBN Gateway [Terms and Conditions](/Terms) as well as any additional use constraints imposed on the use of individual datasets by the data provider. Guidance on referencing the data can be found on the [NBN website](http://www.nbn.org.uk/Use-Data/Using-Maps-or-Data/Using-and-referencing-data-from-the-Gateway.aspx)


## <a id="feedback"></a> Sending my suggestions and feedback
Further information on the NBN Gateway can be found on the NBN website. From the main menu click on 'The NBN' to go to the [NBN website](http://nbn.org.uk/)
If you have any suggestions or feedback around the NBN Gateway then please use the comments and suggestions section of the [NBN Forum](http://forums.nbn.org.uk/viewforum.php?id=22).

        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
