package net.disy.commons.swing.smarttable.filtered;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import net.disy.commons.core.list.IListModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IBlock;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.disy.commons.swing.list.AsynchronousFilteredListModel;
import net.disy.commons.swing.list.ListSelectionMode;
import net.disy.commons.swing.mousecursor.util.CursorUtilities;
import net.disy.commons.swing.smarttable.ISmartTableConfiguration;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.SmartTable;
import net.disy.commons.swing.smarttable.SmartTableConfiguration;
import net.disy.commons.swing.smarttable.actions.ITableActionFactory;
import net.disy.commons.swing.table.ListTableModel;
import net.disy.commons.swing.text.ClearTextFieldButton;
import net.disy.commons.swing.text.TextWidgetFactory;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.util.GuiUtilities;

public class FilterableListTable<T> {
   private final ObjectModel<T> selectionModel;
   private final JTextField filterTextField;
   private final AsynchronousFilteredListModel<T> filteredListModel;
   private final SmartTable table;
   private final ObjectModel<String> filterTextModel;
   private JComponent content;

   @Deprecated
   public FilterableListTable(
      IListModel<T> listModel,
      ObjectModel<T> selectionModel,
      IObjectUi<T>[] objectUis,
      String[] columnNames,
      ITableColumnViewSettings[] tableColumnViewSettings
   ) {
      this(listModel, selectionModel, objectUis, columnNames, new SmartTableConfiguration(tableColumnViewSettings), new ObjectModel<>());
   }

   public FilterableListTable(
      IListModel<T> listModel, ObjectModel<T> selectionModel, IObjectUi<T>[] objectUis, String[] columnNames, ISmartTableConfiguration configuration
   ) {
      this(listModel, selectionModel, objectUis, columnNames, configuration, new ObjectModel<>());
   }

