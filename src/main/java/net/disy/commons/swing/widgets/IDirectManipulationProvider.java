package net.disy.commons.swing.directmanipulation;

import java.awt.Point;
import javax.swing.JComponent;

@Deprecated
public interface IDirectManipulationProvider {
   DirectManipulationObject getDirectManipulationObject(Point var1);

   void handleContextMenuInvoked(JComponent var1, Point var2, DirectManipulationObject var3);

   void handleDoubleClick(JComponent var1, Point var2, DirectManipulationObject var3);
}
