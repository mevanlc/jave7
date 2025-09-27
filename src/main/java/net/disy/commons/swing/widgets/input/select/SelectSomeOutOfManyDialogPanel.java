package net.disy.commons.swing.dialog.input.select;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.CollectionUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.input.AbstractSmartDialogPanel;
import net.disy.commons.swing.label.SmartLabel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.list.IListMouseHandler;
import net.disy.commons.swing.list.JListMouseListener;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class SelectSomeOutOfManyDialogPanel<T> extends AbstractSmartDialogPanel {
   private final JList list;
   private final ISomeOutOfManyDialogPanelConfiguration<T> configuration;
   private final FixedOptionsObjectSelectionModel<T> selectedItemsModel;

   public SelectSomeOutOfManyDialogPanel(final FixedOptionsObjectSelectionModel<T> selectedItemsModel, ISomeOutOfManyDialogPanelConfiguration<T> configuration) {
      Ensure.ensureArgumentNotNull(selectedItemsModel);
      Ensure.ensureArgumentNotNull(configuration);
      this.selectedItemsModel = selectedItemsModel;
      this.configuration = configuration;
      this.list = new JList<>(configuration.getItems());
      this.list.setCellRenderer(new ObjectUiListCellRenderer(configuration.getObjectUi()));
      this.list.getSelectionModel().setSelectionMode(configuration.getListSelectionMode().getListSelectionMode());
      this.list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
               Object[] selectedValues = SelectSomeOutOfManyDialogPanel.this.list.getSelectedValues();
               Set<T> valuesSet = new HashSet<>();

               for (Object object : selectedValues) {
                  CollectionUtilities.addCasted(valuesSet, object);
               }

               selectedItemsModel.setSelectedValues(valuesSet);
            }
         }
      });
      selectedItemsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            SelectSomeOutOfManyDialogPanel.this.updateListSelection();
         }
      });
      this.updateListSelection();
      JListMouseListener.attachTo(this.list, new IListMouseHandler() {
         @Override
         public void handleDoubleClick(Object[] arg0) {
            SelectSomeOutOfManyDialogPanel.this.fireRequestFinish();
         }

         @Override
         public void handleContextMenuClick(Object[] arg0) {
         }
      });
   }

   private void updateListSelection() {
      this.list.getSelectionModel().setValueIsAdjusting(true);
      this.list.setSelectedIndices(this.selectedItemsModel.getSelectedIndices());
      this.list.getSelectionModel().setValueIsAdjusting(false);
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.selectedItemsModel.addChangeListener(listener);
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      if (this.getSelectedItems().size() == 0) {
         String noItemSelectedErrorMessageText = this.configuration.getNoItemSelectedErrorMessageText();
         if (noItemSelectedErrorMessageText != null) {
            return new BasicMessage(noItemSelectedErrorMessageText, MessageType.ERROR);
         }
      }

      return null;
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      GridDialogLayoutData layoutData = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      layoutData.setHorizontalSpan(columnCount);
      panel.add(this.createMainPanel(), layoutData);
   }

   private JPanel createMainPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      String listTitle = this.configuration.getLabel();
      if (listTitle != null) {
         JLabel label = new SmartLabel(listTitle);
         mainPanel.add(label, "North");
         label.setLabelFor(this.list);
      }

      mainPanel.add(new JScrollPane(this.list), "Center");
      return mainPanel;
   }

   public List<T> getSelectedItems() {
      Object[] selectedValues = this.list.getSelectedValues();
      List<T> valueList = new ArrayList<>();

      for (Object object : selectedValues) {
         CollectionUtilities.addCasted(valueList, object);
      }

      return valueList;
   }

   public FixedOptionsObjectSelectionModel<T> getSelectedItemModel() {
      return this.selectedItemsModel;
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }
}
