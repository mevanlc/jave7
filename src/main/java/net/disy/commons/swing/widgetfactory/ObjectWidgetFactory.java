package net.disy.commons.swing.widgetfactory;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.list.IMutableListModel;
import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.CollectionUtilities;
import net.disy.commons.core.util.ObjectUtilities;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class ObjectWidgetFactory {
   private ObjectWidgetFactory() {
      throw new UnreachableCodeReachedException();
   }

   public static <T> JComboBox createComboBox(final ObjectModel<T> objectModel, T[] values) {
      final JComboBox widget = new JComboBox<>(values);
      objectModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Object value = objectModel.getValue();
            if (!ObjectUtilities.equals(value, widget.getSelectedItem())) {
               widget.setSelectedItem(value);
            }
         }
      });
      widget.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            objectModel.setValue((T)widget.getSelectedItem());
         }
      });
      widget.setSelectedItem(objectModel.getValue());
      return widget;
   }

   public static <T> JComboBox createComboBox(ObjectModel<T> objectModel, T[] values, IObjectUi<T> ui) {
      JComboBox widget = createComboBox(objectModel, values);
      widget.setRenderer(new ObjectUiListCellRenderer(ui));
      return widget;
   }

   public static <T> JComboBox createComboBox(
      ObjectModel<T> lebenraumtypModel, final IMutableListModel<T> listModel, AbstractObjectUi<T> abstractObjectUi, Class<T> clazz
   ) {
      final JComboBox comboBox = createComboBox(lebenraumtypModel, CollectionUtilities.toArray(listModel.getItemList(), clazz), abstractObjectUi);
      listModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            comboBox.removeAllItems();

            for (T item : listModel.getItemList()) {
               comboBox.addItem(item);
            }
         }
      });
      return comboBox;
   }

   public static <T> JComboBox createComboBox(final FixedOptionsObjectSelectionModel<T> model, IObjectUi<T> objectUi) {
      final JComboBox comboBox = new JComboBox<>(model.getAllValues());
      comboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            T selectedItem = (T)comboBox.getSelectedItem();
            model.setSelectedValue(selectedItem);
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            comboBox.setSelectedItem(model.getFirstSelectedValue());
         }
      });
      comboBox.setSelectedItem(model.getFirstSelectedValue());
      return comboBox;
   }

   public static <T> JRadioButton createRadioButton(String label, final T type, final ObjectModel<T> objectModel) {
      final JRadioButton radioButton = new JRadioButton(new SmartAction(label) {
         @Override
         protected void execute(Component parentComponent) {
            objectModel.setValue(type);
         }
      });
      objectModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            radioButton.setSelected(type.equals(objectModel.getValue()));
         }
      });
      radioButton.setSelected(type.equals(objectModel.getValue()));
      return radioButton;
   }

   public static <T> JRadioButton createRadioButton(String label, T type, ObjectModel<T> objectModel, ButtonGroup buttonGroup) {
      JRadioButton radioButton = createRadioButton(label, type, objectModel);
      buttonGroup.add(radioButton);
      return radioButton;
   }
}
