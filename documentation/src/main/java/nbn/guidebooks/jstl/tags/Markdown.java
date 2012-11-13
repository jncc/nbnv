
package nbn.guidebooks.jstl.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.tautua.markdownpapers.parser.ParseException;

public class Markdown extends BodyTagSupport {
    @Override
    public int doAfterBody() throws JspException {
        org.tautua.markdownpapers.Markdown md = new org.tautua.markdownpapers.Markdown();
        try {
            md.transform(bodyContent.getReader(), bodyContent.getEnclosingWriter());
        }
        catch (ParseException ex) {
            throw new JspException("A parseException has occurred when processing Markdown", ex);
        }
        return SKIP_BODY;
    }
}
