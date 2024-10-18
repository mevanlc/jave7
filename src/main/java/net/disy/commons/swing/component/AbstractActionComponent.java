package net.disy.commons.swing.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.IClosure;

public abstract class AbstractActionComponent {
   private final ListenerList<ActionListener> actionListeners = new ListenerList<>();

   public void addActionListener(ActionListener listener) {
      this.actionListeners.add(listener);
   }

   public void removeActionListener(ActionListener listener) {
      this.actionListeners.remove(listener);
   }

   protected void fireActionEvent() {
      final ActionEvent event = new ActionEvent(this, 0, null);
      this.actionListeners.forAllDo(new IClosure<ActionListener>() {
         public void execute(ActionListener listener) {
            listener.actionPerformed(event);
         }
      });
   }
}
