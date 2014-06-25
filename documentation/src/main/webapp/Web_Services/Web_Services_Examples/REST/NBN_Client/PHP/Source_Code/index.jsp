<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBN Client Source Code PHP</h1>
        <script type="syntaxhighlighter" class="brush: php; html-script: false"><![CDATA[
        <?php
        class NBNClient
        {
            private $token;

            function Login($username, $password)
            {
                $url = "https://data.nbn.org.uk/api/user/login?username=".$username."&password=".$password."&remember=true";

                $ch = curl_init();

                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                $json = curl_exec($ch);
                if($json === FALSE)
                {
                    echo "<p>Error: ".curl_error($ch)."</p>";
                }
                curl_close($ch);

                $response = json_decode($json);
                $this->token = rtrim($response->token, "=");
            }

            function CurlGetString($url)
            {
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                $output = curl_exec($ch);
                curl_close($ch);

                return $output;
            }

            function CurlPostData($url, $postData)
            {
                $fields_string = "";
                foreach($postData as $key=>$value)
                {
                    $fields_string .= $key . "=" . $value . "&";
                }
                $fields_string = rtrim($fields_string, "&");

                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                curl_setopt($ch, CURLOPT_COOKIE, "nbn.token_key=".$this->token);

                curl_setopt($ch, CURLOPT_POST, count($postData));
                curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);

                curl_setopt($ch, CURLOPT_TIMEOUT, 120);
                $output = curl_exec($ch);
                curl_close($ch);

                return $output;
            }

            function GetViceCounties()
            {
                $url = "https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries";
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTaxonOutputGroups()
            {
                $url = "https://data.nbn.org.uk/api/taxonOutputGroups";
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetDesignations()
            {
                $url = "https://data.nbn.org.uk/api/designations";
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetSiteSpecies($viceCounty, $taxonOutputGroup, $designation)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations/species?featureID=" . $viceCounty . "&taxonOutputGroup=" . $taxonOutputGroup;
                if(!(strlen($designation) == 0))
                {
                    $url = $url . "&designation=" . $designation;
                }
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetSiteDatasets($viceCounty, $taxonOutputGroup, $designation)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations/datasets?featureID=" . $viceCounty . "&taxonOutputGroup=" . $taxonOutputGroup;
                if(!(strlen($designation) == 0))
                {
                    $url = $url . "&designation=" . $designation;
                }
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTenKmSpecies($gridReference)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations/species?gridRef=" . $gridReference;
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTenKmDatasets($gridReference)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations/datasets?gridRef=" . $gridReference;
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTaxonSearchResult($query)
            {
                $url = "https://data.nbn.org.uk/api/taxa?q=" . $query;
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTaxonomy($taxonVersionKey)
            {
                $url = "https://data.nbn.org.uk/api/taxa/" . $taxonVersionKey;
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTaxonParent($taxonVersionKey)
            {
                $url = "https://data.nbn.org.uk/api/taxa/" . $taxonVersionKey . "/parent";
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function GetTaxonParents($taxonVersionKey)
            {
                $parents = array();
                do
                {
                    $parent = $this->GetTaxonParent($taxonVersionKey);
                    if($parent != NULL)
                    {
                        $taxonVersionKey = $parent->taxonVersionKey;
                        $parents[] = $parent;
                    }
                }
                while($parent != NULL);
                $parents = array_reverse($parents);
                return $parents;
            }

            function GetTaxonChildren($taxonVersionKey)
            {
                $url = "https://data.nbn.org.uk/api/taxa/" . $taxonVersionKey . "/children";
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function PostRecordsForDatasetAndGridRef($datasetKey, $gridReference)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations";
                $postData = array();
                $postData["datasetKey"] = $datasetKey;
                $postData["gridRef"] = $gridReference;
                $json = $this->CurlPostString($url, $postData);
                return json_decode($json);
            }

            function GetRecordsForWKTDesignationDates($wkt, $designation, $startYear, $endYear)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations?polygon=" . $wkt . "&designation=" . $designation . "&startYear=" . $startYear . "&endYear=" . $endYear;
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }

            function PostRecordsForWKTDesignationDates($wkt, $designation, $startyear, $endyear)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations";
                $postData = array();
                $postData["polygon"] = $wkt;
                $postData["designation"] = $designation;
                $postData["startYear"] = $startyear;
                $postData["endYear"] = $endyear;
                $json = $this->CurlPostData($url, $postData);
                return json_decode($json);
            }

            function PostRecordsForWKTDesignationDatasetDates($wkt, $designation, $datasetkey, $startyear, $endyear)
            {
                $url = "https://data.nbn.org.uk/api/taxonObservations";
                $postData = array();
                $postData["polygon"] = $wkt;
                $postData["designation"] = $designation;
                $postData["datasetKey"] = $datasetkey;
                $postData["startYear"] = $startyear;
                $postData["endYear"] = $endyear;
                $json = $this->CurlPostData($url, $postData);
                return json_decode($json);
            }
        }
        ?>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
