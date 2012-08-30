package nbn.common.util.json;

import java.io.IOException;
import java.io.Writer;

public class JSONPWriter extends Writer {

    private Writer wrappedUpStream;

    public JSONPWriter(Writer wrappedUpStream, String jsonPCallback) throws IOException {
        this.wrappedUpStream = wrappedUpStream;
        wrappedUpStream.write(jsonPCallback);
        wrappedUpStream.write('(');
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return wrappedUpStream.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return wrappedUpStream.append(csq, start, end);
    }

    @Override
    public Writer append(char c) throws IOException {
        return wrappedUpStream.append(c);
    }

    @Override
    public void write(int b) throws IOException {
        wrappedUpStream.write(b);
    }

    @Override
    public void write(char b[]) throws IOException {
        wrappedUpStream.write(b);
    }

    @Override
    public void write(char b[], int off, int len) throws IOException {
        wrappedUpStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        wrappedUpStream.flush();
    }

    @Override
    public void close() throws IOException {
        try {
            wrappedUpStream.write(");");
            wrappedUpStream.flush();
        } finally {
            wrappedUpStream.close();
        }
    }
}
