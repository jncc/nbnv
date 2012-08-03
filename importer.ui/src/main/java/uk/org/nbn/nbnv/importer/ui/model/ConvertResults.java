/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.model;

import java.util.List;

/**
 *
 * @author Paul Gilbertson
 */
public class ConvertResults {
    private List<String> messages;
    private List<String> steps;
    private List<String> errors;

    /**
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * @return the steps
     */
    public List<String> getSteps() {
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    /**
     * @return the rows
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * @param rows the rows to set
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
