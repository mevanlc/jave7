package de.jdemo.extensions.test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.exceptions.DemoMethodNotFoundException;
import de.jdemo.framework.state.DemoState;
import de.jdemo.framework.test.AbstractDemoCaseTest;

/**
 * @author Markus Gebhard
 */
public class SwingDemoCaseTest extends AbstractDemoCaseTest {
  private String someLookAndFeelClassName;

  @Override
  protected void setUp() throws Exception {
    someLookAndFeelClassName = UIManager.getCrossPlatformLookAndFeelClassName();
  }

  public void testTestPreconditions() {
    assertFalse(SwingDemoCase.isInstalledLookAndFeel("foo")); //$NON-NLS-1$
    assertFalse(SwingDemoCase.isInstalledLookAndFeel("bar")); //$NON-NLS-1$
  }

  public void testInstallLaF() {
    final LookAndFeelInfo[] originalInstalledLookAndFeels = UIManager.getInstalledLookAndFeels();
    final int lafCount = originalInstalledLookAndFeels.length;
    SwingDemoCase.installLookAndFeel("foo", someLookAndFeelClassName); //$NON-NLS-1$
    assertEquals(lafCount + 1, UIManager.getInstalledLookAndFeels().length);
    assertTrue(SwingDemoCase.isInstalledLookAndFeel("foo")); //$NON-NLS-1$
    UIManager.setInstalledLookAndFeels(originalInstalledLookAndFeels);
  }

  public void testInstallTwoLaFs() {
    final LookAndFeelInfo[] originalInstalledLookAndFeels = UIManager.getInstalledLookAndFeels();
    final int lafCount = originalInstalledLookAndFeels.length;
    SwingDemoCase.installLookAndFeel("foo", someLookAndFeelClassName); //$NON-NLS-1$
    SwingDemoCase.installLookAndFeel("bar", someLookAndFeelClassName); //$NON-NLS-1$
    assertEquals(lafCount + 2, UIManager.getInstalledLookAndFeels().length);
    assertTrue(SwingDemoCase.isInstalledLookAndFeel("bar")); //$NON-NLS-1$
    UIManager.setInstalledLookAndFeels(originalInstalledLookAndFeels);
  }

  @Override
  protected IDemoCase createEmptyDemo() {
    return new SwingDemoCase("demo") { //$NON-NLS-1$
    };
  }

  public void testMockImplementation() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoExit"); //$NON-NLS-1$
    mock.verify();
    assertSame(mock, mock.createRunnable(false).getDemo());
  }

  public void testRunnerExecutesCrashDemo() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoCrash"); //$NON-NLS-1$
    mock.setExpectedDemoCrashCalls(1);
    mock.setExpectedSetUpCalls(1);
    mock.setExpectedTearDownCalls(1);
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    assertEquals(DemoState.CRASHED, runner.getState());
    mock.verify();
    assertSame(mock.getException(), runner.getThrowable());
  }

  public void testRunnerExecutesShowWindowDemo() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoShowWindow"); //$NON-NLS-1$
    mock.setExpectedDemoShowWindowCalls(1);
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    assertEquals(DemoState.RUNNING, runner.getState());
    assertTrue(mock.getWindow().isVisible());
    assertSame(mock.getWindow(), mock.getRegisteredDemoWindow());
    mock.setExpectedSetUpCalls(1);
    mock.verify();
  }

  public void testDisposingWindowExitsDemo() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoShowWindow"); //$NON-NLS-1$
    mock.setExpectedSetUpCalls(1);
    mock.setExpectedDemoShowWindowCalls(1);
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    mock.getWindow().dispose();
    try {
      //Wait for the event to be processed - 100ms should be enough in most situations
      Thread.sleep(100);
    }
    catch (final InterruptedException exception) {
      //nothing to do
    }
    assertEquals(DemoState.FINISHED, runner.getState());
    assertNull(runner.getThrowable());
    mock.setExpectedTearDownCalls(1);
    mock.verify();
  }

  public void testCancelRunner() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoShowWindow"); //$NON-NLS-1$
    mock.setExpectedSetUpCalls(1);
    mock.setExpectedTearDownCalls(1);
    mock.setExpectedDemoShowWindowCalls(1);
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    runner.cancel();
    assertFalse(mock.getWindow().isVisible());
    try {
      //Wait for the event to be processed - 100ms should be enough in most situations
      Thread.sleep(100);
    }
    catch (final InterruptedException exception) {
      //nothing to do
    }
    assertEquals(DemoState.FINISHED, runner.getState());
    mock.verify();
  }

  public void testRunnerExecutesNonExistingDemo() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoNonExistingMethod"); //$NON-NLS-1$
    mock.setExpectedSetUpCalls(1);
    mock.setExpectedTearDownCalls(1);
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    assertEquals(DemoState.CRASHED, runner.getState());
    mock.verify();
    assertEquals(DemoMethodNotFoundException.class, runner.getThrowable().getClass());
  }

  public void testRunnerExecutesShowCreatedFrameDemo() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoShowCreatedFrame"); //$NON-NLS-1$
    mock.setExpectedSetUpCalls(1);
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    assertEquals(DemoState.RUNNING, runner.getState());
    mock.verify();
    assertNull(runner.getThrowable());
    assertTrue(mock.getRegisteredDemoWindow().isVisible());
    assertEquals(mock.getName(), ((JFrame) mock.getRegisteredDemoWindow()).getTitle());
    runner.cancel();
  }

  public void testSetFrameTitleDemo() {
    final MockSwingDemoCase mock = new MockSwingDemoCase("demoShowCreatedFrameWithGivenTitle"); //$NON-NLS-1$
    final IDemoCaseRunnable runner = mock.createRunnable(false);
    runner.run();
    assertEquals(mock.getTitle(), ((JFrame) mock.getRegisteredDemoWindow()).getTitle());
    runner.cancel();
  }

  @Override
  protected IDemoCase createCrashingDemo(final RuntimeException exception) {
    return new SwingDemoCase("demo") { //$NON-NLS-1$
      public void demo() {
        throw exception;
      }
    };
  }

  @Override
  protected IDemoCase createValidSelfExitingDemo() {
    return new SwingDemoCase("demo") { //$NON-NLS-1$
      public void demo() {
        show(new JButton("hello world"));
        exit();
      }
    };
  }
}