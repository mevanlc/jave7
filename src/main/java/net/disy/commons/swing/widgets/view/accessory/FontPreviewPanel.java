package net.disy.commons.swing.fontchooser.view.accessory;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JTextField;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.border.TitledPanel;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class FontPreviewPanel implements IFontChooserAccessory {
   private final IChangeListener changeListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         FontPreviewPanel.this.updateFont();
      }
   };
   private FontModel model;
   private JTextField textField;
   private final JComponent content;

   public FontPreviewPanel() {
      this(new FontModel(), false);
   }

   public FontPreviewPanel(boolean defaultBorderVisible) {
      this(new FontModel(), defaultBorderVisible);
   }

   public FontPreviewPanel(FontModel model) {
      this(model, false);
   }

   public FontPreviewPanel(FontModel model, boolean defaultBorderVisible) {
      this.content = this.createContent(defaultBorderVisible);
      this.setModel(model);
   }

   @Override
   public void setModel(FontModel model) {
      Ensure.ensureArgumentNotNull(model);
      if (this.model != null) {
         this.model.removeChangeListener(this.changeListener);
      }

      this.model = model;
      model.addChangeListener(this.changeListener);
      this.updateFont();
   }

   private JComponent createContent(boolean defaultBorderVisible) {
      this.textField = new JTextField(DisyCommonsSwingFontChooserMessages.getString("FontPreviewPanel.previewText")) {
         @Override
         public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.height = 36;
            return size;
         }
      };
      return defaultBorderVisible
         ? new TitledPanel(DisyCommonsSwingFontChooserMessages.getString("FontPreviewPanel.sample"), this.textField)
         : this.textField;
   }

   protected void updateFont() {
      this.textField.setFont(this.model.getFont());
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.textField.setEnabled(enabled);
      this.content.setEnabled(enabled);
   }
}
