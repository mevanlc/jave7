package de.jdemo.framework;

/**
 * @author Markus Gebhard
 */
public class DefaultTextLauncher implements ITextLauncher {

  public void launch(CharSequence text) {
    System.out.println(text);
  }

}