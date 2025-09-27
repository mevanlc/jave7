package net.disy.commons.swing.fontchooser.view.combos;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.AbstractFontAdjustComponent;
import net.disy.commons.swing.fontchooser.view.IFontDialogProperties;

public abstract class AbstractFontAdjustCombo extends AbstractFontAdjustComponent {
   private JComboBox comboBox;

   public AbstractFontAdjustCombo(FontModel fontModel, IFontDialogProperties properties) {
      super(fontModel, properties);
   }

   @Override
   protected final JComponent createContent() {
      return this.getComboBox();
   }

   protected abstract JComboBox createComboBox();

   protected final JComboBox getComboBox() {
      if (this.comboBox == null) {
         this.comboBox = this.createComboBox();
      }

      return this.comboBox;
   }

   @Override
   public final void setEnabled(boolean enabled) {
      this.getComboBox().setEnabled(enabled);
   }

   @Override
   public final boolean isEnabled() {
      return this.getComboBox().isEnabled();
   }
}
