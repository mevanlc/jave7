package net.disy.commons.swing.dialog.input.object.component;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public interface IObjectInputComponent<O, C extends JComponent> {
   void addChangeListener(ChangeListener var1);

   void setValue(O var1);

   O getValue();

   C getComponent();

   void setEditable(boolean var1);

   boolean isEditable();

   void selectAll();

   void requestFocus();

   void setEnabled(boolean var1);

   void update();
}
