package net.disy.commons.swing.dialog.input.object.labeled;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.exception.ConfigurationException;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.object.component.IObjectInputComponent;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class LabeledValueComboBox<O> implements IObjectInputComponent<O, JComboBox> {
   private final IObjectUi<ILabeledValue> objectUi = new LabeledValueUi();
   private final JComboBox comboBox;
   private final Map<O, ILabeledValue> labelByValueMap;

   public LabeledValueComboBox(IPresetValuesFactory<ILabeledValue> valuesFactory, IAttributeContext attributeContext) {
      List<ILabeledValue> values = this.createValues(valuesFactory, attributeContext);
      this.labelByValueMap = this.initLabelByValueMap(values);
      this.comboBox = this.createComboBox(values);
   }

   private Map<O, ILabeledValue> initLabelByValueMap(List<ILabeledValue> values) {
      Map<O, ILabeledValue> map = new HashMap<>();

      for (ILabeledValue value : values) {
         map.put((O)value.getValue(), value);
      }

      return map;
   }

   private DefaultComboBoxModel createComboBoxModel(List<ILabeledValue> values) {
      return values == null ? new DefaultComboBoxModel() : new DefaultComboBoxModel<>(values.toArray());
   }

   private List<ILabeledValue> createValues(IPresetValuesFactory<ILabeledValue> valuesFactory, IAttributeContext attributeContext) {
      new ArrayList();

      try {
         List<ILabeledValue> values = valuesFactory.createList(attributeContext);
         return values == null ? new ArrayList<>() : values;
      } catch (ConfigurationException var5) {
         MessageDialogFactory.createMessageDialog(null, new Message(var5.getMessage(), var5)).show();
         return new ArrayList<>();
      }
   }

   private JComboBox createComboBox(List<ILabeledValue> values) {
      DefaultComboBoxModel comboBoxModel = this.createComboBoxModel(values);
      JComboBox combobox = new JComboBox(comboBoxModel);
      combobox.setPrototypeDisplayValue(this.createPrototypeDisplayValue(values));
      combobox.setRenderer(new ObjectUiListCellRenderer(this.objectUi));
      return combobox;
   }

   private ILabeledValue createPrototypeDisplayValue(List<ILabeledValue> values) {
      ILabeledValue maxLengthValue = new LabeledValue(this.objectUi.getLabel(null), null);

      for (ILabeledValue value : values) {
         maxLengthValue = this.max(maxLengthValue, value);
      }

      return maxLengthValue;
   }

   private ILabeledValue max(ILabeledValue maxLengthValue, ILabeledValue value) {
      return StringUtilities.length(maxLengthValue.getLabel()) > StringUtilities.length(value.getLabel()) ? maxLengthValue : value;
   }

   @Override
   public void addChangeListener(final ChangeListener changeListener) {
      this.comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            changeListener.stateChanged(new ChangeEvent(LabeledValueComboBox.this.comboBox));
         }
      });
   }

   @Override
   public void setValue(O value) {
      ILabeledValue labeledValue = this.getLabeledValue(value);
      this.comboBox.setSelectedItem(labeledValue);
   }

   private ILabeledValue getLabeledValue(O value) {
      ILabeledValue labeledValue = this.labelByValueMap.get(value);
      return value != null && labeledValue == null ? new LabeledValue(value.toString(), value) : labeledValue;
   }

   @Override
   public O getValue() {
      ILabeledValue selectedItem = (ILabeledValue)this.comboBox.getSelectedItem();
      return (O)(selectedItem == null ? null : selectedItem.getValue());
   }

   public JComboBox getComponent() {
      return this.comboBox;
   }

   @Override
   public boolean isEditable() {
      return this.comboBox.isEditable();
   }

   @Override
   public void setEditable(boolean editable) {
      this.comboBox.setEditable(editable);
   }

   @Override
   public void selectAll() {
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocus();
   }

   @Override
   public final void setEnabled(boolean enabled) {
      this.comboBox.setEnabled(enabled);
   }

   @Override
   public void update() {
   }
}
