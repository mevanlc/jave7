package net.disy.commons.swing.mousecursor.util;

import java.awt.Cursor;
import javax.swing.JComponent;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class CursorUtilities {
   public static void attachWaitCursorForBusyModel(final JComponent component, final BooleanModel busyModel) {
      final Cursor originalCursor = component.getCursor();
      busyModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            CursorUtilities.updateCursor(component, busyModel, originalCursor);
         }
      });
      updateCursor(component, busyModel, originalCursor);
   }

   private static void updateCursor(JComponent component, BooleanModel busyModel, Cursor originalCursor) {
      if (busyModel.getValue()) {
         component.setCursor(CursorProvider.getInstance().getCursor(CursorId.WAIT));
      } else {
         component.setCursor(originalCursor);
      }
   }
}
