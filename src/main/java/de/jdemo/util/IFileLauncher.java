package de.jdemo.util;

import java.io.File;

/**
 * @author Markus Gebhard
 */
public interface IFileLauncher {
  /**
   * Tries to open the given file by passing it to the operating system.
   * Note that this might not work properly on all systems.
   * (In fact it only works on windows systems at the moment.)
   */
  public void launch(File file) throws Exception;
}