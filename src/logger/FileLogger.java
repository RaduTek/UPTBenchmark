package logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileLogger extends Logger {
    public FileLogger(String fileName) throws FileNotFoundException {
        super(new FileOutputStream(fileName));
    }

    @Override
    public void close() {
        super.close();
        try {
            if (baseStream != null) {
                baseStream.flush();
                baseStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
