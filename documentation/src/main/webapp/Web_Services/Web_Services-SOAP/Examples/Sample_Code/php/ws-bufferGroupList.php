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
<p>This is a PHP example of the Taxon Reporting Category List Web Service for a point and buffer, requesting a map image.</p>
<?php
// Declare page variables
$x = '';
$y = '';
$distance = '';
$designation = 'NONE';
$start = '';
$end = '';
$minResolution = '';

// Test if a query has been posted
if (array_key_exists('x',$_POST)) {
	$x = $_POST['x'];
	$y = $_POST['y'];
	$distance = $_POST['distance'];
	$designation = $_POST['designation'];
	$start = $_POST['start'];
	$end = $_POST['end'];
	$minResolution = $_POST['minResolution'];
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
    <form method="post" action="ws-bufferGroupList.php">
    	<p>Enter point/buffer values:</p>
		<p>x (e.g. '519700.0'): <input type="text" name="x" size="8" />&nbsp;y (e.g. '279800.0'): <input type="text" name="y" size="8" />&nbsp;distance (m): <input type="text" name="distance" size="6" />
		<p>Enter start year <input type="text" name="start" size="4" /> and end year <input type="text" name="end" size="4"  /></p>
		<p>Select a species list designation:
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
		<p>Select a minimum query resolution:
		<select name="minResolution">
			<option value="_100m">100m</option>
			<option value="_1km">1km</option>
			<option value="_2km">2km</option>
			<option value="_10km">10km</option>
		</select></p>
        <p><input type="submit" value="view list" /></p>
    </form>
</fieldset>
<?php
if (array_key_exists('x',$_POST)) {
	require_once('../lib/nusoap.php');

	$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

	if($client->fault){
		echo "FAULT:  <p>Code: {$client->faultcode} <br />";
		echo "String: {$client->faultstring} </p>";
	}

	$query1 = '<TaxonReportingCategoryListRequest xmlns="http://webservices.searchnbn.net/query" xmlns:nbn="http://webservices.searchnbn.net/data"';
	$query1 .= ' designation="';
	$query1 .= $designation;
	$query1 .= '" >';
	$query1 .= '<GeographicalFilter><nbn:Buffer><nbn:Point srs="EPSG_27700" x="'.$x.'" y="'.$y.'"/><nbn:Distance>'.$distance.'</nbn:Distance></nbn:Buffer>';
	$query1 .= '<MapSettings fillColour="#FF0000" fillTransparency="0.5" height="300" outlineColour="#000000" outlineWidth="1" width="300"/>';
	$query1 .= '</GeographicalFilter>';
	$query1 .= '<MinimumResolution>'.$minResolution.'</MinimumResolution>';
	if (strlen($start) > 0 and strlen($end) > 0) {
		$query1 .= '<DateRange><Start>'.$start.'</Start><End>'.$end.'</End></DateRange>';
	}
	$query1 .= '</TaxonReportingCategoryListRequest>';

	// Response is an associative array representing the XML document.
	$response = $client->call('GetTaxonReportingCategoryList', $query1);

	$TaxonReportingCategoryList = $response['TaxonReportingCategoryList'];
?>
	<div id="mapWrapper">
		<div id="mapImage">
		<?php
		$Map = $response['Map'];
		echo '<h3>Map</h3>';
	    echo '<img src="'.$Map['Url'].'" alt="Map" title="Map"/>';
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
				echo '<tr><td><a href="ws-bufferSpeciesList.php?x='.$x.'&y='.$y.'&distance='.$distance.'&taxCat='.$trc['!taxonReportingCategoryKey'].'">'.$trc['!'].'</a></td></tr>';
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