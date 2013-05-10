/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Matt Debont
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException {
    
}
