package de.jdemo.capture.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Markus Gebhard
 */
public class WriterOutputStream extends OutputStream {
  private Writer writer;
  
  public WriterOutputStream(Writer writer){
    this.writer = writer;
  }

  public void write(int b) throws IOException {
    writer.write((char) b);
  }
}