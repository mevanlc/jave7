package net.disy.commons.swing.fontchooser.table;

import java.awt.Component;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.view.DefaultFontChooserDialogFactory;
import net.disy.commons.swing.fontchooser.view.FontChooserButton;
import net.disy.commons.swing.fontchooser.view.IFontChooserDialogFactory;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.columnsettings.NullDoubleClickBehaviour;

public class FontTableColumnSettings implements ITableColumnViewSettings<Font> {
   private final IFontChooserDialogFactory dialogFactory;

   public FontTableColumnSettings() {
      this(DefaultFontChooserDialogFactory.getInstance());
   }

   public FontTableColumnSettings(IFontChooserDialogFactory dialogFactory) {
      Ensure.ensureArgumentNotNull(dialogFactory);
      this.dialogFactory = dialogFactory;
   }

   @Override
   public TableCellEditor getEditor() {
      return new FontCellEditor(this.dialogFactory);
   }

   @Override
   public TableCellRenderer getRenderer() {
      return new TableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            FontChooserButton fontChooserButton = FontCellEditor.createFontChooserButton();
            fontChooserButton.setEnabled(table.isEnabled() && table.getModel().isCellEditable(row, column));
            return fontChooserButton.getContent();
         }
      };
   }

   @Override
   public boolean isResizable() {
      return false;
   }

   @Override
   public int getPreferredWidth() {
      return FontCellEditor.createFontChooserButton().getContent().getPreferredSize().width;
   }

   @Override
   public IObjectSelectionStrategy<Font> getDoubleClickBehaviour() {
      return new NullDoubleClickBehaviour<>();
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getToolTipText() {
      return null;
   }
}
