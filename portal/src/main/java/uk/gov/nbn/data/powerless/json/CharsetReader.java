/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.json;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Developer
 */
public class CharsetReader {
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");

    /**
     * Parse out a charset from a content type header.
     * 
     * @param contentType
     *            e.g. "text/html; charset=EUC-JP"
     * @return "EUC-JP", or null if not found. Charset is trimmed and
     *         uppercased.
     */
    public static Charset getCharsetFromContentType(String contentType) {
        if (contentType != null) {
            Matcher m = charsetPattern.matcher(contentType);
            if (m.find()) {
                String charset = m.group(1).trim().toUpperCase();
                if(Charset.isSupported(charset)) {
                    return Charset.forName(charset);
                }
            }
        }
        return Charset.defaultCharset();
    }
}
