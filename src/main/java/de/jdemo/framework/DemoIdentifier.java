package de.jdemo.framework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Markus Gebhard
 */
public class DemoIdentifier {
  private static Pattern DEMO_IDENTIFIER_PATTERN = Pattern.compile("([a-zA-Z0-9_\\.\\$]+):(([a-zA-Z0-9_]+)|(warning))"); //$NON-NLS-1$
  private String className;
  private String methodName;

  public static boolean isValidIdentifier(String string) {
    return DEMO_IDENTIFIER_PATTERN.matcher(string).matches();
  }

  /** Creates a new identifier for a democase from the class name and the method name.
   * @see #DemoIdentifier(String) */
  public DemoIdentifier(String className, String methodName) {
    this(className + ":" + methodName); //$NON-NLS-1$
  }

  /** Creates a new identifier for a democase from a textual description.
   * Examples for valid textual description:
   *  <ul>
   *    <li><code>foo.mypackage.MyClass:demoBar</code>
   *    <li><code>de.jdemo.MyClass$1:demoMethod</code>
   *  </ul>
   * @see #DemoIdentifier(String, String) */
  public DemoIdentifier(String demoIdentifierName) {
    Matcher matcher = DEMO_IDENTIFIER_PATTERN.matcher(demoIdentifierName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("not a valid demo identifier: '" + demoIdentifierName + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    this.className = matcher.group(1);
    this.methodName = matcher.group(2);
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  public String toString() {
    return getIdentifierName();
  }

  public String getIdentifierName() {
    return getClassName() + ":" + getMethodName(); //$NON-NLS-1$
  }

  public int hashCode() {
    return getIdentifierName().hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof DemoIdentifier)) {
      return false;
    }
    DemoIdentifier other = (DemoIdentifier) obj;
    return other.getIdentifierName().equals(getIdentifierName());
  }
}