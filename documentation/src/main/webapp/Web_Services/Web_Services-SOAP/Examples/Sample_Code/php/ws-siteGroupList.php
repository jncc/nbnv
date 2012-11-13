<!--
@author Richard Ostler, CEH Monks Wood
@date   10/07/2006
-->
<html>
<head><title>PHP NBN Gateway Taxon Reporting Category List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" /></head>
<body>
<div id="container">
<h1>NBN Gateway Taxon Reporting Category List Web Service, PHP Example</h1>
<p>This is a PHP example of the Taxon Reporting Category List Web Service for a known site, requesting a map image.
The page uses the Known Site List Request web service to build the site drop down list.</p>
<?php
// Declare page variables
$site = '';
$siteKey = '';
$providerKey = '';
$designation = 'NONE';
$start = '';
$end = '';
$overlay = 'overlaps';
$minResolution = '';
$siteName = '';

// Test if a query has been posted
if (array_key_exists('site',$_POST)) {
	$site = $_POST['site'];
	$sitebits = explode('!',$site);
	$siteKey = $sitebits[0];
	$providerKey = $sitebits[1];
	$designation = $_POST['designation'];
	$start = $_POST['start'];
	$end = $_POST['end'];
	$overlay = $_POST['overlay'];
	$minResolution = $_POST['minResolution'];
}

// Define an array of species designations
$groups = array(
		"NONE" => "None",
		"BIODIVERSITY_ACTION_PLAN" => "Biodiversity Action Plan",
		"SPECIES_OF_CONSERVATION_CONCERN" => "Species of Conservation Concern",
		"SNH_SCOTTISH_LIST" => "SNH Scottish List",
        "MARLIN_MARINE_SPECIES" => "Marlin Marine Species");


// Set up the web service info
require_once('../lib/nusoap.php');
$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

if($client->fault){
	echo "FAULT:  <p>Code: {$client->faultcode} <br />";
	echo "String: {$client->faultstring} </p>";
}

// Use the KnownSiteListRequest web service to get a list of RSPB important bird areas
$query1 = '<KnownSiteListRequest xmlns="http://webservices.searchnbn.net/query">';
$query1 .= '<KnownSiteType>RSPB_IMPORTANT_BIRD_AREAS</KnownSiteType>';
$query1 .= '</KnownSiteListRequest>';

// Response is an associative array representing the XML document.
$response1 = $client->call('GetKnownSiteList', $query1);
?>

<fieldset>
    <legend>Create a 10km square species group list</legend>
    <form method="post" action="ws-siteGroupList.php">
    	<p><strong>Select an RSPB Important Bird Area:
    	<select name="site">
    	<?php
    	$KnownSiteList = $response1['KnownSiteList'];
    	$KnownSite = $KnownSiteList['KnownSite'];
    	foreach($KnownSite as $ks) {
			if ($site == $ks['!siteKey'].'!'.$ks['!providerKey']) {
				echo '<option selected="selected" value="'.$ks['!siteKey'].'!'.$ks['!providerKey'].'">'.$ks['KnownSiteName'].'</option>';
				$siteName = $ks['KnownSiteName'];
			} else {
				echo '<option value="'.$ks['!siteKey'].'!'.$ks['!providerKey'].'">'.$ks['KnownSiteName'].'</option>';
			}
		}
		?>
		</select>
    	</p>
    	<p><strong>Enter start year <input type="text" name="start" size="4" /> and end year <input type="text" name="end" size="4"  /></strong></p>
		<p><strong>Select a species list designation: </strong>
		<select name="designation">
			<?php
			foreach($groups as $key => $value) {
				if ($key == $designation) {
					echo '<option selected="selected" value="'.$key.'">'.$value.'</option>';
				} else {
					echo '<option value="'.$key.'">'.$value.'</option>';
				}
			}
        	?>
		</select></p>
		<p><strong>Select a minimum query resolution: </strong>
		<select name="minResolution">
			<option value="_100m">100m</option>
			<option value="_1km">1km</option>
			<option value="_2km">2km</option>
			<option value="_10km">10km</option>
		</select></p>
		<p><strong>Select an overlay rule</strong>
		<select name="overlay">
			<option value="overlaps">overlaps</option>
			<option value="within">within</option>
		</select></p>
        <p><input type="submit" value="view list" /></p>
    </form>
