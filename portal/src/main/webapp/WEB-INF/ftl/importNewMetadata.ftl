<#assign organisations = json.readURL("${api}/user/organisations")/>

<@template.master title="NBN import" javascripts=["/js/metadata/new-taxon-dataset-access-levels.js"]>

  <h1>Add a new dataset - metadata upload</h1>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form action="Metadata" method="POST" enctype="multipart/form-data" >
    <fieldset>
        <#if organisations?has_content>
            size: ${organisations?size}
            <p>
            <label for="organisationId">Select your organisation</label>
            <select name="organisationId">
              <#list organisations as organisation>
               <option value="${organisation.id}">${organisation.name}</option>
              </#list>
            </select>
            </p>
        </#if>
        <legend>Level of Public Access</legend>
        <span class="formlabel">Geographic Resolution</span>
        <span class="formfield">
            <input type="radio" id="res100" name="resolution" value="100"><label for="res100">Full</label> 
            <input type="radio" id="res1000" name="resolution" value="1000"><label for="res1000">1km</label> 
            <input type="radio" id="res2000" name="resolution" value="2000"><label for="res2000">2km</label> 
            <input type="radio" id="res10000" name="resolution" value="10000"><label for="res10000">10km</label> 
            <input type="radio" id="resNone" name="resolution" value="null"><label for="resNone">No Access</label> 
        </span>
        <br /><br />
        <span class="formlabel">Record Attributes</span>
        <span class="formfield">
            <input type="radio" id="attsTrue" name="recordAtts" value="true"><label for="attsTrue">Yes</label>
            <input type="radio" id="attsFalse" name="recordAtts" value="false"><label for="attsFalse">No</label>
            <input type="radio" id="attsNone" name="recordAtts" value="null"><label for="attsNone">N/A</label>
        </span><br /><br />
        <span class="formlabel">Recorder Names</span>
        <span class="formfield">
            <input type="radio" id="recNamesTrue" name="recorderNames" value="true"><label for="recNamesTrue">Yes</label> 
            <input type="radio" id="recNamesFalse" name="recorderNames" value="false"><label for="recNames">No</label> 
            <input type="radio" id="recNamesNone" name="recorderNames" value="null"><label for="recNamesNone">N/A</label> 
        </span>
        <br /><br />
    </fieldset>                
    <p>
      <input type="file" name="file">
    </p>
      <p>
        <input type="submit" value="Import Metadata">
        <a href="/Import">Back to Dashboard</a>
      </p>
    </form>
  </fieldset>

</@template.master>
