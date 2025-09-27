package net.disy.commons.swing.directmanipulation;

import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.ObjectUtilities;

@Deprecated
public class DirectManipulationObjectModel extends AbstractChangeableModel {
   private DirectManipulationObject directManipulationObject;

   public void setDirectManipulationObject(DirectManipulationObject directManipulationObject) {
      if (!ObjectUtilities.equals(this.directManipulationObject, directManipulationObject)) {
         this.directManipulationObject = directManipulationObject;
         this.fireChangeEvent();
      }
   }

   public DirectManipulationObject getDirectManipulationObject() {
      return this.directManipulationObject;
   }
}
