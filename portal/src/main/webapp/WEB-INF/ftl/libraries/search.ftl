<#-- 
    The following template defines the structure of a Search Widget which can 
    be powered by NBN Gateway API standardised search resources
-->

<!--Define a combo filter for searching-->
<#macro combo filter>
    <label>${filter.name}</label>
    <select name="${filter.id}">
        <option value="">All</option>
        <#list filter.data as currentFacet>
            <option 
                value="${currentFacet.key}"
                ${RequestParameters[filter.id]?html?seq_contains(currentFacet.key)?string('selected="selected"','')}
                >${currentFacet.name}</option>
        </#list>
    </select>
</#macro>

<!--Define a spatial filter for searching-->
<#macro spatial filter>
    <input type="hidden" name="${filter.id}" spatial-layer="${filter.layer}" map-div="${filter.map}" value="-180,-90,180,90"/>
</#macro>

<#--The following macro will render search results of a particular query to a 
    particular search resource. A sequence of facet configurations can be 
    supplied if rendering of facets is required
    @param url (string/java.net.URL) The url which should be called in order
        to perform a search
    @param query (optional) (hash) The query hash which will be passed to the 
        readURL method in order to obtain some search results
    @param facets (optional) (sequence) A sequence of freemarker hashs or 
        strings which represent which facets to be rendered for later filtering
        of search results. The form of the facets configuration can be as simple
        as:
        <code>
            ["facetIDOne", "facetIDtwo"]
        </code>
        The above example will render the ids of the facets with default
        rendering. If a finer level of configuration is required then the more
        verbose form will be more of use:
        <code>
            [{
                "id" : "facetIDOne",            //This key is required to target
                                                // a particular facet.

                "name" : "Display Name One",    //This key will display configure
                                                //the display name of this facet.
                                                //The default is to render the ID

                "render" : "tree",              //One can specify a particular 
                                                //rendering function for there 
                                                //facet. Default is "tree"

                "data" : [...]                  //A data object to pass to the
                                                //render function. Default is
                                                //sequence of facet keys returned
                                                //from the search result for this
                                                //facet id.
            },{
                "id" : "facetIDTwo",
                "name" : "Display Name for facetIDTwo",
                "render" : "anotherRenderFunction",
                "data" : [...]
            }]
        </code>

        The render key of the verbose configuration should point to a macro 
        defined in this library. That macro must take in three parameters:
            * id        - The query search name
            * name      - Display name of the parameter 
            * data      - Some data to be used by that function
            * counts    - A hash of ids to result counts
        In the verbose configuration, optional keys can be emitted. It is also
        valid to mix and match verbose and simple facet configurations. E.g
        <code>
            ["renderMeWithDefaultSettings", {
                "id" :"configureMeWithTheBelow",
                "name":"Not Default Name"
            }]
        </code>
-->
<#macro search url display defaultRows=25 query={} filters=[]>
    <#assign search=json.readURL(url, query)/>
    <form class="nbn-search" nbn-search-node="${url}">    
        <div class="controls">
            Search - <input type="text" name="q" value="${RequestParameters.q?first!''?html}"/>
            Show - <@pagination.show defaultRows/> 
            <input type="submit" value="Filter"/>
        </div>
        <#if filters?size != 0><@__renderFilters filters/></#if>
        <table class="results">
            <thead>
                <tr><@__tableHeader display/></tr>
            </thead>
            <tbody>
                <#list search.results as result>
                    <tr><@__renderRow result display/></tr>
                </#list>
            </tbody>
        </table>
        <@pagination.paginator search/>
    </form>
</#macro>

<#-- Macro to create the header of the table -->
<#macro __tableHeader display>
    <#list display as column>
        <th result-attr="${column.attr}" <#if column.link??>result-link="${column.link}"</#if> <#if column.width??>style="width:${column.width}"</#if>>${column.title}</th>
    </#list>
</#macro>

<#-- Macro to render a row of results -->
<#macro __renderRow result display>
    <#list display as column>
        <td>
            <#if column.link??>
                <a href="${result[column.link]}">${result[column.attr]!""}</a>
            <#else>
                ${result[column.attr]!""}
            </#if>
        </td>
    </#list>
</#macro>

<#-- Macro to render a row of results -->
<#macro __renderFilters filters>
    <ul class="filters">
        <#list filters as filter>
            <li><@.vars[filter.type!"combo"] filter/></li>
        </#list>
    </ul>
</#macro>