package net.disy.commons.swing.dialog.input.select;

import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.dialog.input.ISmartDialogPanelsBuilder;
import net.disy.commons.swing.dialog.input.SmartDialogPage;

public class SmartOneOutOfManyDialogPage<T> extends SmartDialogPage {
   private final AbstractOneOutOfManyDialogConfiguration<T> configuration;
   private final ObjectModel<T> model;
   private SelectSomeOutOfManyDialogPanel<T> outputTargetPanel;

   public SmartOneOutOfManyDialogPage(AbstractOneOutOfManyDialogConfiguration<T> configuration, ObjectModel<T> model) {
      super(configuration.getDefaultMessageText());
      this.model = model;
      this.configuration = configuration;
   }

   @Override
   public final String getTitle() {
      return this.configuration.getTitle();
   }

   @Override
   public String getDescription() {
      return this.configuration.getDescription();
   }

   @Override
   public void requestFocus() {
      this.outputTargetPanel.requestFocus();
   }

   @Override
   protected void addPanels(ISmartDialogPanelsBuilder builder) {
      final FixedOptionsObjectSelectionModel<T> outputTargetSelectionModel = new FixedOptionsObjectSelectionModel<>(this.configuration.getItems());
      outputTargetSelectionModel.setSelectedValue(this.model.getValue());
      outputTargetSelectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            SmartOneOutOfManyDialogPage.this.model.setValue(outputTargetSelectionModel.getFirstSelectedValue());
         }
      });
      this.outputTargetPanel = new SelectSomeOutOfManyDialogPanel<>(outputTargetSelectionModel, this.configuration);
      builder.add(this.outputTargetPanel);
   }
}
