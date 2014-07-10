/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.tools;

import java.text.Normalizer;

/**
 *
 * @author Matt Debont
 */
public class TextTools {
    public static String normalizeAndTrim(String input) {
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFKD).replaceAll("âââââ", "").replaceAll("     ", "");
    }
}
