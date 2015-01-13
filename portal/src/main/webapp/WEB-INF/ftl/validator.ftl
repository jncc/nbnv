<@template.master title="NBN Gateway - Online Record Validator" 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] 
    javascripts=["/js/validator/validator.js"]>

    <#assign organisations=json.readURL("${api}/organisations")>

    <div id="stage1">
        <form id="fileUpload">
            <fieldset>
                <legend>Upload Data File</legend>
                <p>Please select the data file that you want to validate, this file must be in the NBN Exchange Format</p>
                <p>Data File: <input type="file" name="file"></p>
                <input type="submit" value="Upload File" />
            </fieldset>
        </form>
    </div>
        
    <div id="stage2" style="display:none;">
        <form id="headersForm">

        </form>
    </div>

    <div id="stage3-metadata" style="display: none;" style="display:none;">
        <form id="metadataUpload">
            Upload Metadata : <input type="file" name="file"><br />
            <input type="submit" value="Upload">
        </form>
        <form id="metadata">
            <fieldset>
                <legend>Dataset Metadata</legend>
                <p>
                    <span class="formlabel"><label for="title">Title</label></span>
                    <input id="title" name="title" class="wide" length="200" />
                </p>
                <p>
                    <span class="formlabel"><label for="organisationID">Organisation</label></span>
                    <span class="formfield">
                        <select  name="organisationID" id="organisationID">
                            <option value="-404" selected="selected"></option>
                            <#list organisations as org>
                            <option value="${org.id}">${org.name}</option>
                            </#list>
                        </select>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="description">Description</label></span>
                    <textarea id="description" name="description" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="methods" >Methods of Data Capture</label></span>
                    <textarea id="methods" name="methods" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="purpose" >Purpose of Data Capture</label></span>
                    <textarea id="purpose" name="purpose" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="geographic" >Geographical Coverage</label></span>
                    <textarea id="geographic" name="geographic" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="temporal" >Temporal Coverage</label></span>
                    <textarea id="temporal" name="temporal" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="quality" >Data Quality</label></span>
                    <textarea id="quality" name="quality" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="info" >Additional Information</label></span>
                    <textarea id="info" name="info" class="wide" rows="6" cols=60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="use" >Use Constraints</label></span>
                    <textarea id="use" name="use" class="wide" rows="6" cols="60"></textarea>
                </p>
                <p>
                    <span class="formlabel"><label for="access" >Access Constraints</label></span>
                    <textarea id="access" name="access" class="wide" rows="6" cols="60"></textarea>
                </p>

                <fieldset>
                    <legend>Dataset Administrator Details</legend>
                    <p>Please enter the email address of the dataset administrator, they must have an account on the NBN Gatway already</p>
                    <p>
                        <span class="formlabel"><label for="datasetAdminEmail" >E-mail Address</label></span>
                        <input id="datasetAdminEmail" name="datasetAdminEmail" class="wide" length="200" />
                    </p>

                    <input type="hidden" id="datasetAdminName" name="datasetAdminName" class="wide" length="200" />
                    <input type="hidden" id="datasetAdminPhone" name="datasetAdminPhone" />
                    <input type="hidden" id="datasetAdminID" name="datasetAdminID" />
                </fieldset>

                <fieldset>
                    <legend>Level of Public Access</legend>
                    <br />
                    <span class="formlabel">Geographic Resolution</span>
                    <span class="formfield">
                        <span><input type="radio" name="geographicalRes" value="10km" />10km</span>
                        <span><input type="radio" name="geographicalRes" value="2km" />2km</span>
                        <span><input type="radio" name="geographicalRes" value="1km" />1km</span>
                        <span><input type="radio" name="geographicalRes" value="100m" />100m</span>
                    </span> <br /> <br />

                    <span class="formlabel">Record Attributes</span>
                    <span class="formfield">
                        <span><input type="radio" name="recordAtts" value="Yes" />Yes</span>
                        <span><input type="radio" name="recordAtts" value="No" />No</span>
                        <span><input type="radio" name="recordAtts" value="N/A" />N/A</span>
                    </span> <br /> <br />

                    <span class="formlabel">Recorder Names</span>
                    <span class="formfield">
                        <span><input type="radio" name="recorderNames" value="Yes" />Yes</span>
                        <span><input type="radio" name="recorderNames" value="No" />No</span>
                        <span><input type="radio" name="recorderNames" value="N/A" />N/A</span>
                    </span> <br /> <br />
                </fieldset>                

                <p>
                    <input type="submit" name="submit" value="Submit Metadata" />
                    <input type="submit" name="clearForm" value="Clear Form" />
                </p>
            </fieldset>
        </form>
    </div>

    <div id="stage3-skip" style="display:none;">
        <form id="stage3-friendlyName">
            <fieldset>
                <legend>Add a name for your dataset</legend>
                <p>
                    <span class="formlabel"><label for="friendlyName" >Enter a friendly name for this dataset</label></span>
                    <input id="friendlyName" class="wide" length=200" />
                </p>
                <input id="addFriendlyName" type="submit" name="addFriendlyName" value="Submit for Processing"/>
            </fieldset>
        </form>
    </div>

    <div id="stage4" style="display:none;">
        <p>Successfully submitted to queue, you will receive an email when this process is completed</p>
    </div>

    <div id="stageCounter" style="">
        <div style="margin: 0 auto; text-align:center">
            <span id="stageCount1" class="stageCounter" style="padding: 10px; color: black;">1</span>
            <span id="stageCount2" class="stageCounter" style="padding: 10px; color: lightgrey;">2</span>
            <span id="stageCount3" class="stageCounter" style="padding: 10px; color: lightgrey;">3</span>
            <span id="stageCount4" class="stageCounter" style="padding: 10px; color: lightgrey;">4</span>
        </div>
    </div>
</@template.master>