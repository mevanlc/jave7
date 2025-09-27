package net.disy.commons.swing.widgetfactory;

import java.awt.Component;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;

public class SelectionWidgetFactory {
   private SelectionWidgetFactory() {
      throw new UnreachableCodeReachedException();
   }

   public static <T> JToggleButton createSelectionToogleButton(String label, ObjectModel<T> valueModel, T selection) {
      SmartAction action = createAction(label, valueModel, selection);
      JToggleButton button = new JToggleButton(action);
      connect(valueModel, selection, button);
      return button;
   }

   public static <T> JRadioButton createSelectionRadioButton(String label, ObjectModel<T> valueModel, T selection) {
      SmartAction action = createAction(label, valueModel, selection);
      JRadioButton radioButton = new JRadioButton(action);
      connect(valueModel, selection, radioButton);
      return radioButton;
   }

   private static <T> SmartAction createAction(String label, final ObjectModel<T> valueModel, final T selection) {
      return new SmartAction(label) {
         @Override
         protected void execute(Component parentComponent) {
            valueModel.setValue(selection);
         }
      };
   }

   private static <T> void connect(final ObjectModel<T> valueModel, final T selection, final JToggleButton button) {
      valueModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            button.setSelected(selection.equals(valueModel.getValue()));
         }
      });
      button.setSelected(selection.equals(valueModel.getValue()));
   }
}
