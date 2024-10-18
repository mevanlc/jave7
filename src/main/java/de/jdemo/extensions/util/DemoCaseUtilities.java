package de.jdemo.extensions.util;

public class DemoCaseUtilities {

  private DemoCaseUtilities() {
    //no instance available
  }
  
  private final static String[] POPULAR_DEMO_CASE_BASE_CLASS_NAMES =
    new String[] {
      "de.jdemo.extensions.SwingDemoCase", //$NON-NLS-1$
      "de.jdemo.extensions.AwtDemoCase", //$NON-NLS-1$
      "de.jdemo.extensions.SwtDemoCase", //$NON-NLS-1$
      "de.jdemo.framework.PlainDemoCase", //$NON-NLS-1$
      "de.jdemo.framework.DemoCase" }; //$NON-NLS-1$

  
  public static String[] getPopularDemoCaseBaseClassNames() {
    return POPULAR_DEMO_CASE_BASE_CLASS_NAMES;
  }
}