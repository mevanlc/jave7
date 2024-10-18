package de.jdemo.capture.stream;

import java.io.PrintStream;

/**
 * Typesafe enumeration for <code>System.err</code> and <code>System.out</code>.
 * 
 * @author Markus Gebhard
 * @deprecated As of Jul 22, 2004 (Markus Gebhard) System stream capture is no longer supported
 */
@Deprecated
public abstract class SystemStreamType {
  private PrintStream previousStream;

  public final static SystemStreamType ERR = new SystemStreamType() {
    protected void set(PrintStream stream) {
      System.setErr(stream);
    }
    protected PrintStream get() {
      return System.err;
    }

    public String toString() {
      return "System.err"; //$NON-NLS-1$
    }
  };

  public final static SystemStreamType OUT = new SystemStreamType() {
    protected void set(PrintStream stream) {
      System.setOut(stream);
    }
    protected PrintStream get() {
      return System.out;
    }
    public String toString() {
      return "System.out"; //$NON-NLS-1$
    }
  };

  public synchronized void resetPrintStream() {
    if (previousStream == null) {
      throw new IllegalStateException("Unable to reset PrintStream when none has been set."); //$NON-NLS-1$
    }
    flush();
    set(previousStream);
    previousStream = null;
  }

  public synchronized void setPrintStream(PrintStream stream) {
    if (previousStream != null) {
      throw new IllegalStateException("Unable to set new PrintStream when previous one has not been reset."); //$NON-NLS-1$
    }
    previousStream = get();
    set(stream);
  }

  private void flush() {
    get().flush();
  }

  protected abstract void set(PrintStream stream);

  protected abstract PrintStream get();

  public static SystemStreamType getByName(String streamName) {
    if ("out".equalsIgnoreCase(streamName)) {
      return SystemStreamType.OUT;
    }
    if ("err".equalsIgnoreCase(streamName)) {
      return SystemStreamType.ERR;
    }
    return null;
  }
}