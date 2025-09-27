package net.disy.commons.swing.fontchooser.view.combos;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;
import net.disy.commons.swing.fontchooser.util.FontUtilities;
import net.disy.commons.swing.fontchooser.view.DefaultFontDialogProperties;
import net.disy.commons.swing.fontchooser.view.IFontDialogProperties;

public class FontSizeCombo extends AbstractFontAdjustCombo {
   public FontSizeCombo(FontModel fontModel, IFontDialogProperties properties) {
      super(fontModel, properties);
   }

   public FontSizeCombo(FontModel fontModel) {
      this(fontModel, new DefaultFontDialogProperties());
   }

   @Override
   protected void updateFontModelView() {
      int fontSize = this.getFontModel().getFontSize();
      this.getComboBox().setSelectedItem(fontSize);
   }

   @Override
   protected JComboBox createComboBox() {
      JComboBox comboBox = new JComboBox<>(FontUtilities.getStandardSizes());
      Dimension preferredSize = comboBox.getPreferredSize();
      comboBox.setEditable(true);
      comboBox.setPreferredSize(new Dimension(preferredSize.width + 10, preferredSize.height));
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent event) {
            Object selectedItem = FontSizeCombo.this.getComboBox().getSelectedItem();
            int selectedSize;
            if (selectedItem instanceof Integer) {
               selectedSize = (Integer)selectedItem;
            } else {
               try {
                  selectedSize = Integer.parseInt(selectedItem.toString());
               } catch (NumberFormatException var5) {
                  FontSizeCombo.this.rejectUserInput();
                  return;
               }
            }

            if (FontUtilities.isValidFontSize(selectedSize)) {
               FontSizeCombo.this.getFontModel().setFontSize(selectedSize);
            } else {
               FontSizeCombo.this.rejectUserInput();
            }
         }
      });
      comboBox.setToolTipText(DisyCommonsSwingFontChooserMessages.getString("FontSizeCombo.Tooltip"));
      return comboBox;
   }
}
