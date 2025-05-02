package logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class Logger implements ILog {
    protected final OutputStream baseStream;
    protected final PrintStream out;

    public Logger(PrintStream out) {
        this.baseStream = null;
        this.out = out;
    }

    public Logger(OutputStream baseStream) {
        this.baseStream = baseStream;
        this.out = new PrintStream(baseStream, true);
    }

    @Override
    public void write(long value) {
        out.println(value);
    }

    @Override
    public void write(String value) {
        out.println(value);
    }

    @Override
    public void write(Object... values) {
        for (Object value : values) {
            out.print(value + " ");
        }
        out.println();
    }

    //@Override
    public void writeTime(String msg, long time, TimeUnit unit) {
        double convertedTime = TimeUnit.convert(time, unit);
        out.println(msg + " " + convertedTime + " " + unit);
    }

    @Override
    public void close() {
        out.flush();
        if (baseStream != null) {
            out.close();
        }
    }
}

