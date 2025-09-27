package de.jave.jave.clipart;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.icon.CommonIcons;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class ClipartGroupListPanel implements IComponentContainer {
   private final JList list;
   private final ClipartGroupListModel listModel;
   private final JComponent content;

   public ClipartGroupListPanel(ClipartManager clipartManager, final ObjectModel<ClipartGroup> selectionModel) {
      Ensure.ensureArgumentNotNull(clipartManager);
      Ensure.ensureArgumentNotNull(selectionModel);
      this.listModel = new ClipartGroupListModel(clipartManager);
      this.list = new JList(this.listModel);
      this.list.setCellRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<ClipartGroupItem>() {
         public Icon getIcon(ClipartGroupItem value) {
            return value.isExisting() ? CommonIcons.FOLDER : CommonIcons.FOLDER_NEW;
         }

         public String getLabel(ClipartGroupItem value) {
            return value.getClipartGroup().getName();
         }
      }));
      if (selectionModel.getValue() != null) {
         int index = this.listModel.getItemIndex(selectionModel.getValue());
         this.list.setSelectedIndex(index);
      }

      this.list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            selectionModel.setValue(ClipartGroupListPanel.this.getSelectedGroup());
         }
      });
      this.content = new JScrollPane(this.list);
      this.content.setPreferredSize(new Dimension(150, 250));
   }

   private ClipartGroup getSelectedGroup() {
      ClipartGroupItem selectedValue = (ClipartGroupItem)this.list.getSelectedValue();
      return selectedValue == null ? null : selectedValue.getClipartGroup();
   }

   public void addNewTemporaryGroup(ClipartGroup newGroup) {
      this.listModel.add(newGroup);
      int selectionIndex = this.listModel.getSize() - 1;
      this.list.setSelectedIndex(selectionIndex);
      this.list.ensureIndexIsVisible(selectionIndex);
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
