package net.disy.commons.swing.smarttable.celleditors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

public class EditorDelegate implements ActionListener, ItemListener, Serializable {
   private static final int CLICK_COUNT_TO_START = 2;
   private final AbstractDelegatingCellEditor editor;
   private final int clickCountToStart;
   private Object value;

   public EditorDelegate(AbstractDelegatingCellEditor editor) {
      this(editor, 2);
   }

   public EditorDelegate(AbstractDelegatingCellEditor editor, int clickCountToStart) {
      this.editor = editor;
      this.clickCountToStart = clickCountToStart;
   }

   public Object getCellEditorValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public boolean isCellEditable(EventObject anEvent) {
      return anEvent instanceof MouseEvent ? ((MouseEvent)anEvent).getClickCount() >= this.clickCountToStart : true;
   }

   public boolean stopCellEditing() {
      this.editor.fireEditingStopped();
      return true;
   }

   public void cancelCellEditing() {
      this.editor.fireEditingCanceled();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      this.editor.stopCellEditing();
   }

   @Override
   public void itemStateChanged(ItemEvent e) {
      this.editor.stopCellEditing();
   }
}