   public FilterableListTable(
      IListModel<T> listModel,
      final ObjectModel<T> selectionModel,
      final IObjectUi<T>[] objectUis,
      String[] columnNames,
      ISmartTableConfiguration configuration,
      final ObjectModel<String> filterTextModel
   ) {
      Ensure.ensureArgumentNotNull(objectUis);
      Ensure.ensureArgumentArrayContentsNotNull(objectUis);
      Ensure.ensureArgumentNotNull(filterTextModel);
      this.selectionModel = selectionModel;
      this.filterTextModel = filterTextModel;
      this.filterTextField = TextWidgetFactory.createTextField(filterTextModel, 20);
      filterTextModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FilterableListTable.this.updateFilter(objectUis, filterTextModel);
         }
      });
      this.filteredListModel = new AsynchronousFilteredListModel<>(listModel);
      TableModel tableModel = new ListTableModel<>(this.filteredListModel, columnNames);
      this.table = new SmartTable(tableModel, configuration);
      this.table.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      this.table.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            int selectedRowIndex = FilterableListTable.this.table.getSelectedRowIndex();
            T value = selectedRowIndex == -1 ? null : FilterableListTable.this.filteredListModel.getItem(selectedRowIndex);
            selectionModel.setValue(value);
         }
      });
      this.filterTextField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 38) {
               FilterableListTable.this.moveSelectionIndex(-1);
            }

            if (e.getKeyCode() == 40) {
               FilterableListTable.this.moveSelectionIndex(1);
            }

            if (e.getKeyCode() == 27) {
               filterTextModel.setValue("");
            }
         }
      });
      this.table.setFocusable(false);
      this.filteredListModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (FilterableListTable.this.filteredListModel.getItemCount() != 0) {
               GuiUtilities.invokeLaterIfNecessary(new Runnable() {
                  @Override
                  public void run() {
                     if (FilterableListTable.this.filteredListModel.getItemCount() != 0) {
                        selectionModel.setValue(FilterableListTable.this.filteredListModel.getItem(0));
                        FilterableListTable.this.updateSelection();
                     }
                  }
               });
            }
         }
      });
      this.updateFilter(objectUis, filterTextModel);
      this.updateSelection();
   }

   private void moveSelectionIndex(int distance) {
      int newRowIndex = this.table.getSelectedRowIndex() + distance;
      this.secureScrollToAndSelect(newRowIndex);
   }

   protected void secureScrollToAndSelect(int newRowIndex) {
      if (newRowIndex >= 0 && newRowIndex < this.getRowCount()) {
         this.scrollToAndSelect(newRowIndex);
      }
   }

   protected int getRowCount() {
      return this.table.getRowCount();
   }

   protected void scrollToAndSelect(int newRowIndex) {
      this.table.scrollToAndSelect(newRowIndex);
   }

   private void updateFilter(IObjectUi<T>[] objectUis, ObjectModel<String> filterTextModel) {
      this.filteredListModel.setFilter(new ObjectUiStringFilter<>(objectUis, filterTextModel.getValue()));
   }

   public void enableEnterActionsOnFilter(final IBlock applySelection) {
      this.filterTextField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 10) {
               if (FilterableListTable.this.getFilteredListModel().getItemCount() == 1) {
                  FilterableListTable.this.applySingleFilterResult(applySelection);
               } else {
                  FilterableListTable.this.leaveFilter();
               }

               e.consume();
            }
         }
      });
   }

   protected void leaveFilter() {
      this.table.requestFocus();
      if (this.table.getSelectionModel().isSelectionEmpty() && this.getFilteredListModel().getItemCount() > 0) {
         this.table.getSelectionModel().setSelectionInterval(0, 0);
         this.table.getTable().setEditingRow(0);
      }
   }

   protected void applySingleFilterResult(IBlock applySelection) {
      this.table.getSelectionModel().setSelectionInterval(0, 0);
      this.updateSelection();
      applySelection.execute();
      this.clearFilter();
      this.filterTextField.requestFocus();
   }

   public JComponent getContent() {
      if (this.content == null) {
         JPanel panel = new JPanel(new GridDialogLayout(2, false));
         panel.add(new JLabel("Filter:"));
         ClearTextFieldButton button = new ClearTextFieldButton(this.filterTextField, "Filter zurÃ¼cksetzen");
         panel.add(TextWidgetFactory.createInternalComponentWrappedTextFieldComponent(null, this.filterTextField, button));
         panel.add(this.table.getContent(), GridDialogLayoutDataFactory.createHorizontalSpanData(2, GridDialogLayoutData.FILL_BOTH));
         this.content = panel;
         CursorUtilities.attachWaitCursorForBusyModel(this.table.getContent(), this.filteredListModel.getBusyModel());
      }

      return this.content;
   }

   private void updateSelection() {
      Object selectedValue = this.selectionModel.getValue();
      if (selectedValue == null) {
         this.table.getTable().getSelectionModel().clearSelection();
      } else {
         synchronized (this.filteredListModel) {
            int index = this.filteredListModel.indexOf(selectedValue);
            if (index == -1) {
               this.table.getTable().getSelectionModel().clearSelection();
            } else {
               this.scrollToAndSelect(index);
            }
         }
      }
   }

   public void addDoubleClickActionListener(ActionListener listener) {
      this.table.addSelectionActionListener(listener);
   }

   public void requestFocus() {
      this.table.assureSelectionVisible();
      this.filterTextField.requestFocus();
   }

   public void clearFilter() {
      this.filterTextField.setText("");
   }

   protected JTextField getFilterTextField() {
      return this.filterTextField;
   }

   protected IListModel<T> getFilteredListModel() {
      return this.filteredListModel;
   }

   protected ListSelectionModel getSelectionModel() {
      return this.table.getSelectionModel();
   }

   public void addActionFactory(ITableActionFactory actionFactory) {
      this.table.addActionFactory(actionFactory);
   }

   public void setToolbarStyleActions(boolean toolbarStyleActions) {
      this.table.setToolBarStyleButtons(toolbarStyleActions);
   }

   public void fireChangeEvent() {
      this.table.getTable().tableChanged(new TableModelEvent(this.table.getModel()));
   }
}
