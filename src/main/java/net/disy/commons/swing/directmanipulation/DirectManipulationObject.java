package net.disy.commons.swing.directmanipulation;

import net.disy.commons.core.util.Ensure;

@Deprecated
public class DirectManipulationObject {
   private final IManipulationMarkerProvider markerProvider;

   public DirectManipulationObject(IManipulationMarkerProvider markerProvider) {
      Ensure.ensureArgumentNotNull(markerProvider);
      this.markerProvider = markerProvider;
   }

   public IManipulationMarkerProvider getMarkerProvider() {
      return this.markerProvider;
   }
}
