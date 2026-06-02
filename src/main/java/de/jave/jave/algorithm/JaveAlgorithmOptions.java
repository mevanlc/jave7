package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import java.util.Vector;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public abstract class JaveAlgorithmOptions {
   protected Vector algorithmOptionsListeners;

   public abstract JaveAlgorithmOptionsPanel getPanel(FontModel var1);

   public abstract void adjustTo(JaveSelection var1);

   public void addAlgorithmOptionsListener(JaveAlgorithmOptionsListener listener) {
      if (this.algorithmOptionsListeners == null) {
         this.algorithmOptionsListeners = new Vector();
      }

      this.algorithmOptionsListeners.addElement(listener);
   }

   public void removeAlgorithmOptionsListener(JaveAlgorithmOptionsListener listener) {
      this.algorithmOptionsListeners.removeElement(listener);
   }

   public void fireAlgorithmOptionsChangeEvent() {
      if (this.algorithmOptionsListeners != null) {
         for (int i = 0; i < this.algorithmOptionsListeners.size(); i++) {
            ((JaveAlgorithmOptionsListener)this.algorithmOptionsListeners.elementAt(i)).algorithmOptionsChanged();
         }
      }
   }
}
