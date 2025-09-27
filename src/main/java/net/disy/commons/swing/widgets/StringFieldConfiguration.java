package net.disy.commons.swing.objectfield;

import net.disy.commons.core.creation.IFactory;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.provider.IObjectProvider;

public final class StringFieldConfiguration implements IObjectFieldConfiguration<String> {
   private final IObjectFormater<String> formater;
   private final int columns;
   private final boolean editable;

   public StringFieldConfiguration(int columns, IObjectFormater<String> formater) {
      this(columns, formater, true);
   }

   public StringFieldConfiguration(int columns, IObjectFormater<String> formater, boolean editable) {
      this.columns = columns;
      this.formater = formater;
      this.editable = editable;
   }

   @Override
   public IObjectFormater<String> getObjectFormater(IObjectProvider<String> provider) {
      return this.formater;
   }

   @Override
   public int getColumns() {
      return this.columns;
   }

   @Override
   public int getHorizontalAlignment() {
      return 4;
   }

   @Override
   public boolean isEditable() {
      return this.editable;
   }

   @Override
   public IFactory<ObjectModel<String>, RuntimeException> getModelFactory() {
      return new IFactory<ObjectModel<String>, RuntimeException>() {
         public ObjectModel<String> createInstance() throws RuntimeException {
            return new ObjectModel<>();
         }
      };
   }
}
