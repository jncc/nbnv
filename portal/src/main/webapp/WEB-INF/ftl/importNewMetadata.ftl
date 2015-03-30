<@template.master title="NBN import">


  <h1>Add a new dataset - metadata upload</h1>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <fieldset>
        <legend>Level of Public Access</legend>
      <p>
        <input type="file" name="file">
      </p>
        TODO: Simple form here for resolution, recorder names and attributes
    </fieldset>                
      </p>
      <p>
        <input type="submit" value="Import Metadata">
        <a href="/Import">Back to Dashboard</a>
      </p>
    </form>
  </fieldset>

</@template.master>
