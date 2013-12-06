<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}")>
<#assign taxonDataset=json.readURL("${api}/taxonDatasets/${URLParameters.dataset}")>
<#assign resolution=json.readURL("${api}/datasets/${URLParameters.dataset}/resolutionData")>
<#assign provider=json.readURL("${api}/organisations/${dataset.organisationID}")>
<#assign isAdmin=json.readURL("${api}/datasets/${URLParameters.dataset}/isAdmin")>
<#assign user=json.readURL("${api}/user")>

<@template.master title="NBN Gateway - Datasets"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/"
        ,"/js/enable-dataset-metadata-tabs.js","/js/jquery.dataTables.min.js",
        "/js/jqplot/jquery.jqplot.min.js","/js/jqplot/excanvas.min.js",
        "/js/jqplot/plugins/jqplot.json2.min.js",
        "/js/jqplot/plugins/jqplot.highlighter.min.js",
        "/js/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js",
        "/js/jqplot/plugins/jqplot.canvasTextRenderer.min.js",
        "/js/jqplot/plugins/jqplot.cursor.min.js",
        "/js/dialog_spinner.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/jquery.jqplot.min.css","/css/dataset-metadata.css","/css/dialog-spinner.css","/css/org-admin.css"] >
        <h1>${dataset.title}</h1>
        <div id="nbn-tabs">
            <ul>
                <li><a href="#tabs-1">General</a></li>
                <li><a href="#tabs-2">Access and constraints</a></li>
                <#if dataset.typeName = "Taxon">
                    <li><a href="#tabs-3">Geographical</a></li>
                    <li><a href="/Datasets/${dataset.key}/Records_Per_Year"><span>Temporal</span></a></li>
                    <li><a href="/Datasets/${dataset.key}/Surveys"><span>Surveys</span></a></li>
                    <li><a href="/Datasets/${dataset.key}/Attributes"><span>Attributes</span></a></li>
                    <li><a href="/Datasets/${dataset.key}/Taxa"><span>Species</span></a></li>
                <#elseif dataset.typeName = "Site Boundary">
                    <li><a href="/Datasets/${dataset.key}/Site_Boundaries"><span>Sites</span></a></li>
                </#if>
            </ul>
            <div id="tabs-1">
                <table class="nbn-dataset-table nbn-simple-table nbn-metadata-dataset-table">
                    <tr>
                        <th>Provider</th>
                        <td>
                            <#if provider.hasLogo>
                                <img id="nbn-provider-logo" src="${api}/organisations/${dataset.organisation.id}/logo" class="nbn-provider-table-logo">
                            </#if>
                            <a href="/Organisations/${dataset.organisationID}">${dataset.organisationName}</a>
                        </td>
                    </tr>
                    <#if dataset.contributingOrganisations?has_content>
                        <tr>
                            <th>Contributing Organisations</th>
                            <td>
                                <#list dataset.contributingOrganisations as corg>
                                    <a href="/Organisations/${corg.id}">${corg.name}</a>
                                </#list>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <th>Title</th>
                        <td>${dataset.title}</td>
                    </tr>
                    <tr>
                        <th>Permanent key</th>
                        <td>${dataset.key}</td>
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
                        <th>View in interactive map</th>
                        <td><a href="<@hrefForDatasetOnIMT dataset/>">Map link</a></td>
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
                    <#if dataset.typeName = "Taxon">
                        <#assign taxonDataset=json.readURL("${api}/taxonDatasets/${dataset.key}")>
                        <tr>
                            <th>Number of records</th>
                            <td>${taxonDataset.recordCount}</td>
                        </tr>
                        <tr>
                            <th>Number of species</th>
                            <td>${taxonDataset.speciesCount}</td>
                        </tr>
                    </#if>
                    <#if isAdmin>
                        <tr>
                            <td></td>
                            <td><a style="float:right;" href="/Datasets/${URLParameters.dataset}/Edit">Edit Dataset Metadata</a></td>
                        </tr>
                    </#if>
                </table>
                <#if user.id != 1 && dataset.typeName = "Taxon">
                    <br />
                    <a id="nbn-download-observations" href="#" data-dataset="${URLParameters.dataset}">Download Dataset</a>
                    <p>Before downloading records from this dataset, you can click on 'Access and Constraints' to check your level of access to the data and apply for better access if necessary</p>
                    <@report_utils.downloadTermsDialogue/>
                </#if>
            </div>
            <div id="tabs-2">
                <table class="nbn-dataset-table nbn-simple-table nbn-metadata-dataset-table">
                    <tr>
                        <th>Your access</th>
                        <td>
                            <@report_utils.datasetAccessPositionByDatasetKey dataset=dataset taxonDataset=taxonDataset/>
                            <br/>
                            <#if dataset.typeName == "Taxon" && (taxonDataset.publicResolution != "100m" || !taxonDataset.publicRecorder || !taxonDataset.publicAttribute)> 
                                <div><a href="/AccessRequest/Create?json={dataset:{all:false,datasets:['${dataset.key}']}}">Request Better Access</a></div>
                            </#if>
                        </td>
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
            <#if dataset.typeName = "Taxon">
                <div id="tabs-3">
                    <h1>Resolution</h1>
                    <p>Some biological records have more precise location information than others. Often they are recorded using national grid references, which identify which 10km, 2km, 1km or 100m square they occur in. This table shows how many records there are at these resolutions for this dataset.</p>
                    <table class="nbn-dataset-table nbn-simple-table nbn-metadata-dataset-table">
                        <#list resolution?sort_by("resolutionID")?reverse as r>
                            <tr><th>${r.label}</th><td style="text-align: right">${r.count} records</td></tr>
                        </#list>
                    </table>
                    <h1>Species Richness Map</h1>
                    <div class="nbn-grid-map">
                        <div class="nbn-gridmap-div">
                            <img class="map" src="${gis}/DatasetSpeciesDensity/${dataset.key}/map?imagesize=4" alt="Species Richness for ${dataset.title}">
                        </div>
                        <div class="nbn-gridmap-legend-div">
                            <p>Number of Species</p>
                            <img class="legend" src="${gis}/DatasetSpeciesDensity/${dataset.key}/legend" alt="Species Richness for ${dataset.title}">
                        </div>
                    </div>
                </div>
            </#if>
        </div>
<#macro hrefForDatasetOnIMT dataset>
    <#if dataset.typeName = "Taxon">
        /imt/?mode=SINGLE_DATASET&dataset=${dataset.key}
    <#elseif dataset.typeName = "Site Boundary">
        /imt/?boundary=${dataset.key}
    <#elseif dataset.typeName = "Habitat">
        /imt/?habitats=${dataset.key}
    </#if>
</#macro>

</@template.master>