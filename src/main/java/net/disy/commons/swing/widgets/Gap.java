package net.disy.commons.swing.component;

import java.awt.Dimension;
import javax.swing.JComponent;

public class Gap extends JComponent {
   private Dimension preferredSize;

   public Gap(int preferredWidth, int preferredHeight) {
      this(new Dimension(preferredWidth, preferredHeight));
   }

   public Gap(Dimension preferredSize) {
      this.preferredSize = preferredSize;
   }

   public Gap() {
   }

   @Override
   public Dimension getPreferredSize() {
      return this.preferredSize == null ? super.getPreferredSize() : this.preferredSize;
   }
}
