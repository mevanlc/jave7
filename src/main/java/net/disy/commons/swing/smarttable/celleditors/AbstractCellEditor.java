package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public class AbstractCellEditor implements CellEditor {
   protected EventListenerList listenerList = new EventListenerList();

   @Override
   public Object getCellEditorValue() {
      return null;
   }

   @Override
   public boolean isCellEditable(EventObject e) {
      return true;
   }

   @Override
   public boolean shouldSelectCell(EventObject anEvent) {
      return false;
   }

   @Override
   public boolean stopCellEditing() {
      return true;
   }

   @Override
   public void cancelCellEditing() {
   }

   protected final void cancelEditOnFocusLost(final Component component) {
      component.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent e) {
            component.setVisible(false);
            AbstractCellEditor.this.fireEditingCanceled();
         }
      });
   }

   @Override
   public void addCellEditorListener(CellEditorListener l) {
      this.listenerList.add(CellEditorListener.class, l);
   }

   @Override
   public void removeCellEditorListener(CellEditorListener l) {
      this.listenerList.remove(CellEditorListener.class, l);
   }

   protected void fireEditingStopped() {
      Object[] listeners = this.listenerList.getListenerList();

      for (int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == CellEditorListener.class) {
            ((CellEditorListener)listeners[i + 1]).editingStopped(new ChangeEvent(this));
         }
      }
   }

   protected void fireEditingCanceled() {
      Object[] listeners = this.listenerList.getListenerList();

      for (int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == CellEditorListener.class) {
            ((CellEditorListener)listeners[i + 1]).editingCanceled(new ChangeEvent(this));
         }
      }
   }
}
