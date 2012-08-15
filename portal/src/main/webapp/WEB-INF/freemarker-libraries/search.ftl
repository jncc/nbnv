<#-- 
    The following template defines the structure of a Search Widget which can 
    be powered by NBN Gateway API standardised search resources
-->

<#--Define a tree macro for facet rendering
    @param name (Scalar) Specifies the name of the current facet
    @param data (Sequence) The data which is to be rendered up in tree form.
        The structure of the data sequence is quite forgiving.
        An example of the structure of this would be :
        <code>
            [{
                "name":"Name which will be displayed in tree",
                "id": 'ID_AS_DEFINED_IN_COUNTS_HASH',
                "children": [{
                    "name":"A will be rendered as a tree child of my hash parent",
                    "id": 'ID_AS_DEFINED_IN_COUNTS_HASH',
                }]
            },{
                "id": 'THIS_ID_WILL_BE_RENDERED_AS_NAME'
            },"SO_WOULD_THIS_ONE"]
        </code>
    @param counts (hash) A hash of ids to count values
-->
<#macro tree name data counts>
    <h1>${name}</h1>
    <ul class="collapsible-list"><@__treeFacetHelper name data counts/></ul>
</#macro>

<#macro __treeFacetHelper name data counts>
    <@__listID data; currentFacet>
        <li>
            <@__filterInputBox name currentFacet/>
            <#if currentFacet.children?has_content>
                ${currentFacet.name} (${(counts[currentFacet.id])!0})
                <ul><@__treeFacetHelper name currentFacet.children counts/></ul>
            <#else>
                ${currentFacet.name!currentFacet.id} (${(counts[currentFacet.id])!0}) 
            </#if>
        </li>
    </@__listID>
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
            * name      - Name of the parameter 
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
<#macro search url query={} facets=[]>
    <#assign search=json.readURL(url, query)/>
    <form class="nbn-search">    
        <@__facets facets search.facetFields/>
        <div class="controls">
            Search - <input type="text" name="q" value="${RequestParameters.q?first!''}"/>
            Show - <@pagination.show/> 
            <input type="submit" value="Filter"/>
        </div>
        <ol class="results">
            <#list search.results as result>
                <li><#nested result></li>
            </#list>
        </ol>

    </form>
    <@pagination.paginator search/>
</#macro>

<#-- Start defining the utilities used for creating facets -->
<#macro __facets facets counts>
    <ul class="nbn-search-facets">
        <@__listID facets; facetConfig>
            <li class="nbn-search-facet">
                <@.vars[facetConfig.render!"tree"] 
                    name=facetConfig.name!facetConfig.id
                    data=facetConfig.data!counts[facetConfig.id]?keys
                    counts=counts[facetConfig.id]
                />
            </li>
        </@__listID>
    </ul>
</#macro>

<#macro __filterInputBox name currentFacet>
    <input 
        type="checkbox" 
        name="${name}" 
        value="${currentFacet.id}"
        ${RequestParameters[name]?seq_contains(currentFacet.id)?string('checked="checked"','')}
    />
</#macro>

<#macro __listID sequence>
    <#list sequence as id>
        <#--Check if the id object is already an id-->
        <#if id?is_hash>
            <#nested id>
        <#else>
            <#nested {"id" : id}/>
        </#if>
    </#list>
</#macro>