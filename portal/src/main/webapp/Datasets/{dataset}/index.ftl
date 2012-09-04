
<@template.master title="NBN Gateway - Datasets"
    javascripts=["/js/enable-dataset-metadata-tabs.js","/js/jquery.dataTables.min.js","/js/jqplot/jquery.jqplot.min.js","/js/jqplot/excanvas.min.js","/js/jqplot/plugins/jqplot.json2.min.js","/js/jqplot/plugins/jqplot.highlighter.min.js","/js/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js","/js/jqplot/plugins/jqplot.canvasTextRenderer.min.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/js/jqplot/jquery.jqplot.min.css"] >

    <#assign datasetId="${URLParameters.dataset}">
    <#assign dataset=json.readURL("${api}/datasets/${datasetId}")>

        <h1>${dataset.name}</h1>
        <div id="progress" style="display: none">Your gif here</div>

        <div id="nbn-tabs">
            
            <ul>
                <li><a href="#tabs-1">General</a></li>
                <li><a href="#tabs-2">Access and constraints</a></li>
                <li><a href="#tabs-3">Geographical</a></li>
                <#if dataset.typeName = "Taxon">
                    <li><a href="/Datasets/${datasetId}/Records_Per_Year"><span>Temporal</span></a></li>
                    <li><a href="#tabs-11">Surveys</a></li>
                    <li><a href="#tabs-12">Attributes</a></li>
                    <li><a href="/Datasets/${datasetId}/Taxa"><span>Species</span></a></li>
                <#elseif dataset.typeName = "Habitat">
                    <li><a href="#tabs-20">Attributes</a></li>
                <#elseif dataset.typeName = "Site Boundary">
                    <li><a href="/Datasets/${datasetId}/Site_Boundaries"><span>Sites</span></a></li>
                </#if>
            </ul>

            <div id="tabs-1">
                <table class="nbn-dataset-table nbn-simple-table">
                    <tr>
                        <th>Provider</th>
                        <td>[TODO Logo] <a href="/Organisations/${dataset.organisationID}">${dataset.organisationName}</a></td>
                    </tr>
                    <tr>
                        <th>Title</th>
                        <td>${dataset.name}</td>
                    </tr>
                    <tr>
                        <th>Permanent key</th>
                        <td>${dataset.datasetKey}</td>
                    </tr>
                    <tr>
                        <th>Description</th>
                        <td>${dataset.description!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Date uploaded</th>
                        <td>${dataset.formattedDateUploaded}</td>
                    </tr>
                    <tr>
                        <th>Purpose of data capture</th>
                        <td>${dataset.purpose!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Methods of data capture</th>
                        <td>${dataset.captureMethod!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Geographical coverage</th>
                        <td>${dataset.geographicalCoverage!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Temporal coverage</th>
                        <td>${dataset.temporalCoverage!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Data quality</th>
                        <td>${dataset.quality!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Additional information</th>
                        <td>${dataset.additionalInformation!"Not available"}</td>
                    </tr>
                </table>
            </div>

            <div id="tabs-2">
                <table class="nbn-dataset-table nbn-simple-table">
                    <tr>
                        <th>Your access</th>
                        <td></td>
                    </tr>
                    <tr>
                        <th>Access constraints</th>
                        <td>${dataset.accessConstraints!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Use constraints</th>
                        <td>${dataset.useConstraints!"Not available"}</td>
                    </tr>
                </table>
            </div>

            <div id="tabs-3">
                [TODO: Ajax call to map]
                <!--<iframe width="100%" height="600px" src="http://data.nbn.org.uk/imt/?baselayer=Hybrid&bbox=-18.576014044435876,49.03927489540209,9.5489859505262,60.45827601788278&mode=SINGLE_DATASET&dataset=GA001044"></iframe>-->
            </div>

            <#if dataset.typeName = "Taxon">
                <div id="tabs-11">
                    TODO - Surveys
                </div>
                <div id="tabs-12">
                    TODO - Species attributes
                </div>
            <#elseif dataset.typeName = "Habitat">
                <div id="tabs-20">
                    TODO - Habitat attributes
                </div>
            </#if>
        </div>

</@template.master>
