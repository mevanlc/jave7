package net.disy.commons.swing.fontchooser.view;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.model.IFontFamilyNameFilter;
import net.disy.commons.swing.fontchooser.view.accessory.FontPreviewPanel;
import net.disy.commons.swing.fontchooser.view.accessory.IFontChooserAccessory;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class FontChooserPanel implements IFontModelView {
   private final FontFamilyPanel fontFamilyPanel;
   private final FontStylePanel fontStylePanel;
   private final FontSizePanel fontSizePanel;
   private IFontChooserAccessory accessory;
   private final FontModel fontModel;
   private JComponent content;

   public FontChooserPanel(FontModel fontModel) {
      this(fontModel, new DefaultFontDialogProperties());
   }

   public FontChooserPanel(FontModel fontModel, IFontDialogProperties properties) {
      Ensure.ensureArgumentNotNull(fontModel);
      Ensure.ensureArgumentNotNull(properties);
      this.fontModel = fontModel;
      this.fontFamilyPanel = new FontFamilyPanel(fontModel, properties);
      this.fontStylePanel = new FontStylePanel(fontModel, properties);
      this.fontSizePanel = new FontSizePanel(fontModel, properties);
      this.accessory = new FontPreviewPanel(fontModel, true);
   }

   @Override
   public FontModel getFontModel() {
      return this.fontModel;
   }

   private JComponent createContent() {
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      GridDialogLayoutData topData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      topData.setVerticalAlignment(GridAlignment.FILL);
      panel.add(this.fontFamilyPanel.getContent(), topData);
      panel.add(this.fontStylePanel.getContent(), topData);
      panel.add(this.fontSizePanel.getContent(), topData);
      GridDialogLayoutData bottomData = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      bottomData.setHorizontalSpan(3);
      panel.add(this.accessory.getContent(), bottomData);
      return panel;
   }

   public void setAccessory(IFontChooserAccessory accessory) {
      if (this.content != null) {
         throw new IllegalStateException("Exchanging the accessory after showing the panel is not allowed.");
      } else {
         accessory.setModel(this.fontModel);
         this.accessory = accessory;
      }
   }

   @Override
   public JComponent getContent() {
      if (this.content == null) {
         this.content = this.createContent();
      }

      return this.content;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.fontFamilyPanel.setEnabled(enabled);
      this.fontStylePanel.setEnabled(enabled);
      this.fontSizePanel.setEnabled(enabled);
      this.accessory.setEnabled(enabled);
   }

   @Override
   public boolean isEnabled() {
      return this.fontFamilyPanel.isEnabled();
   }

   public void setFontFamilyFilter(IFontFamilyNameFilter filter) {
      this.fontFamilyPanel.setFontFamilyNameFilter(filter);
   }
}
