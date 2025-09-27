package net.disy.commons.core.io;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class IOUtilities {
    private static final DecimalFormat DECIMAL_FORMAT_3 = new DecimalFormat("000");
    private static final DecimalFormat DECIMAL_FORMAT_2_1 = new DecimalFormat("00.0");
    private static final DecimalFormat DECIMAL_FORMAT_1_2 = new DecimalFormat("0.00");
    private static final DecimalFormat DECIMAL_FORMAT_1_3 = new DecimalFormat("0.000");
    @SuppressWarnings("unused")
    public static final long ONE_KB = 1024L;
    @SuppressWarnings("unused")
    public static final long ONE_MB = 1048576L;
    @SuppressWarnings("unused")
    public static final long ONE_GB = 1073741824L;

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
            }
        }
    }

    public static String readString(Reader reader) throws IOException {
        StringBuffer buffer = new StringBuffer();
        char[] buf = new char[1024];
        int numChars = 0;

        while ((numChars = reader.read(buf)) > 0) {
            buffer.append(buf, 0, numChars);
        }

        return buffer.toString();
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        copyStream(in, out, 4096);
    }

    public static void copyStream(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];

        int numChars;
        while ((numChars = in.read(buffer)) > 0) {
            out.write(buffer, 0, numChars);
        }
    }

    public static void copyStream(Reader reader, Writer writer) throws IOException {
        copyStream(reader, writer, 4096);
    }

    public static void copyStream(Reader in, Writer out, int bufferSize) throws IOException {
        char[] buffer = new char[bufferSize];

        int numChars;
        while ((numChars = in.read(buffer)) > 0) {
            out.write(buffer, 0, numChars);
        }
    }

    public static InputStream toInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }

    public static String toString(InputStream inputStream, String encoding) throws IOException {
        InputStreamReader reader = encoding == null ? new InputStreamReader(inputStream) : new InputStreamReader(inputStream, encoding);
        StringWriter writer = new StringWriter();
        copyStream(reader, writer);
        return writer.toString();
    }

    public static final String byteCountToDisplaySize(long size) {
        if (size / 1073741824L > 0L) {
            return asThreeSignificantCiffersString((double) size / 1.0737418E9F) + " GB";
        } else if (size / 1048576L > 0L) {
            return asThreeSignificantCiffersString((double) size / 1048576.0) + " MB";
        } else {
            return size / 1024L > 0L ? asThreeSignificantCiffersString((double) size / 1024.0) + " KB" : size + " bytes";
        }
    }

    private static final String asThreeSignificantCiffersString(double value) {
        if (value < 1.0) {
            return DECIMAL_FORMAT_1_3.format(value);
        } else if (value < 10.0) {
            return DECIMAL_FORMAT_1_2.format(value);
        } else {
            return value < 100.0 ? DECIMAL_FORMAT_2_1.format(value) : DECIMAL_FORMAT_3.format(value);
        }
    }

    public static void withWriter(File file, IWriterClosure closure) throws IOException {
        withWriter(new FileWriter(file), closure);
    }

    private static void withWriter(Writer writer, IWriterClosure closure) throws IOException {
        try {
            closure.execute(writer);
        } finally {
            close(writer);
        }
    }

    public static void withInputStream(File file, IInputStreamClosure closure) throws IOException {
        withInputStream(new FileInputStream(file), closure);
    }

    private static void withInputStream(InputStream inputStream, IInputStreamClosure closure) throws IOException {
        try {
            closure.execute(inputStream);
        } finally {
            close(inputStream);
        }
    }

    private static Thread createInputStreamConsumerThread(final InputStream inputStream) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //noinspection StatementWithEmptyBody
                    while (inputStream.read() > -1) {
                    }
                } catch (IOException var2) {
                    throw new RuntimeException(var2);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException var2) {
            }
        }
    }

    public static void executeCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        process.getOutputStream().close();
        createInputStreamConsumerThread(process.getInputStream());
        createInputStreamConsumerThread(process.getErrorStream());
    }

    static Pattern PAT_NOT_FS_SAFE = Pattern.compile("[^a-zA-Z0-9-_.]");

    public static String fsSafeString(String input) {
        return PAT_NOT_FS_SAFE.matcher(input).replaceAll("_");
    }

    public static InputStream tmpTee(InputStream input, String nameHint)  {
        String fsHint = fsSafeString(nameHint);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            copyStream(input, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayInputStream fwd = new ByteArrayInputStream(baos.toByteArray());
        try {
            File teeDir = new File(System.getProperty("java.io.tmpdir"), "tee");
            boolean mkdirs = teeDir.mkdirs();
            File branchFile = File.createTempFile("tee_", "_" + fsHint, teeDir);
            System.err.println("tee for " + nameHint + " into " + branchFile.getAbsolutePath());
            try (FileOutputStream branch = new FileOutputStream(branchFile)) {
                baos.writeTo(branch);
            }
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return fwd;
    }
}
