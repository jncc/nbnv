<!--
@author Richard Ostler, CEH Monks Wood
@date   10/07/2006
-->
<html>
<head><title>PHP NBN Known Site Name Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" /></head>
<body>
<div id="container">
<h1>NBN Gateway Known Site Name Web Service, PHP Example</h1>
<p>This is a PHP example of the Known Site Name Web Service used to lookup a site name.</p>
<?php
// Set up the web service info
require_once('../lib/nusoap.php');
$client = new soapclient('http://www.nbnws.net/ws/WSDL', 'wsdl');

if($client->fault){
	echo "FAULT:  <p>Code: {$client->faultcode} <br />";
	echo "String: {$client->faultstring} </p>";
}

$query = '<KnownSiteNameRequest
	xmlns="http://webservices.searchnbn.net/query"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:nbn="http://webservices.searchnbn.net/data">';
$query .= '<nbn:KnownSite siteKey="292" providerKey="GA000374" />';
$query .= '</KnownSiteNameRequest>';

$response = $client->call('GetKnownSiteName', $query);

$KnownSite = $response['KnownSite'];
?>
<div class="bottomLine">
<h3>Site Details:</h3>
<p>site key: <b><?php echo $KnownSite['!siteKey'] ?></b></p>
<p>provider key: <b><?php echo $KnownSite['!providerKey'] ?></b></p>
<p>name: <b><?php echo $KnownSite['KnownSiteName'] ?></b></p>
<p>type: <b><?php echo $KnownSite['KnownSiteType'] ?></b></p>
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