package de.jdemo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Version information for JDemo, automatically taken from the <code>version.properties</code> file created
 * by the ant build script when distributing JDemo.
 * 
 * @author Markus Gebhard
 */
public class Version {
  private static final String BUNDLE_NAME = Version.class.getPackage().getName() + ".version"; //$NON-NLS-1$
  private static ResourceBundle resourceBundle;

  private static String getString(final String key, final String fallback) {
    try {
      return getResourceBundle().getString(key);
    }
    catch (final MissingResourceException e) {
      return fallback;
    }
  }

  private static ResourceBundle getResourceBundle() {
    if (resourceBundle == null) {
      resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }
    return resourceBundle;
  }

  public static String getFullVersionNumber() {
    return getString("Version.version", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static String getBuildDate() {
    return getString("Version.buildDate", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }
}