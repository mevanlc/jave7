package net.disy.commons.swing.dialog.input.optional;

import java.util.Date;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.combo.BooleanModelSmartDialogPanel;
import net.disy.commons.swing.dialog.input.date.DateSmartDialogPanel;
import net.disy.commons.swing.dialog.input.number.ByteModelSmartDialogPanel;
import net.disy.commons.swing.dialog.input.number.DoubleModelSmartDialogPanel;
import net.disy.commons.swing.dialog.input.number.IntegerModelSmartDialogPanel;
import net.disy.commons.swing.dialog.input.number.LongModelSmartDialogPanel;
import net.disy.commons.swing.dialog.input.number.ShortModelSmartDialogPanel;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.text.DefaultTextSmartDialogPanel;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;
import net.disy.commons.swing.dialog.input.text.IUpdatableSmartDialogPanel;
import net.disy.commons.swing.dialog.input.text.component.StringInputComponentFactoryFactory;

public class OptionalSmartDialogPanelFactory implements IOptionalSmartDialogPanelFactory {
   private final String printName;
   private final IMessageProducingValidator validator;

   public OptionalSmartDialogPanelFactory(String printName, IMessageProducingValidator validator) {
      this.printName = printName;
      this.validator = validator;
   }

   @Override
   public IOptionalSmartDialogPanel createBytePanel(ObjectModel<Byte> model, boolean mandatory) {
      ObjectModel<Byte> fieldModel = new ObjectModel<>(model.getValue());
      ByteModelSmartDialogPanel dialogPanel = new ByteModelSmartDialogPanel(this.getLabel(), fieldModel, this.validator);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createDatePanel(ObjectModel<Date> model, boolean mandatory, String formatPattern) {
      ObjectModel<Date> fieldModel = new ObjectModel<>(model.getValue());
      DateSmartDialogPanel dialogPanel = new DateSmartDialogPanel(this.getLabel(), fieldModel, this.validator, formatPattern);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createDoublePanel(ObjectModel<Double> model, boolean mandatory) {
      ObjectModel<Double> fieldModel = new ObjectModel<>(model.getValue());
      DoubleModelSmartDialogPanel dialogPanel = new DoubleModelSmartDialogPanel(this.getLabel(), fieldModel, this.validator);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createIntegerPanel(ObjectModel<Integer> model, boolean mandatory) {
      ObjectModel<Integer> fieldModel = new ObjectModel<>(model.getValue());
      IntegerModelSmartDialogPanel dialogPanel = new IntegerModelSmartDialogPanel(this.getLabel(), fieldModel, this.validator);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createLongPanel(ObjectModel<Long> model, boolean mandatory) {
      ObjectModel<Long> fieldModel = new ObjectModel<>(model.getValue());
      LongModelSmartDialogPanel dialogPanel = new LongModelSmartDialogPanel(this.getLabel(), fieldModel, this.validator);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createShortPanel(ObjectModel<Short> model, boolean mandatory) {
      ObjectModel<Short> fieldModel = new ObjectModel<>(model.getValue());
      ShortModelSmartDialogPanel dialogPanel = new ShortModelSmartDialogPanel(this.getLabel(), fieldModel, this.validator);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createStringPanel(
      ObjectModel<String> model, boolean mandatory, IPresetValuesFactory<String> valueListFactory, IAttributeContext attributeContext
   ) {
      ObjectModel<String> fieldModel = new ObjectModel<>(model.getValue());
      DefaultTextSmartDialogPanel dialogPanel = new DefaultTextSmartDialogPanel(
         this.getLabel(), fieldModel, new StringInputComponentFactoryFactory().createFactory(valueListFactory, attributeContext), this.validator
      );
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   @Override
   public IOptionalSmartDialogPanel createBooleanPanel(ObjectModel<Boolean> model, boolean mandatory) {
      ObjectModel<Boolean> fieldModel = new ObjectModel<>(model.getValue());
      BooleanModelSmartDialogPanel dialogPanel = new BooleanModelSmartDialogPanel(this.getLabel(), fieldModel, this.validator);
      return this.createOptionalPanel(model, mandatory, dialogPanel, fieldModel);
   }

   protected <T> IOptionalSmartDialogPanel createOptionalPanel(
      ObjectModel<T> model, boolean mandatory, IUpdatableSmartDialogPanel dialogPanel, ObjectModel<T> fieldModel
   ) {
      return new OptionalSmartDialogPanel<>(model, dialogPanel, fieldModel, mandatory);
   }

   private String getLabel() {
      return this.printName + ':';
   }
}
