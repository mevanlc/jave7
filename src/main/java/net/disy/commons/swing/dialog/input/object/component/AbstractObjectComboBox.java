package net.disy.commons.swing.dialog.input.object.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.exception.ConfigurationException;
import net.disy.commons.core.message.Message;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public abstract class AbstractObjectComboBox<O> implements IObjectInputComponent<O, JComboBox> {
   private final IPresetValuesFactory<O> valueListFactory;
   private final IAttributeContext attributeContext;
   private final IObjectUi<O> objectUi;
   private final JComboBox comboBox;

   public AbstractObjectComboBox(IPresetValuesFactory<O> valueListFactory, IAttributeContext attributeContext, IObjectUi<O> objectUi) {
      this.valueListFactory = valueListFactory;
      this.attributeContext = attributeContext;
      this.objectUi = objectUi;
      this.comboBox = this.createComboBox();
   }

   private DefaultComboBoxModel createComboBoxModel() {
      List<O> values = new ArrayList<>();

      try {
         values = this.valueListFactory.createList(this.attributeContext);
      } catch (ConfigurationException var3) {
         MessageDialogFactory.createMessageDialog(null, new Message(var3.getMessage(), var3)).show();
      }

      return values == null ? new DefaultComboBoxModel() : new DefaultComboBoxModel<>(values.toArray());
   }

   protected JComboBox createComboBox() {
      DefaultComboBoxModel comboBoxModel = this.createComboBoxModel();
      JComboBox combobox = new JComboBox(comboBoxModel);
      if (this.objectUi.getLabel(null) != null) {
         combobox.setPrototypeDisplayValue(this.objectUi.getLabel(null));
      }

      combobox.setRenderer(new ObjectUiListCellRenderer(this.objectUi));
      return combobox;
   }

   @Override
   public void addChangeListener(final ChangeListener changeListener) {
      this.comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            changeListener.stateChanged(new ChangeEvent(AbstractObjectComboBox.this.comboBox));
         }
      });
   }

   @Override
   public void setValue(O value) {
      this.comboBox.setSelectedItem(value);
   }

   @Override
   public O getValue() {
      return (O)this.comboBox.getSelectedItem();
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
