package de.jdemo.framework;

import java.io.File;
import java.io.IOException;

import de.jdemo.framework.exceptions.DemoExecutionFailedError;
import de.jdemo.util.FileLauncher;
import de.jdemo.util.IFileLauncher;

/**
 * Abstract superclass for all text/file based demos or extensions.
 * @see de.jdemo.framework.DemoSuite
 */
public abstract class DemoCase extends AbstractDemoCase {
  private IFileLauncher fileLauncher = new FileLauncher();
  private ITextLauncher textLauncher = new DefaultTextLauncher();

  /**
   * No-arg constructor to enable serialization. This method is not intended
   * to be used by mere mortals without calling setName().
   */
  public DemoCase() {
    super();
  }

  /**
   * Constructs a demo case with the given name.
   */
  public DemoCase(final String name) {
    super(name);
  }

  @Override
  public IDemoCaseRunnable createRunnable(final boolean allowExternalLaunches) {
    final DemoCase clone = (DemoCase) this.getClone();
    if (!allowExternalLaunches) {
      clone.setFileLauncher(new IFileLauncher() {
        @Override
        public void launch(final File file) throws Exception {
          //nothing to do
        }
      });
      clone.setTextLauncher(new ITextLauncher() {

        @Override
        public void launch(final CharSequence text) {
          //nothing to do  
        }
      });
    }
    return new DemoCaseRunnable(clone);
  }

  /**
   * Creates a temporary file using the specified suffig (e.g.
   * &quot;.txt&quot;) that will be deleted when the virtual machine
   * terminates.
   * 
   * @see #show(File)
   * @param suffix
   *            the filename suffix for the temporary file to be created.
   * @return an empty temporary file having the specified suffix.
   */
  protected final File createTempFile(final String suffix) throws IOException {
    final File file = File.createTempFile("jdemofile", suffix); //$NON-NLS-1$
    file.deleteOnExit();
    return file;
  }

  /**
   * Tries to open the given file by passing it to the operating system. Note
   * that this might not work properly on all systems.
   * 
   * @see #createTempFile(String)
   */
  protected final void show(final File file) {
    assertNotNull("The file to show must not be null.", file);
    getRunnable().showCalled();
    if (!file.exists()) {
      throw new DemoExecutionFailedError("The file to launch does not exist: " + file.getAbsolutePath());
    }
    try {
      fileLauncher.launch(file);
    }
    catch (final Exception e) {
      throw new RuntimeException("Unable to launch file '" + file.getAbsolutePath() + "'", e);
    }
  }

  /**
   * Shows the given text by using the demos text launcher. The default
   * launcher prints out the text to System.out, but each DemoRunner
   * application is free to set its own text launcher.
   * 
   * @see #setTextLauncher(ITextLauncher)
   */
  protected final void show(final CharSequence text) {
    assertNotNull("The text to show must not be null.", text);
    getRunnable().showCalled();
    textLauncher.launch(text);
  }

  /**
   * Sets the launcher to show files with. This method is intended to be only
   * called by the demorunner, never by the demo itself.
   */
  public void setFileLauncher(final IFileLauncher fileLauncher) {
    assertNotNull("The file launcher must not be null.", fileLauncher);
    this.fileLauncher = fileLauncher;
  }

  /**
   * Sets the launcher to show texts with. This method is intended to be only
   * called by the demorunner, never by the demo itself.
   */
  public void setTextLauncher(final ITextLauncher textLauncher) {
    assertNotNull("The text launcher must not be null.", textLauncher);
    this.textLauncher = textLauncher;
  }
}