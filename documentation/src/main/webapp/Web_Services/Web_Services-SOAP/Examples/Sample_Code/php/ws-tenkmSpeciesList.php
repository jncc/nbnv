<!--
@author Richard Ostler, CEH Monks Wood
@date   10/07/2006
-->
<html>
<head><title>PHP NBN Gateway Taxon Reporting Category List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" /></head>
<body>
<div id="container">
<h1>NBN Gateway Species List Web Service, PHP Example</h1>
<p>This is a PHP example of the Species List Web Service for a 10km, requesting a map image.</p>
<?php
// Declare page variables
$square = "";
$designation = "NONE";
$start = "";
$end = "";
$taxCat = "";
$nameOrder = "";

// Test if a query has been posted
if (array_key_exists('square',$_REQUEST)) {
	$square = $_REQUEST['square'];
	$designation = isset($_REQUEST['designation']) ? $_REQUEST['designation'] : 'NONE';
	$start = isset($_REQUEST['start']) ? $_REQUEST['start'] : '';
	$end = isset($_REQUEST['end']) ? $_REQUEST['end'] : '';
	$taxCat = $_REQUEST['taxCat'];
	$nameOrder = isset($_REQUEST['Common']) ? $_REQUEST['Common'] : '';
}

// Define an array of species designations
$groups = array(
		"NONE" => "None",
		"BIODIVERSITY_ACTION_PLAN" => "Biodiversity Action Plan",
		"SPECIES_OF_CONSERVATION_CONCERN" => "Species of Conservation Concern",
		"SNH_SCOTTISH_LIST" => "SNH Scottish List",
        "MARLIN_MARINE_SPECIES" => "Marlin Marine Species");
?>
<fieldset>
    <legend>Create a 10km square species group list</legend>
    <form method="post" action="ws-tenkmSpeciesList.php">
    	<p><strong>Enter a 10km grid square (e.g. TL18): <input type="text" size="4" name="square" /></strong></p>
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
		<p><strong>Species name order</strong>
		<select name="nameOrder">
			<option value="Common">Common</option>
			<option value="Scientific">Scientific</option>
		</select></p>
        <p><input type="submit" value="view list" /></p>
    </form>
</fieldset>

<?php

if (array_key_exists('square',$_REQUEST)) {
	require_once('../lib/nusoap.php');
	$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

	if($client->fault){
		echo "FAULT:  <p>Code: {$client->faultcode} <br />";
		echo "String: {$client->faultstring} </p>";
	}

	echo '<h2>species list</h2>';

	$query1 = '<SpeciesListRequest xmlns="http://webservices.searchnbn.net/query" xmlns:nbn="http://webservices.searchnbn.net/data" sort="';
	$query1 .= $nameOrder;
	$query1 .= '" designation="';
	$query1 .= $designation;
	$query1 .= '" ><GeographicalFilter><nbn:GridSquare key="'.$square.'" maxx="0.0" maxy="0.0" minx="0.0" miny="0.0" />';
	$query1 .= '<MapSettings fillColour="#FF0000" fillTransparency="0.5" height="300" outlineColour="#000000" outlineWidth="1" width="300"/>';
	$query1 .= '</GeographicalFilter>';
	if (strlen($start) > 0 and strlen($end) > 0) {
		$query1 .= '<DateRange><Start>'.$start.'</Start><End>'.$end.'</End></DateRange>';
	}
	$query1 .= '</SpeciesListRequest>';

	// Response is an associative array representing the XML document.
	$response = $client->call('GetSpeciesList', $query1);

	$SpeciesList = $response['SpeciesList'];

	?>
	<div id="mapWrapper">
		<div id="mapImage">
		<?php
		$Map = $response['Map'];
		echo '<h3>Map for '.$square.'</h3>';
		echo '<img src="'.$Map['Url'].'" alt="Map for '.$square.'" title="Map for '.$square.'"/>';
		?>
		</div>
		<?php
		// If there are results
		if (sizeof($SpeciesList) > 0) {
			$Species = $SpeciesList['Species'];
			?>
			<div id="rightOfMap">
			<h3><?php echo sizeof($Species); ?> species recorded</h3>
			<p>Click on a species name to view its data.</p>
			<table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
			<?php
			// Loop through the array and print the names
			foreach ($SpeciesList['Species'] as $Species) {
				echo '<tr><td><a href="somePage.php?tvk='.$Species['!taxonVersionKey'].'"><i>'.$Species['ScientificName'].'</i></a></td><td>'.$Species['CommonName'].'</td></tr>';
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
	<div id="tandc"><a href="<?php echo $response['!termsAndConditions']; ?>" class="popup">Gateway terms and conditions</a></div>
	<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank">
		<img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website"
		border="0" src="<?php echo $response['!NBNLogo']; ?>" /></a></div>
	</div>
	<?php } ?>
</div>
</body>
</html>


