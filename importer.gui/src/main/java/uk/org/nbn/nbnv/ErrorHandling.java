package uk.org.nbn.nbnv;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * include javadoc
 */
public class ErrorHandling {

    /**
     * Default for all validation errors
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<String> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        // make a friendly message that we can display
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> messages = new ArrayList<String>(fieldErrors.size());
        for(FieldError fieldError : fieldErrors){
            messages.add(fieldError.getCode());
            Logger.warn("Validation failed, and was handled by informing user : " +fieldError.getCode());
        }
        return messages;
    }
}
