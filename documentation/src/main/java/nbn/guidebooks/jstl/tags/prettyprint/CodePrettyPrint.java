
package nbn.guidebooks.jstl.tags.prettyprint;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

public class CodePrettyPrint extends BodyTagSupport {
    private static final String ENCODING = "UTF-8";

    private PrettyPrintableLanguage languageRenderer;
    private String codeSegmentName = ""; //no code name set

    public void setLang(String lang) {
        this.languageRenderer = PrettyPrintableLanguage.getPrettyPrintLanguage(lang);
    }

    public void setName(String name) {
        this.codeSegmentName = name;
    }

    @Override
    public int doAfterBody() throws JspException {
        BodyContent bodycontent = getBodyContent();
        String body = bodycontent.getString();
        JspWriter out = bodycontent.getEnclosingWriter();
        try {
            out.print("<code class=\"prettyPrint\" lang=\""); out.print(languageRenderer.getLanguage()); out.print("\">");
                out.print(languageRenderer.getRenderer().highlight(codeSegmentName, body, ENCODING, true));
            out.print("</code>");
        }
        catch(IOException io) {
            throw new JspException("An IOException has occurred",io);
        }
        return SKIP_BODY;
    }
}