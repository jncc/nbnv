<@template.master title="NBN Gateway - Datasets"
    javascripts=["/js/enable-tabs.js","/js/jqueryui.simple-table-style.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] >

    <#assign datasetId="${URLParameters.dataset}">
    <#assign dataset=json.readURL("${api}/datasets/${datasetId}")>

        <h1>${dataset.name}</h1>

        <div id="nbn-tabs">
            
            <ul>
                <li><a href="#tabs-1">General</a></li>
                <li><a href="#tabs-2">Access and constraints</a></li>
                <li><a href="#tabs-3">Geographical</a></li>
                <#if dataset.typeName = "Taxon">
                    <li><a href="#tabs-10">Temporal</a></li>
                    <li><a href="#tabs-11">Surveys</a></li>
                    <li><a href="#tabs-12">Attributes</a></li>
                    <li><a href="#tabs-13">Species</a></li>
                <#elseif dataset.typeName = "Habitat">
                    <li><a href="#tabs-20">Attributes</a></li>
                <#elseif dataset.typeName = "Site Boundary">
                    <li><a href="#tabs-30">Sites</a></li>
                </#if>
            </ul>

            <div id="tabs-1">
                <table class="nbn-simple-table">
                    <tr>
                        <td>Provider</td>
                        <td>[TODO Logo] <a href="/Organisations/${dataset.organisationID}">${dataset.organisationName}</a></td>
                    </tr>
                    <tr>
                        <td>Title</td>
                        <td>${dataset.name}</td>
                    </tr>
                    <tr>
                        <td>Permanent key</td>
                        <td>${dataset.datasetKey}</td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td>${dataset.description}</td>
                    </tr>
                    <tr>
                        <td>Date uploaded</td>
                        <td>${dataset.formattedDateUploaded}</td>
                    </tr>
                    <tr>
                        <td>Purpose of data capture</td>
                        <td>TBA</td>
                    </tr>
                    <tr>
                        <td>Methods of data capture</td>
                        <td>TBA</td>
                    </tr>
                    <tr>
                        <td>Geographical coverage</td>
                        <td>TBA</td>
                    </tr>
                    <tr>
                        <td>Temporal coverage</td>
                        <td>TBA</td>
                    </tr>
                    <tr>
                        <td>Data quality</td>
                        <td>${dataset.quality!"Not available"}</td>
                    </tr>
                    <tr>
                        <td>Additional information</td>
                        <td>TBA</td>
                    </tr>
                </table>
            </div>

            <div id="tabs-2">
                [TODO - Access and constraints]
            </div>

            <div id="tabs-3">
                [TODO: Ajax call to map]
                <!--<iframe width="100%" height="600px" src="http://data.nbn.org.uk/imt/?baselayer=Hybrid&bbox=-18.576014044435876,49.03927489540209,9.5489859505262,60.45827601788278&mode=SINGLE_DATASET&dataset=GA001044"></iframe>-->
            </div>

            <#if dataset.typeName = "Taxon">
                <div id="tabs-10">
                    TODO - temporal
                </div>
                <div id="tabs-11">
                    TODO - Surveys
                </div>
                <div id="tabs-12">
                    TODO - Species attributes
                </div>
                <div id="tabs-13">
                    TODO - Species
                </div>
            <#elseif dataset.typeName = "Habitat">
                <div id="tabs-20">
                    TODO - Habitat attributes
                </div>
            <#elseif dataset.typeName = "Site Boundary">
                <div id="tabs-30">
                    TODO - Site list
                </div>
            </#if>
        </div>

</@template.master>
