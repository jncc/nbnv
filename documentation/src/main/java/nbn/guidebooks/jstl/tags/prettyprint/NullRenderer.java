
package nbn.guidebooks.jstl.tags.prettyprint;

import com.uwyn.jhighlight.renderer.Renderer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NullRenderer implements Renderer {
    private static final int READ_BUFFER_SIZE = 1024;

    public void highlight(String name, InputStream in, OutputStream out, String encoding, boolean fragment) throws IOException {
        byte[] buf = new byte[READ_BUFFER_SIZE];
        int amountRead = 0;
        while ((amountRead = in.read(buf)) != -1) {
            out.write(buf, 0, amountRead);
        }
    }

    public String highlight(String name, String body, String encoding, boolean fragment) throws IOException {
        return body;
    }

}