</fieldset>
<?php
if (array_key_exists('site',$_POST)) {

	$query2 = '<TaxonReportingCategoryListRequest xmlns="http://webservices.searchnbn.net/query" xmlns:nbn="http://webservices.searchnbn.net/data"';
	$query2 .= ' designation="';
	$query2 .= $designation;
	$query2 .= '" >';
	$query2 .= '<GeographicalFilter><nbn:KnownSite siteKey="'.$siteKey.'" providerKey="'.$providerKey.'" />';
	$query2 .= '<MapSettings fillColour="#FF0000" fillTransparency="0.5" height="300" outlineColour="#000000" outlineWidth="1" width="300"/>';
	$query2 .= '</GeographicalFilter>';
	$query2 .= '<MinimumResolution>'.$minResolution.'</MinimumResolution>';
	$query2 .= '<OverlayRule>'.$overlay.'</OverlayRule>';
	if (strlen($start) > 0 and strlen($end) > 0) {
		$query2 .= '<DateRange><Start>'.$start.'</Start><End>'.$end.'</End></DateRange>';
	}
	$query2 .= '</TaxonReportingCategoryListRequest>';

	// Response is an associative array representing the XML document.
	$response2 = $client->call('GetTaxonReportingCategoryList', $query2);

	$TaxonReportingCategoryList = $response2['TaxonReportingCategoryList'];
?>
	<div id="mapWrapper">
		<div id="mapImage">
		<?php
		$Map = $response2['Map'];
		echo '<h3>Map for '.$siteName.'</h3>';
	    echo '<img src="'.$Map['Url'].'" alt="Map for '.$siteName.'" title="Map for '.$siteName.'"/>';
        ?>
        </div>
		<?php
		// If there are results
		if (sizeof($TaxonReportingCategoryList) > 0) {
			$TaxonReportingCategory = $TaxonReportingCategoryList['TaxonReportingCategory'];
			?>
			<div id="rightOfMap">
			<h3><?php echo sizeof($TaxonReportingCategory); ?> species groups recorded</h3>
			<p>Click on a taxon group name to view the species recorded.</p>
            <table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
            <?php
            // Loop through the array and print the names
			foreach ($TaxonReportingCategory as $trc) {
				echo '<tr><td><a href="ws-siteSpeciesList.php?siteKey='.$siteKey.'&providerKey='.$providerKey.'&taxCat='.$trc['!taxonReportingCategoryKey'].'">'.$trc['!'].'</a></td></tr>';
			} ?>
			</table>
			</div>
		<?php } ?>
	</div>
	<div class="bottomLine">
	<h2>Datasets used</h2>
	<table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
		<tr>
			<th>Dataset</th>
			<th>Provider</th>
		</tr>
		<?php
		$DatasetSummaryList = $response2['DatasetSummaryList'];
		// php foreach does not seem to handle arrays with just one element so... test size.
		if (sizeof($DatasetSummaryList['DatasetSummary']) > 1) {
			foreach ($DatasetSummaryList['DatasetSummary'] as $DatasetSummary) {
				$ProviderMetadata = $DatasetSummary['ProviderMetadata'];
				echo '<tr><td bgcolor="#FFFFFF"><a href="ws-dataset.php?dsKey='.$DatasetSummary['!id'].'">'.$ProviderMetadata['DatasetTitle'].'</a></td>';
				echo '<td bgcolor="#FFFFFF">'.$ProviderMetadata['DatasetProvider'].'</td></tr>';
			}
		} else {
			$DatasetSummary = $DatasetSummaryList['DatasetSummary'];
			$ProviderMetadata = $DatasetSummary['ProviderMetadata'];
			echo '<tr><td bgcolor="#FFFFFF"><a href="ws-dataset.php?dsKey='.$DatasetSummary['!id'].'">'.$ProviderMetadata['DatasetTitle'].'</a></td>';
			echo '<td bgcolor="#FFFFFF">'.$ProviderMetadata['DatasetProvider'].'</td></tr>';
		} ?>
	</table>
	</div>
	<!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
	<div id="footer">
	<div id="tandc"><a href="<?php echo $response1['!termsAndConditions']; ?>" class="popup">Gateway terms and conditions</a></div>
	<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank">
		<img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website"
		border="0" src="<?php echo $response1['!NBNLogo']; ?>" /></a></div>
	</div>
	<?php } ?>
</div>
</body>
</html>