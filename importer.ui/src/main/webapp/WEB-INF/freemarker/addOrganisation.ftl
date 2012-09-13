<#import "spring.ftl" as spring />
<html>
    <head>
        <title>Add an Organisation</title>
        <link rel="stylesheet" type="text/css" href="importer.css" />
    </head>
    <body>
        <#if model.errors?has_content>
            <div class="errors">
                <ul>
                    <#list model.errors as error>
                        <li>${error}</li>
                    </#list>
                </ul>
            </div>
        </#if>
        <form method="post" enctype="multipart/form-data" action="addOrganisationProcess.html">
            <fieldset>
                <legend>Add A New Organisation</legend>
                <p>
                    <span class="formlabel"><label for="organisationName" path="organisation">Organisation Name</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.organisationName" />
                        <input class="wide" path="organisation" type="text" id="organisationName" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>    
                <p>
                    <span class="formlabel"><label for="abbreviation" path="organisation">Organisation Abbreviation</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.abbreviation" />
                        <input class="wide" path="organisation" type="text" id="abbreviation" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="website" path="organisation">Website</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.website" />
                        <input class="wide" path="organisation" type="text" id="website" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>

                <p>
                    <span class="formlabel"><label for="contactName" path="organisation">Contact Name</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.contactName" />
                        <input class="wide" path="organisation" type="text" id="contactName" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p> 
                    <span class="formlabel"><label for="contactEmail" path="organisation">Contact Email</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.contactEmail" />
                        <input class="wide" path="organisation" type="text" id="contactEmail" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="phone" path="organisation">Phone</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.phone" />
                        <input class="wide" path="organisation" type="text" id="phone" name="${spring.status.expression}" length="200" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="address" path="organisation">Address</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.address" />
                        <textarea class="wide" path="organisation" type="text" id="address" name="${spring.status.expression}" rows="6" cols="60">${spring.status.value?default("")}</textarea>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="postcode" path="organisation">Postcode</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.postcode" />
                        <input class="wide" path="organisation" type="text" id="postcode" name="${spring.status.expression}" length="10" value="${spring.status.value?default("")}"/>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="logo" path="organisation">Logo</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.logo" />
                        <img alt="" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAIAAAD/gAIDAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAABp0RVh0U29mdHdhcmUAUGFpbnQuTkVUIHYzLjUuMTAw9HKhAAAC7UlEQVR4Xu3ZvY7iQBAEYPbNyZAICEgISIhIyMgIiXkB3ma3VpaQRY+ha2x62rtFdLorc8xHzY/N13K5XOjlFACWXk6BhTOn2O8UlIJfQFh+KzWLsBKWsBgBIqs1S1iEABFVs4RFCBBRNUtYhAARVbOERQgQUTVLWIQAEVWzhEUIEFE1S1iEABFN1Kz1en04HC6Xy/V6/e69brcb/hL/tN1uiZF9IJoCCxAQ6QMN/fl+vx+Pxw84uN6yMdZms3Ey9flAtt/vXeObNNQSa7fbedo0lDmdTpNSvH+zZlgjpTpBrGXvhzhdog0WZt9QX87nMxxXq9VjjAijRJh6xUsi+9UGq7hOYRPEhviiB3AsekFzuva8eqcGWNj77Jgh5RkwemSvBb3n2vGZBli2Vphi/pEUvWKOYNFYmGi2Glik/FhI2vUrZuWKxrJzkKpVZ2rfxDmLqa/EhqOxsNk/NaviRF6s50gIz+XRWHYG1S03di57RjsyE42Fj4udHjcrKBRaVjEHuwH/F6yRXy8ux5H1CSvm9NCgWeOx7AL/N3fD8VLFowN7+Kj7GPNr1tOjQczHmDkI35lhFW8P6/bTinLNCct2CrUCX8Ww6y6ZBxZOoUWpmIP7Q3YGWDiXFR9mBUvNYM0aeqAaL5UdK5VUaqwhqcgV/WkfSLpm4TTQ/Im73TGTYhVXdNzl1G35U12VEav44LjJr6ozmIZ2AjZcp/pe6ZpVXNf7PyNONacq3icdlr37i3n84rFLh2Vva2Iev8wSyy5YYT84v/VK16wmD9ffMnUBYTmhhEVApcQiP39oPN00DB09+Z8JiwATlrAIASKarlk6ZxHfnrCERQgQUTVLWIQAEVWzCKzM0XRHB2FlFiA+m5olLEKAiKpZwiIEiKiaJSxCgIiqWcIiBIiomiUsQoCIqlnCIgSIqJolLEKAiKpZwiIEiKiaJSxCgIiqWcIiBIiomiUsQoCIqlnCIgSIqJolLEKAiKpZwiIEiOgPTJE0DTZiTIcAAAAASUVORK5CYII=" />
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                        <@spring.bind "model.organisation.logoSmall" />
                        <img alt="" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAIAAAD/gAIDAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAABp0RVh0U29mdHdhcmUAUGFpbnQuTkVUIHYzLjUuMTAw9HKhAAAC7UlEQVR4Xu3ZvY7iQBAEYPbNyZAICEgISIhIyMgIiXkB3ma3VpaQRY+ha2x62rtFdLorc8xHzY/N13K5XOjlFACWXk6BhTOn2O8UlIJfQFh+KzWLsBKWsBgBIqs1S1iEABFVs4RFCBBRNUtYhAARVbOERQgQUTVLWIQAEVWzhEUIEFE1S1iEABFN1Kz1en04HC6Xy/V6/e69brcb/hL/tN1uiZF9IJoCCxAQ6QMN/fl+vx+Pxw84uN6yMdZms3Ey9flAtt/vXeObNNQSa7fbedo0lDmdTpNSvH+zZlgjpTpBrGXvhzhdog0WZt9QX87nMxxXq9VjjAijRJh6xUsi+9UGq7hOYRPEhviiB3AsekFzuva8eqcGWNj77Jgh5RkwemSvBb3n2vGZBli2Vphi/pEUvWKOYNFYmGi2Glik/FhI2vUrZuWKxrJzkKpVZ2rfxDmLqa/EhqOxsNk/NaviRF6s50gIz+XRWHYG1S03di57RjsyE42Fj4udHjcrKBRaVjEHuwH/F6yRXy8ux5H1CSvm9NCgWeOx7AL/N3fD8VLFowN7+Kj7GPNr1tOjQczHmDkI35lhFW8P6/bTinLNCct2CrUCX8Ww6y6ZBxZOoUWpmIP7Q3YGWDiXFR9mBUvNYM0aeqAaL5UdK5VUaqwhqcgV/WkfSLpm4TTQ/Im73TGTYhVXdNzl1G35U12VEav44LjJr6ozmIZ2AjZcp/pe6ZpVXNf7PyNONacq3icdlr37i3n84rFLh2Vva2Iev8wSyy5YYT84v/VK16wmD9ffMnUBYTmhhEVApcQiP39oPN00DB09+Z8JiwATlrAIASKarlk6ZxHfnrCERQgQUTVLWIQAEVWzCKzM0XRHB2FlFiA+m5olLEKAiKpZwiIEiKiaJSxCgIiqWcIiBIiomiUsQoCIqlnCIgSIqJolLEKAiKpZwiIEiKiaJSxCgIiqWcIiBIiomiUsQoCIqlnCIgSIqJolLEKAiKpZwiIEiOgPTJE0DTZiTIcAAAAASUVORK5CYII=" />
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="summary" path="organisation">Summary</label></span>
                    <span class="formfield">
                        <@spring.bind "model.organisation.summary" />
                        <textarea class="wide" path="organisation" type="text" id="summary" name="${spring.status.expression}" rows="6" cols="60">${spring.status.value?default("")}</textarea>
                        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
                    </span>
                </p>
                <p>
                    <span class="formlabel"><label for="allowPublicRegistration" path="organisation">Allow Public Registration</label></span>
                    <span class="formfield">
                        <@spring.formCheckbox "model.organisation.allowPublicRegistration", "" />
                    </span>
                </p>

                <p>
                    <input type="submit" name="submit" value="submit" />
                </p>
            </fieldset>
        </form>
    </body>
</html>