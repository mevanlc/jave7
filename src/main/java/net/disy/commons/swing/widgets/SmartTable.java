package net.disy.commons.swing.smarttable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.swing.action.DisableableProxyAction;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutDirection;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.list.ListSelectionMode;
import net.disy.commons.swing.smarttable.actions.ITableActionFactory;
import net.disy.commons.swing.table.AbstractTableCellRendererDecorator;
import net.disy.commons.swing.table.DefaultTableHeaderToolTipProvider;
import net.disy.commons.swing.table.ITableHeaderToolTipProvider;
import net.disy.commons.swing.table.ToolTipCellRendererDecorator;
import net.disy.commons.swing.toolbar.ToolBarUtilities;
import net.disy.commons.swing.util.IEnableableComponentContainer;

public class SmartTable implements IEnableableComponentContainer, IComponentContainer {
   private boolean enabled = true;
   private final JTable table;
   private JComponent content;
   private final ListenerList<ActionListener> selectionActionListeners = new ListenerList<>();
   private final List<ITableActionFactory> actionFactories = new ArrayList<>();
   private ITableActionFactory doubleClickOnItemActionFactory;
   private boolean toolBarStyleButtons = false;
   private Action[] actions = new Action[0];
   private final ISmartTableConfiguration configuration;

   @Deprecated
   public SmartTable(TableModel tableModel, ITableColumnViewSettings<?>... settings) {
      this(tableModel, new SmartTableConfiguration(settings));
   }

   public SmartTable(TableModel tableModel, ISmartTableConfiguration configuration) {
      this(new JTable(), tableModel, configuration);
   }

