<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Downloading records
 
Once registered you have can download records at your level of access (see [Getting better access to records](../Getting_better_access_to_records)) using the download wizard. From this wizard you can download records for a species or taxon,  a site, a 10KM grid square or a dataset. Records may be additionally filtered by a year range, geographical area, designated or organisation supplied list of species or a selection of datasets.

The download wizard may be accessed from a number of NBN Gateway pages. 
- For downloading records for a species browse for the required species (see [Exploring species](../Exploring_species)) and click on download link in either the species information or grid map page. Species records may also be displayed on the Interactive Map Tool (see [Using the Interactive Map Tool](../Using_the_Interactive_Map_Tool)).
- For downloading records for a site or 10KM grid square browse for the required site or grid square (see [Exploring a site or 10KM grid square](../Exploring_a_site_or_10KM_grid_square)) and click on the download link in the site or grid square report page. Records for a single species with this site or 10KM grid square may also be displayed within this report.
- For downloading records from a dataset browse for the dataset (see [Exploring datasets](../Exploring_datasets)) and click on the download link in the dataset's metadata page.

## To use the download wizard
1. Make sure you have registered and logged in to download records (see [Getting better access to records](../Getting_better_access_to_records)).
2. Click on the download link available within the appropriate NBN Gateway reporting page.
3. Read and accept the download terms and conditions and important note on data access.
4. In the download wizard click on each of the following seven sections to fill or change the required information.

### [REQUIRED] **Download Details**  (Supply the purpose of your download)
1. elect either yourself or an organisation you are downloading records on behalf of.
2. Select one of the predefined download purposes.
3. Add a detailed description of purpose for your download. This information will be accessible by the dataset administrators so that they are aware of who and for what purpose records from their datasets are being downloaded.
4. Tick include record attributes if you want attribute fields to be included in the download. These optional fields are in addition to the sitename, recorder or determiner names fields which are automatically supplied with the records, provided that this information has been supplied and you have a sufficient level of access. The attribute fields are listed for each dataset within the dataset's metadata page (see [Exploring datasets](../Exploring_datasets)) and are available to include in the download only if you have been granted full access to the record details. 

### [REQUIRED] **Sensitive Records** (Include or exclude sensitive records)
1. Sensitive records as flagged by the data providers will be included if you have been granted access to them by the data provider for that particular dataset. You can restrict the download to non-sensitive records only by selecting the non-sensitive drop down option.

### [OPTIONAL] **Geographical Area** (Restrict download to an area)
1. If you do not wish to restrict your download to a specific area select the 'All areas' option..
2. Otherwise select either 'Records that are 'within' or 'overlapping and within'' option for a 10KM grid square or site boundary. For a 10KM grid square type in the required grid square. For a site choose one of the supplied site lists and then select the required individual site.

### [OPTIONAL] **Species** (Restrict download to species, designated list or taxon group)
1. If you do not wish to restrict your download to a specific species or group of species select the 'All species' option.
2. Otherwise restrict your download by selecting one of the following four options
 - Single species or taxon. Type in the name and select the required species or taxon. A taxon at a higher taxonomic level (for example genus or family) with include all the child taxa (for example all species within a genus).
 - Select one of the supplied designated lists (see [Exploring designated lists](../Exploring_designated_lists)).
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
 2. Metadata. A text file listing the datasets contributing to the download, along with description and use constraints for each dataset. Further information on the dataset can be found on the dataset's metadata page (see [Exploring datasets](../Exploring_datasets)).
 3. Read me. Text file summarising your download criteria.
3. The details of your download, along with your username, email address and purpose of download will be available to the data administrators to view within their account page. Dataset administrators may also elect to receive an automated email alerting them to your download.
4. When using downloading records ensure you comply with the NBN Gateway [Terms and Conditions](/Terms) as well as any additional use constraints imposed on the use of individual datasets by the data provider. Guidance on referencing the data can be found on the [NBN website](http://www.nbn.org.uk/Use-Data/Using-Maps-or-Data/Using-and-referencing-data-from-the-Gateway.aspx)

        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>