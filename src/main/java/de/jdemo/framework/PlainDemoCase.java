package de.jdemo.framework;

/**
 * Demo case base class for demos having output not supported by the other demo case base classes.
 * Plain demo cases are useful when you want to demonstrate output to an LPT/COM/USB port or to a
 * soundcard for example.
 * 
 * @author Markus Gebhard
 */
public abstract class PlainDemoCase extends AbstractDemoCase {

  public IDemoCaseRunnable createRunnable(final boolean allowExternalLaunches) {
    final PlainDemoCase clone = (PlainDemoCase) this.clone();
    if (allowExternalLaunches) {
      return new PlainDemoCaseRunnable(clone);
    }
    return new NullDemoCaseRunnable(clone);
  }
}