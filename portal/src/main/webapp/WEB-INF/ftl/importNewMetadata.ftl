<#assign organisations = json.readURL("${api}/user/adminOrganisations")/>

<@template.master title="NBN import" javascripts=["/js/metadata/new-taxon-dataset-access-levels.js"]>

  <h1>Upload a new taxon dataset - metadata upload</h1>
  <p>
    Before you can add your new taxon dataset you must provide its metadata.  Do this by filling in the form below, which includes uploading a pre-filled Word metadata document (available <a href="http://www.nbn.org.uk/Share-Data/Providing-Data/Metadata-form-for-species-datasets.aspx">here</a>).  Press 'Import Metadata' to submit and go to the taxon dataset upload page.
  </p>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form action="NewMetadata" method="POST" enctype="multipart/form-data" >
    <fieldset>
        <legend>Level of Public Access</legend>
        <span class="formlabel">Geographic Resolution</span>
        <span class="formfield">
            <input type="radio" id="res100" name="resolution" value="100m"><label for="res100">Full</label> 
            <input type="radio" id="res1000" name="resolution" value="1km"><label for="res1000">1km</label> 
            <input type="radio" id="res2000" name="resolution" value="2km"><label for="res2000">2km</label> 
            <input type="radio" id="res10000" name="resolution" value="10km"><label for="res10000">10km</label> 
            <input type="radio" id="resNone" name="resolution" value="No Access" checked="checked"><label for="resNone">No Access</label> 
        </span>
        <br /><br />
        <span class="formlabel">Record Attributes (only available when Geographic Resolution is set to Full)</span>
        <span class="formfield">
            <input type="radio" id="attsTrue" name="recordAtts" value="true"><label for="attsTrue">Yes</label>
            <input type="radio" id="attsFalse" name="recordAtts" value="false" checked="checked"><label for="attsFalse">No</label>
        </span><br /><br />
        <span class="formlabel">Recorder Names (only available when Geographic Resolution is set to Full)</span>
        <span class="formfield">
            <input type="radio" id="recNamesTrue" name="recorderNames" value="true"><label for="recNamesTrue">Yes</label> 
            <input type="radio" id="recNamesFalse" name="recorderNames" value="false" checked="checked"><label for="recNames">No</label>
        </span>
        <br /><br />
    </fieldset>                
    <#if organisations?has_content>
        <#if organisations?size=1>
            <input type="hidden" name="organisation" value="${organisations[0].id}">
        <#else>
            <p>
            <label for="organisation">Select the organisation your new dataset belongs to: </label>
            <select name="organisation">
              <#list organisations as organisation>
               <option value="${organisation.id}">${organisation.name}</option>
              </#list>
            </select>
            </p>
        </#if>
    </#if>
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
