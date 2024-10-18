package net.disy.commons.swing.border;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;
import net.disy.commons.swing.color.SwingColors;

public class BorderUtilities {
   public static void attachDisableableTitledBorder(final JComponent component, final TitledBorder border) {
      component.setBorder(border);
      component.addPropertyChangeListener("enabled", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            BorderUtilities.setEnabled(border, component, component.isEnabled());
         }
      });
      setEnabled(border, component, component.isEnabled());
   }

   public static void setEnabled(TitledBorder border, JComponent ownerComponent, boolean enabled) {
      if (enabled) {
         border.setTitleColor(SwingColors.getTextAreaForegroundColor());
      } else {
         border.setTitleColor(SwingColors.getTextAreaInactiveForegroundColor());
      }

      ownerComponent.repaint();
   }
}
