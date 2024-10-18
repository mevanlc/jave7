package de.jdemo.capture.stream;

import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;

import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.state.IDemoStateChangeEvent;
import de.jdemo.framework.state.IDemoStateChangeListener;
import de.jdemo.framework.util.DemoUtilities;

/**
 * @author Markus Gebhard
 * @deprecated As of Jul 22, 2004 (Markus Gebhard) System stream capture is no longer supported
 */
@Deprecated
public class SystemStreamCapture {

  public static void recordSystemStream(
      final DemoIdentifier demoId,
      final Writer writer,
      final SystemStreamType type) throws Throwable {
    final IDemoCase demo = DemoUtilities.createDemo(demoId);
    recordSystemStream(demo, writer, type);
  }

  public static void recordSystemStream(final IDemoCase demo, final Writer writer, final SystemStreamType type)
      throws Throwable {
    final IDemoCaseRunnable runner = demo.createRunnable(false);

    type.setPrintStream(new PrintStream(new WriterOutputStream(writer)));
    runner.addDemoStateChangeListener(new IDemoStateChangeListener() {
      public void demoStateChanged(final IDemoStateChangeEvent event) {
        if (event.getNewState().equals(DemoState.FINISHED)) {
          type.resetPrintStream();
        }
        else if (event.getNewState().equals(DemoState.CRASHED)) {
          type.resetPrintStream();
        }
      }
    });

    final Thread thread = new Thread(runner);
    thread.start();
    try {
      thread.join();
    }
    catch (final InterruptedException e) {
      //nothing to do
    }
    if (runner.getState().equals(DemoState.CRASHED)) {
      throw runner.getThrowable();
    }
  }

  public static String recordSystemStreamToString(final DemoIdentifier demoId, final SystemStreamType type)
      throws Throwable {

    final StringWriter writer = new StringWriter();
    recordSystemStream(demoId, writer, type);
    return writer.getBuffer().toString();
  }
}