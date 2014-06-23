
package nbn.guidebooks.jstl.tags;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Navigation extends SimpleTagSupport {
    private static final FileFilter ACCESSABLE_DOCUMENT_FILTER = new FileFilter(){
        private static final String documentFile = "index.jsp";
        public boolean accept(File toAccept) {
            return toAccept.isDirectory() && new File(toAccept,documentFile).exists();
        }
    };

    private static final int INFINITE_DEPTH = -1;
    private int maximumDepth = INFINITE_DEPTH;
    
    private ServletContext context;
    private boolean createTitle = true;
    private String root = "";

    public void setContext(ServletContext context ) {
        this.context = context;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setDepth(int maximumDepth) {
        this.maximumDepth = maximumDepth;
    }

    public void setCreateTitle(boolean createTitle) {
        this.createTitle = createTitle;
    }
    
    private void listFiles(File[] files, JspWriter out, String href, int maximumDepthLeft) throws java.io.IOException {
        if(maximumDepthLeft !=0) {
            out.print("<ul>");
            for(File currFile : files) {
                String currHref = href + '/' + currFile.getName();
                out.print("<li>"
                    + "<a href=\""); out.print(currHref); out.print("\">");
                        out.print(formatDocumentTitle(currFile.getName()));
                    out.print("</a>");
                    listFiles(currFile.listFiles(ACCESSABLE_DOCUMENT_FILTER), out, currHref, maximumDepthLeft-1);
                out.print("</li>");
            }
            out.print("</ul>");
        }
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        File rootFile = new File(context.getRealPath("/" + root));

        if(createTitle)
            listFiles(new File[]{rootFile}, out, context.getContextPath(), (maximumDepth == INFINITE_DEPTH) ? INFINITE_DEPTH : maximumDepth+1);
        else
            listFiles(rootFile.listFiles(ACCESSABLE_DOCUMENT_FILTER), out, context.getContextPath() + root, maximumDepth);
    }

    public static String formatDocumentTitle(String in) {
        return in.replaceAll("_"," ");
    }
}
