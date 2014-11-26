/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.daemon.exceptions;

/**
 *
 * @author Matt Debont
 */
public class MissingFileException extends Exception {
    public MissingFileException(String fileName) {
        super(String.format("%s is missing from the upload, aborting", fileName));
    }
    
    public MissingFileException() {
        super("A file was missing from the upload, aborting");
    }
}
