<!--
@author Richard Ostler, CEH Monks Wood
@date   10/07/2006
-->
<html>
<head><title>PHP NBN Known Site List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" /></head>
<body>
<div id="container">
<h1>NBN Gateway Known Site List Web Service, PHP Example</h1>
<p>This is a PHP example of the Known Site List Web Service used to lookup a site name.</p>
<?php
// Set up the web service info
require_once('../lib/nusoap.php');
$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

if($client->fault){
	echo "FAULT:  <p>Code: {$client->faultcode} <br />";
	echo "String: {$client->faultstring} </p>";
}

$query = '<KnownSiteListRequest
	xmlns="http://webservices.searchnbn.net/query"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:nbn="http://webservices.searchnbn.net/data">';
$query .= '<KnownSiteType>RAMSAR</KnownSiteType>';
$query .= '</KnownSiteListRequest>';

$response = $client->call('GetKnownSiteList', $query);
$KnownSiteList = $response['KnownSiteList'];
?>
<div class="bottomLine">
<h3>RAMSAR Sites on the NBN Gateway:</h3>
<table cellpadding="3" cellspacing="1" bgcolor="#CCCCCC">
	<tr>
		<th>Site Key</th>
		<th>Site Name</th>
	</tr>
	<?php
	foreach ($KnownSiteList['KnownSite'] as $KnownSite) {
		echo '<tr><td>'.$KnownSite['!siteKey'].'</td>';
		echo '<td>'.$KnownSite['KnownSiteName'].'</td></tr>';
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
</div>
</body>
</html>