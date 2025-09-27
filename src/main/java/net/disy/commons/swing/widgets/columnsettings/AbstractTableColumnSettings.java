package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.smarttable.IObjectSelectionStrategy;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.IPreferredWidth;
import net.disy.commons.swing.smarttable.columnsettings.preferredwidth.TextPreferredWidth;

public abstract class AbstractTableColumnSettings<T> implements ITableColumnViewSettings<T> {
   private final int preferredWidth;
   private final Color background;
   private final Icon icon;

   public AbstractTableColumnSettings(int preferredColumnCount) {
      this(new TextPreferredWidth(preferredColumnCount));
   }

   public AbstractTableColumnSettings(int preferredColumnCount, Icon icon) {
      this(new TextPreferredWidth(preferredColumnCount), icon);
   }

   public AbstractTableColumnSettings(IPreferredWidth preferredWidth) {
      this(preferredWidth, (Icon)null);
   }

   public AbstractTableColumnSettings(IPreferredWidth preferredWidth, Icon icon) {
      this(preferredWidth, null, icon);
   }

   public AbstractTableColumnSettings(int preferredColumnCount, Color background) {
      this(new TextPreferredWidth(preferredColumnCount), background);
   }

   public AbstractTableColumnSettings(IPreferredWidth preferredWidth, Color background) {
      this(preferredWidth, background, null);
   }

   public AbstractTableColumnSettings(IPreferredWidth preferredWidth, Color background, Icon icon) {
      Ensure.ensureArgumentNotNull(preferredWidth);
      this.preferredWidth = preferredWidth.getPreferredWidth();
      this.background = background;
      this.icon = icon;
   }

   @Override
   public IObjectSelectionStrategy<T> getDoubleClickBehaviour() {
      return new NullDoubleClickBehaviour<>();
   }

   @Override
   public final int getPreferredWidth() {
      return this.preferredWidth;
   }

   @Override
   public final TableCellRenderer getRenderer() {
      TableCellRenderer renderer = this.getBaseRenderer();
      return this.background != null ? new BackgroundColorTableCellRendererDecorator(renderer, this.background) : renderer;
   }

   protected TableCellRenderer getBaseRenderer() {
      return new DefaultTableCellRenderer();
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public final Icon getIcon() {
      return this.icon;
   }

   @Override
   public String getToolTipText() {
      return null;
   }
}
