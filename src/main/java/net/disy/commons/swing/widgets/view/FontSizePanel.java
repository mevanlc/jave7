package net.disy.commons.swing.fontchooser.view;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;
import net.disy.commons.swing.fontchooser.util.FontUtilities;

public class FontSizePanel extends AbstractFontAdjustListPanel {
   public FontSizePanel(FontModel fontModel) {
      this(fontModel, new DefaultFontDialogProperties());
   }

   public FontSizePanel(FontModel fontModel, IFontDialogProperties properties) {
      super(fontModel, properties);
   }

   @Override
   protected String getTitle() {
      return DisyCommonsSwingFontChooserMessages.getString("FontSizePanel.size");
   }

   @Override
   protected JList createListComponent() {
      return new JList<>(FontUtilities.getStandardSizes());
   }

   @Override
   protected void updateFontModelView() {
      int fontSize = this.getFontModel().getFontSize();
      this.getTextField().setText(String.valueOf(fontSize));
      this.getList().setSelectedValue(fontSize, true);
   }

   @Override
   protected void attachListListener() {
      this.getList().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            Integer selectedValue = (Integer)FontSizePanel.this.getList().getSelectedValue();
            if (selectedValue != null) {
               int fontSize = selectedValue;
               if (FontUtilities.isValidFontSize(fontSize)) {
                  FontSizePanel.this.getFontModel().setFontSize(fontSize);
               } else {
                  FontSizePanel.this.rejectUserInput();
               }
            }
         }
      });
   }
}