   public SmartTable(final JTable table, TableModel tableModel, ISmartTableConfiguration configuration) {
      Ensure.ensureArgumentNotNull(table);
      Ensure.ensureArgumentNotNull(tableModel);
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
      this.table = table;
      table.setModel(tableModel);
      tableModel.addTableModelListener(new TableModelListener() {
         @Override
         public void tableChanged(TableModelEvent e) {
            SmartTable.this.cancelCellEditing();
         }
      });
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && !e.isMetaDown()) {
               if (table.getSelectedRowCount() != 0) {
                  int rowIndex = table.getSelectedRow();
                  int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
                  SmartTable.this.handleDoubleClickOnCell(rowIndex, columnIndex);
                  SmartTable.this.fireSelectionActionEvent();
               }
            }
         }
      });
      table.setRowHeight(Math.max(table.getRowHeight(), 21));
      TableColumnConfigurator.configureTableColumns(table, configuration.getColumnViewSettings());
      this.setHeaderToolTipProvider(new DefaultTableHeaderToolTipProvider());
      this.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
   }

   protected void handleDoubleClickOnCell(final int rowIndex, final int columnIndex) {
      final Object cellValue = this.table.getModel().getValueAt(rowIndex, columnIndex);
      ITableColumnViewSettings<Object>[] columnViewSettings = (ITableColumnViewSettings<Object>[])this.configuration.getColumnViewSettings();
      columnViewSettings[columnIndex].getDoubleClickBehaviour().invokeForValue(this.table, cellValue, new IClosure<Object>() {
         @Override
         public void execute(Object newValue) throws RuntimeException {
            if (cellValue != newValue) {
               SmartTable.this.table.getModel().setValueAt(newValue, rowIndex, columnIndex);
            }
         }
      });
   }

   public void addActionFactory(ITableActionFactory actionFactory) {
      this.addActionFactory(actionFactory, false);
   }

   public void addActionFactory(ITableActionFactory actionFactory, boolean activateOnDoubleClickOnItem) {
      Ensure.ensureArgumentNotNull(actionFactory);
      if (this.content != null) {
         throw new IllegalStateException("Adding action factories after creating content is not allowed.");
      } else {
         this.actionFactories.add(actionFactory);
         if (activateOnDoubleClickOnItem) {
            this.doubleClickOnItemActionFactory = actionFactory;
         }
      }
   }

   public void setSelectionMode(ListSelectionMode selectionMode) {
      this.table.setSelectionMode(selectionMode.getListSelectionMode());
   }

   public JTable getTable() {
      return this.table;
   }

   public TableModel getModel() {
      return this.table.getModel();
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      if (!enabled) {
         TableCellEditor cellEditor = this.table.getCellEditor();
         if (cellEditor != null) {
            cellEditor.stopCellEditing();
         }
      }

      this.updateEnabled();
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   protected void updateEnabled() {
      this.table.setEnabled(this.enabled);
      if (!this.enabled) {
         this.table.getSelectionModel().clearSelection();
      }

      for (int i = 0; i < this.actions.length; i++) {
         this.actions[i].setEnabled(this.enabled);
      }
   }

   @Override
   public final JComponent getContent() {
      if (this.content == null) {
         this.content = this.createContent();
         this.updateEnabled();
      }

      return this.content;
   }

   private JComponent createContent() {
      JPanel tablePanel = new JPanel(new BorderLayout());
      tablePanel.add(this.table);
      JScrollPane scrollPane = new JScrollPane(tablePanel) {
         @Override
         public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            SmartTable.this.setEnabled(enabled);
         }
      };
      scrollPane.getVerticalScrollBar().setUnitIncrement(this.table.getRowHeight());
      JTableHeader tableHeader = this.table.getTableHeader();
      scrollPane.setColumnHeaderView(tableHeader);
      int preferredWidth = this.table.getPreferredSize().width + scrollPane.getInsets().left + scrollPane.getInsets().right;
      int headerHeight = tableHeader == null ? 0 : tableHeader.getPreferredSize().height;
      int preferredHeigth = this.table.getRowHeight() * this.configuration.getVisibleRowCount()
         + headerHeight
         + scrollPane.getInsets().bottom
         + scrollPane.getInsets().top;
      scrollPane.setPreferredSize(new Dimension(preferredWidth, preferredHeigth));
      this.actions = this.createTableActions();
      if (this.actions.length == 0) {
         return scrollPane;
      } else {
         JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing())) {
            @Override
            public void setEnabled(boolean enabled) {
               SmartTable.this.setEnabled(enabled);
               if (!enabled) {
                  TableCellEditor cellEditor = SmartTable.this.getTable().getCellEditor();
                  if (cellEditor != null) {
                     cellEditor.stopCellEditing();
                  }
               }
            }
         };
         panel.add(scrollPane, "Center");
         panel.add(this.createButtonPanel(this.actions), "East");
         return panel;
      }
   }

   private Action[] createTableActions() {
      Action[] createdActions = new Action[this.actionFactories.size()];

      for (int i = 0; i < createdActions.length; i++) {
         ITableActionFactory factory = this.actionFactories.get(i);
         DisableableProxyAction action = new DisableableProxyAction(factory.createAction(this));
         if (factory == this.doubleClickOnItemActionFactory) {
            this.addSelectionActionListener(action);
         }

         createdActions[i] = action;
      }

      return createdActions;
   }

   private JPanel createButtonPanel(Action[] additionalActions) {
      return this.isToolBarStyleButtons() ? this.createToolbarStyleButtons(additionalActions) : this.createNonToolbarStyleButtons(additionalActions);
   }

   private JPanel createNonToolbarStyleButtons(Action[] additionalActions) {
      ButtonPanelBuilder builder = new ButtonPanelBuilder(LayoutDirection.VERTICAL);

      for (int i = 0; i < additionalActions.length; i++) {
         builder.add(additionalActions[i]);
      }

      JPanel panel = builder.createPanel();
      panel.setBorder(null);
      return panel;
   }

   private JPanel createToolbarStyleButtons(Action[] additionalActions) {
      GridDialogLayoutData layoutData = new GridDialogLayoutData();
      layoutData.setHorizontalAlignment(GridAlignment.FILL);
      JPanel buttonPanel = new JPanel(new GridDialogLayout(1, false));

      for (int i = 0; i < additionalActions.length; i++) {
         buttonPanel.add(ToolBarUtilities.createToolBarButton(additionalActions[i]), layoutData);
      }

      return buttonPanel;
   }

   public void requestFocus() {
      this.table.requestFocus();
   }

   public void setHeaderToolTipProvider(ITableHeaderToolTipProvider provider) {
      this.addTableHeaderRendererDecorator(new ToolTipCellRendererDecorator(provider));
   }

   public void addTableHeaderRendererDecorator(AbstractTableCellRendererDecorator decorator) {
      JTableHeader tableHeader = this.table.getTableHeader();
      decorator.setDelegate(tableHeader.getDefaultRenderer());
      tableHeader.setDefaultRenderer(decorator);
   }

   public synchronized void addSelectionActionListener(ActionListener listener) {
      this.selectionActionListeners.add(listener);
   }

   public synchronized void removeSelectionActionListener(ActionListener listener) {
      this.selectionActionListeners.remove(listener);
   }

   private void fireSelectionActionEvent() {
      final ActionEvent actionEvent = new ActionEvent(this.table, -1, "select");
      this.selectionActionListeners.forAllDo(new IClosure<ActionListener>() {
         public void execute(ActionListener listener) {
            listener.actionPerformed(actionEvent);
         }
      });
   }

   public final void scrollToAndSelect(int rowIndex) {
      this.getTable().getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
      this.assureSelectionVisible();
   }

   public void stopCellEditing() {
      TableCellEditor cellEditor = this.getTable().getCellEditor();
      if (cellEditor != null) {
         cellEditor.stopCellEditing();
      }
   }

   public void cancelCellEditing() {
      TableCellEditor cellEditor = this.getTable().getCellEditor();
      if (cellEditor != null) {
         cellEditor.cancelCellEditing();
      }
   }

   public boolean isToolBarStyleButtons() {
      return this.toolBarStyleButtons;
   }

   public void setToolBarStyleButtons(boolean toolBarStyleButtons) {
      this.toolBarStyleButtons = toolBarStyleButtons;
   }

   public int getSelectedRowIndex() {
      int selectedRowIndex = this.table.getSelectedRow();
      return selectedRowIndex > this.table.getRowCount() ? -1 : selectedRowIndex;
   }

   public boolean isSelectionEmpty() {
      return this.getSelectedRowIndex() == -1;
   }

   public void addListSelectionListener(ListSelectionListener listener) {
      ListSelectionModel selectionModel = this.getTable().getSelectionModel();
      selectionModel.addListSelectionListener(listener);
   }

   public void assureSelectionVisible() {
      int rowIndex = this.getTable().getSelectedRow();
      if (rowIndex != -1) {
         this.getTable().scrollRectToVisible(this.getTable().getCellRect(rowIndex, 0, true));
      }
   }

   @Override
   public JComponent[] getComponents() {
      return new JComponent[]{this.getContent()};
   }

   public ITableColumnViewSettings<?>[] getSettings() {
      ITableColumnViewSettings<?>[] settings = this.configuration.getColumnViewSettings();
      return Arrays.copyOf(settings, settings.length);
   }

   public void setBackground(Color background) {
      this.table.setBackground(background);
   }

   public void hideHeader() {
      this.table.setTableHeader(null);
   }

   public void setName(String name) {
      this.table.setName(name);
   }

   public ListSelectionModel getSelectionModel() {
      return this.table.getSelectionModel();
   }

   public void setFocusable(boolean focusable) {
      this.table.setFocusable(focusable);
   }

   public int getRowCount() {
      return this.table.getRowCount();
   }
}
