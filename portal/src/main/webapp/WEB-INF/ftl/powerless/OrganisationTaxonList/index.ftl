<@template.master title="NBN Gateway - Organisation Supplied Taxon Lists">
    
    <#assign designationCategories=json.readURL("${api}/designationCategories")>

    <div id="nbn-designation-content">
        <h1>Organisation Supplied Taxon Lists</h1>

        <ul>
            <#assign oLists=json.readURL("${api}/organisationList")>
            <#list oLists as oList>
                <li class="nbn-designation-nested-list">
                    <a href="/OrganisationTaxonList/${oList.code}">${oList.name}</a> [${oList.code}] : ${oList.description!}
                </li>
            </#list>
        </ul>

        <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://www.jncc.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
    </div>
</@template.master>