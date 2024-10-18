package de.jdemo.extensions.test;

import javax.swing.JWindow;

import junit.framework.Assert;
import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.IDemoCase;

public class MockSwingDemoCase extends SwingDemoCase {
  private final String title = "title"; //$NON-NLS-1$
  private final RuntimeException exception = new RuntimeException();
  private JWindow window;

  private int expectedDemoExitCalls = 0;
  private int expectedDemoCrashCalls = 0;
  private int expectedDemoShowWindowCalls = 0;
  private int expectedSetUpCalls = 0;
  private int expectedTearDownCalls = 0;
  private int demoShowWindowCalls = 0;
  private int demoExitCalls = 0;
  private int demoCrashCalls = 0;
  private int setUpCalls = 0;
  private int tearDownCalls = 0;

  public MockSwingDemoCase(final String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    ++setUpCalls;
    window = new JWindow();
  }

  @Override
  protected void tearDown() throws Exception {
    ++tearDownCalls;
  }

  @Override
  public IDemoCase getClone() {
    return this;
  }

  public void demoShowWindow() {
    ++demoShowWindowCalls;
    show(window);
  }

  public void demoShowCreatedFrame() {
    show(createJFrame());
  }

  public void demoShowCreatedFrameWithGivenTitle() {
    setFrameTitle(title);
    show(createJFrame());
  }

  public void demoExit() {
    ++demoExitCalls;
    exit();
  }

  public void demoCrash() {
    ++demoCrashCalls;
    exception.fillInStackTrace();
    throw exception;
  }

  public void verify() {
    Assert.assertEquals(expectedDemoCrashCalls, demoCrashCalls);
    Assert.assertEquals(expectedDemoExitCalls, demoExitCalls);
    Assert.assertEquals(expectedDemoShowWindowCalls, demoShowWindowCalls);
    Assert.assertEquals(expectedSetUpCalls, setUpCalls);
    Assert.assertEquals(expectedTearDownCalls, tearDownCalls);
  }

  public void setExpectedDemoCrashCalls(final int expectedDemoCrashCalls) {
    this.expectedDemoCrashCalls = expectedDemoCrashCalls;
  }

  public void setExpectedDemoExitCalls(final int expectedDemoExitCalls) {
    this.expectedDemoExitCalls = expectedDemoExitCalls;
  }

  public void setExpectedDemoShowWindowCalls(final int expectedDemoShowWindowCalls) {
    this.expectedDemoShowWindowCalls = expectedDemoShowWindowCalls;
  }

  public void setExpectedSetUpCalls(final int expectedSetUpCalls) {
    this.expectedSetUpCalls = expectedSetUpCalls;
  }

  public void setExpectedTearDownCalls(final int expectedTearDownCalls) {
    this.expectedTearDownCalls = expectedTearDownCalls;
  }

  public JWindow getWindow() {
    return window;
  }

  public Exception getException() {
    return exception;
  }

  public String getTitle() {
    return title;
  }
}