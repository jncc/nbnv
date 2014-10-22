/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.wordImporter;

import java.util.List;

/**
 *
 * @author Matt Debont
 */
public class WordImporter_3_3 extends WordImporter_3_0 {
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final int MAJOR = 3;
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final int MINOR = 3;
    
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    private final String FORM_SIGNATURE = "^(I,  )(FORMTEXT (.*)\\. \\[INSERT NAME\\])(, hereby confirm that on  )(FORMTEXT (.*) \\[dd/mm/yyyy\\])( I read and understood the NBN Gateway Data Provider Agreement and agree, on behalf of the data provider named in section A, to submit the dataset, and any future updates to the dataset, described in section E to the NBN Trust under this agreement\\.)$";
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    private final String FORM_SIGNATURE_IDENTIFIER = "I read and understood the NBN Gateway Data Provider Agreement and agree, on behalf of the data provider named in section A, to submit the dataset, and any future updates to the dataset, described in section E to the NBN Trust under this agreement.";    
    
    public WordImporter_3_3() {
        super();
    }

    @Override
    public boolean supports(int major, int minor) {
        return major == MAJOR && minor == MINOR;
    }
    
    @Override
    public List<String> getDefaultMessages() {
        return defaultMessages;
    }
    
    @Override
    protected String getFormSignatureIdentifier() {
        return FORM_SIGNATURE_IDENTIFIER;
    }
    
    @Override
    protected String getFormSignature() {
        return FORM_SIGNATURE;
    }
    
}
