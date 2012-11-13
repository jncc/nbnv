<!--
@author Richard Ostler, CEH Monks Wood
@date   10/07/2006
-->
<html>
<head><title>PHP NBN Gateway Species List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" /></head>
<body>
<div id="container">
<h1>NBN Gateway Species List Web Service, PHP Example</h1>
<p>This is a PHP example of the Species List Web Service for a known site, requesting a map image. Uses the Known Site Name service
to lookup the site name.</p>
<?php
// Declare page variables
$designation = isset($_REQUEST['designation']) ? $_REQUEST['designation'] : 'NONE';
$start = isset($_REQUEST['start']) ? $_REQUEST['start'] : '';
$end = isset($_REQUEST['end']) ? $_REQUEST['end'] : '';
$taxCat = isset($_REQUEST['taxCat']) ? $_REQUEST['taxCat'] : '';
$nameOrder = isset($_REQUEST['Common']) ? $_REQUEST['Common'] : '';
$siteKey = $_REQUEST['siteKey'];
$providerKey = $_REQUEST['providerKey'];
$minResolution = isset($_REQUEST['minResolution']) ? $_REQUEST['minResolution'] : '_100m';
$overlay = isset($_REQUEST['overlay']) ? $_REQUEST['overlay'] : 'Overlaps';

// Set up the web service info
require_once('../lib/nusoap.php');
$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

if($client->fault){
	echo "FAULT:  <p>Code: {$client->faultcode} <br />";
	echo "String: {$client->faultstring} </p>";
}

$query0 = '<KnownSiteNameRequest
	xmlns="http://webservices.searchnbn.net/query"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:nbn="http://webservices.searchnbn.net/data">';
$query0 .= '<nbn:KnownSite siteKey="'.$siteKey.'" providerKey="'.$providerKey.'" />';
$query0 .= '</KnownSiteNameRequest>';

$response0 = $client->call('GetKnownSiteName', $query0);

$KnownSite = $response0['KnownSite'];
$SiteName = $KnownSite['KnownSiteName'];

$query1 = '<SpeciesListRequest
	xmlns="http://webservices.searchnbn.net/query"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:nbn="http://webservices.searchnbn.net/data"
	sort="';
$query1 .= $nameOrder;
$query1 .= '" designation="';
$query1 .= $designation;
$query1 .= '" >';
$query1 .= '<GeographicalFilter>';
$query1 .= '<nbn:KnownSite providerKey="'.$providerKey.'" siteKey="'.$siteKey.'" />';
$query1 .= '<MapSettings fillColour="#FF0000" height="300" width="300"/>';
$query1 .= '</GeographicalFilter>';
$query1 .= '<MinimumResolution>'.$minResolution.'</MinimumResolution>';
$query1 .= '<OverlayRule>'.$overlay.'</OverlayRule>';
$query1 .= '</SpeciesListRequest>';

// Response is an associative array representing the XML document.
$response = $client->call('GetSpeciesList', $query1);

$Map = $response['Map'];
echo '<div class="bottomLine">';
echo '<h3>Map for '.$SiteName.'</h3>';
echo '<img src="'.$Map['Url'].'" alt="Map for '.$SiteName.'" title="Map for '.$SiteName.'"/>';
echo '</div>';

$SpeciesList = $response['SpeciesList'];

echo '<div class="bottomLine"><h3>Species recorded in '.$SiteName.'</h3>';?>
<p>Click on a species name to view its data.</p>
<table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
<?php
foreach ($SpeciesList['Species'] as $Species) {
	echo '<tr><td><a href="siteSpeciesData.php?siteKey='.$KnownSite['!siteKey'].'&providerKey='.$KnownSite['!providerKey'].'&tvk='.$Species['!taxonVersionKey'].'"><i>'.$Species['ScientificName'].'</i></a></td><td>'.$Species['CommonName'].'</td></tr>';
}
?>
</table>
</div>
<div class="bottomLine">
<h2>Datasets used</h2>
<table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
	<tr>
		<th>Dataset</th>
		<th>Provider</th>
	</tr>
	<?php
	$DatasetSummaryList = $response['DatasetSummaryList'];
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
<div id="tandc"><a href="<?php echo $response0['!termsAndConditions']; ?>" class="popup">Gateway terms and conditions</a></div>
<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank">
	<img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website"
	border="0" src="<?php echo $response0['!NBNLogo']; ?>" /></a></div>
</div>
</div>
</body>
</html>