package net.disy.commons.swing.fontchooser.table;

import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.font.FontFactory;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.DefaultFontChooserDialogFactory;
import net.disy.commons.swing.fontchooser.view.FontChooserButton;
import net.disy.commons.swing.fontchooser.view.IFontChooserDialogFactory;
import net.disy.commons.swing.fontchooser.view.button.IconFontChooserButtonConfiguration;
import net.disy.commons.swing.smarttable.celleditors.AbstractDelegatingCellEditor;
import net.disy.commons.swing.smarttable.celleditors.EditorDelegate;

public class FontCellEditor extends AbstractDelegatingCellEditor {
   private FontModel fontModel;
   private FontChooserButton fontChooserButton;
   private final IFontChooserDialogFactory dialogFactory;

   public FontCellEditor(IFontChooserDialogFactory dialogFactory) {
      Ensure.ensureArgumentNotNull(dialogFactory);
      this.dialogFactory = dialogFactory;
   }

   @Override
   protected EditorDelegate createDelegate(JComponent editorComponent) {
      return new EditorDelegate(this, 1) {
         @Override
         public void setValue(Object value) {
            FontCellEditor.this.fontModel.setFont(FontFactory.createFontDescription((Font)value));
         }

         @Override
         public Object getCellEditorValue() {
            return FontCellEditor.this.fontModel.getFont();
         }
      };
   }

   @Override
   protected void attachEditorActionListener(JComponent editor, ActionListener actionListener) {
      this.fontChooserButton.addActionListener(actionListener);
   }

   @Override
   protected JComponent createEditorComponent() {
      this.fontModel = new FontModel();
      this.fontChooserButton = createFontChooserButton(this.fontModel, this.dialogFactory);
      return this.fontChooserButton.getContent();
   }

   private static FontChooserButton createFontChooserButton(FontModel fontModel, IFontChooserDialogFactory dialogFactory) {
      return new FontChooserButton(fontModel, new IconFontChooserButtonConfiguration(), dialogFactory);
   }

   public static FontChooserButton createFontChooserButton() {
      return createFontChooserButton(new FontModel(), DefaultFontChooserDialogFactory.getInstance());
   }
}
