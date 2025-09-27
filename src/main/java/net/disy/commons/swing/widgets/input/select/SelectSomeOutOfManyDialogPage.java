package net.disy.commons.swing.dialog.input.select;

import java.util.List;
import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.swing.dialog.input.ISmartDialogPanel;
import net.disy.commons.swing.dialog.input.ISmartDialogPanelsBuilder;
import net.disy.commons.swing.dialog.input.SmartDialogPage;

public class SelectSomeOutOfManyDialogPage<T> extends SmartDialogPage {
   private final ISomeOutOfManyDialogConfiguration<T> configuration;
   private final SelectSomeOutOfManyDialogPanel<T> selectionPanel;

   public SelectSomeOutOfManyDialogPage(ISomeOutOfManyDialogConfiguration<T> configuration) {
      super(configuration.getDefaultMessageText());
      this.configuration = configuration;
      T[] items = configuration.getItems();
      FixedOptionsObjectSelectionModel<T> selectionModel = new FixedOptionsObjectSelectionModel<>(items);
      if (items.length > 0) {
         selectionModel.setSelectedValue(items[0]);
      }

      this.selectionPanel = new SelectSomeOutOfManyDialogPanel<>(selectionModel, configuration);
   }

   public List<T> getSelectedItems() {
      return this.selectionPanel.getSelectedItems();
   }

   @Override
   public String getTitle() {
      return this.configuration.getTitle();
   }

   @Override
   public String getDescription() {
      return this.configuration.getDescription();
   }

   @Override
   protected void addPanels(ISmartDialogPanelsBuilder builder) {
      builder.add(this.selectionPanel);
      ISmartDialogPanel[] additionalPanels = this.configuration.createAdditionalPanels(this.selectionPanel.getSelectedItemModel());
      if (additionalPanels != null) {
         builder.add(additionalPanels);
      }
   }

   @Override
   public void requestFocus() {
      this.selectionPanel.requestFocus();
   }
}
