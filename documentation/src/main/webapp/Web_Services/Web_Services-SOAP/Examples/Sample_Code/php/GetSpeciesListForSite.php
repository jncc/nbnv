<H1>GetSpeciesListForSiteExample</H1>

<?php
/*
 *    NBN Gateway SOAP Client Sample: 
 *    Author: James Perrins, 
 *    exeGesIS SDM
 *    3rd March 2006
 */
 
require_once('lib/nusoap.php');

//pass in details of any proxy server
$proxyhost = isset($_POST['proxyhost']) ? $_POST['proxyhost'] : '';
$proxyport = isset($_POST['proxyport']) ? $_POST['proxyport'] : '';
$proxyusername = isset($_POST['proxyusername']) ? $_POST['proxyusername'] : '';
$proxypassword = isset($_POST['proxypassword']) ? $_POST['proxypassword'] : '';

$client = new soapclient("http://212.219.37.104/NBNWebServices/ws/WSDL", 'true', $proxyhost, $proxyport, $proxyusername, $proxypassword);

// Set timeouts, nusoap default is 30 
$client->timeout = 500; 
$client->response_timeout = 500; 

$err = $client->getError();
if ($err) {
	echo '<h2>Constructor error</h2><pre>' . $err . '</pre>';
}


//define request - siteKey is the providerkey - which currently isn't visible on the gateway I think - so have to ask
//Example below is for Monks wood SSSI
$query1 = '<SpeciesListRequest xmlns="http://webservices.searchnbn.net/query">
		<GeographicalFilter>
			<KnownSite siteKey="1002775" providerKey="GA000339" xmlns="http://webservices.searchnbn.net/data" />
		</GeographicalFilter>
		<TaxonReportingCategoryKey>NHMSYS0000080085</TaxonReportingCategoryKey>
	  </SpeciesListRequest>';

$result = $client->call("GetSpeciesList", $query1);
$debug = $client->getDebug();


print '<h2>Results</h2>';

print $result;

//the xml returned from the gateway appears to be missing the xml header which upsets PHP - so fix it first
$obj = simplexml_load_string(fixupxml($client->response));



print ("<hr><table>");

foreach ($obj->SpeciesList->Species as $Species) {
   echo '<tr><td><i>', $Species->ScientificName, '</i></td><td>', $Species->CommonName, '</td></tr>';
}
print ("</table><hr>");

foreach ($obj->DatasetSummaryList->DatasetSummary->ProviderMetadata as $DataSet) {
   echo '<b>', $DataSet->DatasetTitle,'</b><br>',$DataSet->DatasetProvider, '<br />';
}
print ("<hr>");

//uncomment this to see the xml to see what data is returned
echo '<xmp>'.fixupxml($client->response).'</xmp>';



//print '<h2>Request</h2>';
//echo '<xmp>'.$client->request.'</xmp>';
//print ("<hr>");
print '<h2>Response</h2>';
echo '<xmp>'.$client->response.'</xmp>';
//echo '<hr><h2>Debug</h2><pre>' . $debug . '</pre><hr>';


//this function fixes up the xml from the gateway so that PHP can turn it into an object
  function fixupxml($xml) {
	$pos = strpos($xml, '<SOAP-ENV:Body');   // Find the start of the <SOAP-ENV:Body tag
    $xml = substr($xml,$pos);                // Discard everything before it
    $pos = strpos($xml, '>');                // Find the close of the <SOAP-ENV:Body tag
    $xml = substr($xml,$pos+1);              // Discard the remainder of the SOAP-ENV:Body tag
    $pos = strpos($xml, '</SOAP-ENV:Body>'); // Find the start of the </SOAP-ENV:Body> tag
    $xml = substr($xml,0,$pos);              // Discard the </SOAP-ENV:Body> tag and everything after it
    $xml = '<?xml version="1.0" encoding="utf-8"?>'.$xml; // Add the missing xml header
	return $xml;
  }

?>

