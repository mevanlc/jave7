package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.EventObject;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;

public abstract class AbstractDelegatingCellEditor extends AbstractCellEditor implements ICellEditor {
   private JComponent editorComponent;
   private EditorDelegate delegate;

   private void initialize() {
      if (this.editorComponent == null) {
         this.editorComponent = this.createEditorComponent();
         this.delegate = this.createDelegate(this.editorComponent);
         this.attachEditorActionListener(this.editorComponent, this.delegate);
      }
   }

   protected void attachEditorActionListener(JComponent editor, ActionListener actionListener) {
      try {
         Method addActionListener = editor.getClass().getMethod("addActionListener", ActionListener.class);
         addActionListener.invoke(editor, actionListener);
      } catch (Exception var4) {
      }
   }

   @Override
   public final Object getCellEditorValue() {
      this.initialize();
      return this.delegate.getCellEditorValue();
   }

   @Override
   public final boolean isCellEditable(EventObject anEvent) {
      this.initialize();
      return this.delegate.isCellEditable(anEvent);
   }

   @Override
   public final boolean shouldSelectCell(EventObject anEvent) {
      return true;
   }

   @Override
   public final boolean stopCellEditing() {
      this.initialize();
      return this.delegate.stopCellEditing();
   }

   @Override
   public final void cancelCellEditing() {
      this.initialize();
      this.delegate.cancelCellEditing();
   }

   @Override
   public final Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
      this.initialize();
      this.delegate.setValue(value);
      return this.editorComponent;
   }

   @Override
   public final Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.initialize();
      this.delegate.setValue(value);
      return this.editorComponent;
   }

   protected JComponent getEditorComponent() {
      this.initialize();
      return this.editorComponent;
   }

   protected abstract EditorDelegate createDelegate(JComponent var1);

   protected abstract JComponent createEditorComponent();
}
