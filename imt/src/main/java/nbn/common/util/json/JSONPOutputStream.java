
package nbn.common.util.json;

import java.io.IOException;
import java.io.OutputStream;

public class JSONPOutputStream extends OutputStream {
    private OutputStream wrappedUpStream;

    public JSONPOutputStream(OutputStream wrappedUpStream, String jsonPCallback) throws IOException {
        this.wrappedUpStream = wrappedUpStream;
        wrappedUpStream.write((jsonPCallback+'(').getBytes());
    }
    
    @Override
    public void write(int b) throws IOException {
        wrappedUpStream.write(b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        wrappedUpStream.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        wrappedUpStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        wrappedUpStream.flush();
    }

    @Override
    public void close() throws IOException {
        try {
            wrappedUpStream.write(");".getBytes());
            wrappedUpStream.flush();
        } finally {
            wrappedUpStream.close();
        }
    }
}
