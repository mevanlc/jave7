package de.jdemo.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * @author Markus Gebhard
 */
public class FileLauncher implements IFileLauncher {

  /**
   * Tries to open the given file. Uses the java.awt.Desktop class, which might not support
   * opening files on some operating systems. 
   *
   */
  @Override
  public void launch(final File file) throws IOException {
    final Desktop desktop = Desktop.getDesktop();
    desktop.open(file);
  }
}