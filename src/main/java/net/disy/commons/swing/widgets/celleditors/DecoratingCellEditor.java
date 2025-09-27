package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IDecorator;

public class DecoratingCellEditor implements ICellEditor {
   private final ICellEditor cellEditor;
   private final IDecorator<Component> componentDecorator;

   public DecoratingCellEditor(ICellEditor cellEditor, IDecorator<Component> componentDecorator) {
      Ensure.ensureArgumentNotNull(cellEditor);
      Ensure.ensureArgumentNotNull(componentDecorator);
      this.cellEditor = cellEditor;
      this.componentDecorator = componentDecorator;
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      Component editComponent = this.cellEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
      return this.componentDecorator.decorate(editComponent);
   }

   @Override
   public void addCellEditorListener(CellEditorListener l) {
      this.cellEditor.addCellEditorListener(l);
   }

   @Override
   public void cancelCellEditing() {
      this.cellEditor.cancelCellEditing();
   }

   @Override
   public Object getCellEditorValue() {
      return this.cellEditor.getCellEditorValue();
   }

   @Override
   public boolean isCellEditable(EventObject anEvent) {
      return this.cellEditor.isCellEditable(anEvent);
   }

   @Override
   public void removeCellEditorListener(CellEditorListener l) {
      this.cellEditor.removeCellEditorListener(l);
   }

   @Override
   public boolean shouldSelectCell(EventObject anEvent) {
      return this.cellEditor.shouldSelectCell(anEvent);
   }

   @Override
   public boolean stopCellEditing() {
      return this.cellEditor.stopCellEditing();
   }

   @Override
   public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
      Component editComponent = this.cellEditor.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
      return this.componentDecorator.decorate(editComponent);
   }
}
