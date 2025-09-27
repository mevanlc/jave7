package de.jave.jave.ascii3d;

import de.jave.jave.ascii3d.example.I3dExample;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class Ascii3dExampleObjectUi extends AbstractObjectUi<I3dExample> {
   public String getLabel(I3dExample value) {
      return value.getTitle();
   }
}
