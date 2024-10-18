package net.disy.commons.swing.fontchooser.view.fixedwidth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;
import net.disy.commons.swing.fontchooser.view.FontChooserPanel;
import net.disy.commons.swing.fontchooser.view.accessory.FontPreviewPanel;
import net.disy.commons.swing.fontchooser.view.accessory.IFontChooserAccessory;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class FixedWidthOptionFontChooserAccessory implements IFontChooserAccessory {
   private final JCheckBox cbFixedWidthOnly;
   private final FontPreviewPanel previewPanel;
   private final JComponent content;
   private final FontChooserPanel fontChooserPanel;
   private final JCheckBox cbNonSymbolFontsOnly;

   public FixedWidthOptionFontChooserAccessory(FontChooserPanel fontChooserPanel) {
      this.fontChooserPanel = fontChooserPanel;
      this.cbFixedWidthOnly = new JCheckBox(DisyCommonsSwingFontChooserMessages.getString("FixedWidthOptionFontChooserAccessory.ShowFixedWidthFontsOnly"), true);
      this.cbNonSymbolFontsOnly = new JCheckBox(DisyCommonsSwingFontChooserMessages.getString("FixedWidthOptionFontChooserAccessory.HideSymbolFonts"), true);
      this.cbFixedWidthOnly.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FixedWidthOptionFontChooserAccessory.this.updateFontFilter();
         }
      });
      this.cbNonSymbolFontsOnly.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FixedWidthOptionFontChooserAccessory.this.updateFontFilter();
         }
      });
      this.updateFontFilter();
      this.previewPanel = new FontPreviewPanel(fontChooserPanel.getFontModel());
      JPanel checkBoxPanel = new JPanel(new GridDialogLayout(1, false));
      checkBoxPanel.add(this.cbFixedWidthOnly);
      checkBoxPanel.add(this.cbNonSymbolFontsOnly);
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(this.previewPanel.getContent());
      panel.add(checkBoxPanel);
      this.content = panel;
   }

   private void updateFontFilter() {
      this.fontChooserPanel.setFontFamilyFilter(new FixedWidthFontFamilyNameFilter(this.cbFixedWidthOnly.isSelected(), this.cbNonSymbolFontsOnly.isSelected()));
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.cbFixedWidthOnly.setEnabled(enabled);
      this.previewPanel.setEnabled(enabled);
   }

   @Override
   public void setModel(FontModel model) {
      this.previewPanel.setModel(model);
   }
}
