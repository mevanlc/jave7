package net.disy.commons.swing.dialog.input.combo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.list.IListModel;
import net.disy.commons.core.list.ImmutableListModel;
import net.disy.commons.core.model.IObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.dialog.input.AbstractLabeledSmartDialogPanel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.ISelectableItemsPanelConfiguration;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class ComboSelectionDialogPanel<T> extends AbstractLabeledSmartDialogPanel {
   private final IObjectModel<T> model;
   private final JComboBox comboBox;
   private final IListModel<T> itemsModel;

   public ComboSelectionDialogPanel(
      String label, IObjectModel<T> model, ISelectableItemsPanelConfiguration<T> configuration, IMessageProducingValidator validator
   ) {
      this(label, model, new ImmutableListModel<>(configuration.getItems()), configuration.getObjectUi(), validator);
   }

   public ComboSelectionDialogPanel(
      String label, final IObjectModel<T> model, IListModel<T> itemsModel, IObjectUi<T> objectUi, IMessageProducingValidator validator
   ) {
      super(label, validator);
      this.model = model;
      this.itemsModel = itemsModel;
      this.comboBox = new JComboBox();
      itemsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ComboSelectionDialogPanel.this.updateComboItems();
            model.setValue(null);
         }
      });
      this.updateComboItems();
      this.comboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ComboSelectionDialogPanel.this.updateCombo();
         }
      });
      this.updateCombo();
      this.comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setValue((T)ComboSelectionDialogPanel.this.comboBox.getSelectedItem());
         }
      });
   }

   private void updateComboItems() {
      this.comboBox.removeAllItems();

      for (T item : this.itemsModel.getItemList()) {
         this.comboBox.addItem(item);
      }
   }

   private void updateCombo() {
      this.comboBox.setSelectedItem(this.model.getValue());
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.model.addChangeListener(listener);
   }

   @Override
   protected int getMainComponentColumnCount() {
      return 1;
   }

   @Override
   protected JComponent fillMainComponentInto(JPanel panel, int columnCount) {
      panel.add(this.comboBox, GridDialogLayoutDataFactory.createHorizontalFillNoGrab());
      return this.comboBox;
   }

   @Override
   public final void requestFocus() {
      this.comboBox.requestFocus();
   }

   @Override
   protected void setMainComponentEnabled(boolean enabled) {
      this.comboBox.setEnabled(enabled);
   }

   @Override
   protected JComponent[] getOtherComponents() {
      return new JComponent[]{this.comboBox};
   }
}
