package de.jdemo.capture.stream;

import java.io.OutputStream;

/**
 * @author Markus Gebhard
 */
public class StringBufferOutputStream extends OutputStream {
  private StringBuffer buffer;

  public StringBufferOutputStream() {
    this(new StringBuffer());
  }

  public StringBufferOutputStream(StringBuffer buffer) {
    this.buffer = buffer;
  }

  public void write(int b) {
    buffer.append((char) b);
  }
  
  public void flush() {
    //nothing to do
  }

  public StringBuffer getStringBuffer(){
    return buffer;
  }
}