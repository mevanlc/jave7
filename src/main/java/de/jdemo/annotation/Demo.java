package de.jdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Demo {
  public static final int DEFAULT_TIMEOUT_MILLIS = 60000;

  /**
   * Optionally specify <code>timeout</code> in milliseconds to cause a demo
   * method to fail if it takes longer than that number of milliseconds.
   */
  long timeout() default DEFAULT_TIMEOUT_MILLIS;

  /**
   * An optional description for the demo, that might be displayed in a demo
   * runner application. The format is HTML, as supported by the JTextPane
   * Swing component.
   */
  public String description() default ""; //$NON-NLS-1$

  /**
   * Optional markers for the categories, this demo belongs to. The markers
   * can be used by demo runners in order to filter all available demos to
   * certain categories.
   * <p>
   * There are no marker names defined by the framework.
   * <p>
   * Example: <code>{ &quot;test&quot;, &quot;screenshot&quot; }</code>
   * might indicate, that the demo is being used for testing and for
   * automatically generating screenshots.
   */
  public String[] categories() default {};
}