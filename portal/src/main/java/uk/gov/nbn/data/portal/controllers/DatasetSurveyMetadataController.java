package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import javax.validation.Valid;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.exceptions.ForbiddenException;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
@Controller
@RequestMapping(value = "/Datasets/{dataset}/Surveys/{survey}")
public class DatasetSurveyMetadataController {

    @Autowired
    WebResource resource;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getSurvey(@PathVariable("dataset") String dataset, @PathVariable("survey") int id) {
        try {
            Survey survey = resource.path("taxonDatasets/" + dataset + "/surveys/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Survey.class);
            
            ModelAndView view = new ModelAndView("survey-edit", "survey", survey);
            view.addObject("datasetKey", survey.getDatasetKey());
            view.addObject("id", survey.getId());
            
            return view;

        } catch (UniformInterfaceException ex) {
            if (ex.getResponse().getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
                throw new ForbiddenException();
            }
        }
        
        throw new NotFoundException();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView updateSurvey(@PathVariable("dataset") String dataset, @PathVariable("survey") int id, @Valid Survey survey) {
        try {
            resource.path("taxonDatasets/" + dataset + "/surveys/" + id)
                    .type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, survey);

            ModelAndView view = getSurvey(dataset, id);
            view.addObject("success", "Survey Metadata was successfully modifed, it may take up to a day for this information to propogate into the database");
            
            return view;
        } catch (UniformInterfaceException ex) {
            if (ex.getResponse().getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
                throw new ForbiddenException();
            } else if (ex.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new NotFoundException();
            }
            throw new UniformInterfaceException(ex.getResponse());
        }
    }
}
