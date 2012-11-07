<!--
@author Richard Ostler, CEH Monks Wood
@date   10/07/2006
-->
<html>
<head><title>PHP NBN Gateway Dataset Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" /></head>
<body>
<div id="container">
<h1>Dataset Metadata Web Service, java example</h1>
<p>This is an example of the dataset metadata web service. This service returns metadata for one or more datasets.
 This example fetches metadata for the dataset identified by a dataset key request parameter.`</p>
<?php
// Declare page variables
$datasetKey = "";

// Test if a query has been posted
if (array_key_exists('dsKey',$_GET)) {
	$datasetKey = $_GET['dsKey'];

	require_once('../lib/nusoap.php');

	$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

	if($client->fault){
		echo "FAULT:  <p>Code: {$client->faultcode} <br />";
		echo "String: {$client->faultstring} </p>";
	}

	$query1 = '<DatasetListRequest xmlns="http://webservices.searchnbn.net/query">';
	$query1 .= '<DatasetList><DatasetKey>' . $datasetKey . '</DatasetKey></DatasetList>';
	$query1 .= '</DatasetListRequest>';

	// Response is an associative array representing the XML document.
	$response = $client->call('GetDatasetList', $query1);

	$DatasetSummary = $response['DatasetSummary'];

	//print '<pre>'.htmlspecialchars($client->response).'</pre>';;

	$ProviderMetadata = $DatasetSummary['ProviderMetadata'];
	$Abstract = $ProviderMetadata['Abstract'];
	?>
	<div class="bottomLine">
	<table cellspacing="1" bgcolor="#CCCCCC" cellpadding="2" width="100%">
		<tr>
			<th>Field</th>
			<th>Value</th>
		</tr><tr>
			<td><b>Title</b></td>
			<?php echo '<td>'.$ProviderMetadata['DatasetTitle'].'</td>'; ?>
		</tr><tr>
			<td><b>Provider</b></td>
			<?php echo '<td>'.$ProviderMetadata['DatasetProvider'].'</td>'; ?>
		</tr><tr>
			<td><b>Description</b></td>
			<?php echo '<td>'.$Abstract['Description'].'</td>'; ?>
		</tr><tr>
			<td><b>Data Capture Method</b></td>
			<?php echo '<td>'.$Abstract['DataCaptureMethod'].'</td>'; ?>
		</tr><tr>
			<td><b>Dataset Purpose</b></td>
			<?php echo '<td>'.$Abstract['DatasetPurpose'].'</td>'; ?>
		</tr><tr>
			<td><b>Geographical Coverage</b></td>
			<?php echo '<td>'.$Abstract['GeographicalCoverage'].'</td>'; ?>
		</tr><tr>
			<td><b>Temporal Coverage</b></td>
			<?php echo '<td>'.$Abstract['TemporalCoverage'].'</td>'; ?>
		</tr><tr>
			<td><b>Data Quality</b></td>
			<?php echo '<td>'.$Abstract['DataQuality'].'</td>'; ?>
		</tr><tr>
			<td><b>Additional Information</b></td>
			<?php echo '<td>'.$Abstract['AdditionalInformation'].'</td>'; ?>
		</tr><tr>
			<td><b>Access Constraints</b></td>
			<?php echo '<td>'.$ProviderMetadata['AccessConstraints'].'</td>'; ?>
		</tr><tr>
			<td><b>Use Constraints</b></td>
			<?php echo '<td>'.$ProviderMetadata['UseConstraints'].'</td>'; ?>
		</tr>
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