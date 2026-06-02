package de.jave.gui.layout;

import java.awt.Dimension;
import javax.swing.JComponent;
import net.dizzy.commons.swing.layout.util.LayoutUtilities;

public class Gap extends JComponent {
   private final Dimension preferredSize;

   public Gap(int preferredWidth, int preferredHeight) {
      this(new Dimension(preferredWidth, preferredHeight));
   }

   public Gap(Dimension preferredSize) {
      this.preferredSize = new Dimension(LayoutUtilities.getDpiAdjusted(preferredSize.width), LayoutUtilities.getDpiAdjusted(preferredSize.height));
   }

   public Gap() {
      this(0, 0);
   }

   @Override
   public Dimension getPreferredSize() {
      return this.preferredSize == null ? super.getPreferredSize() : this.preferredSize;
   }
}
