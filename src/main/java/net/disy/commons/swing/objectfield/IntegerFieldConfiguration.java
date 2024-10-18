package net.disy.commons.swing.objectfield;

import net.disy.commons.core.creation.IFactory;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.provider.IObjectProvider;

public final class IntegerFieldConfiguration implements IObjectFieldConfiguration<Integer> {
   private final IObjectFormater<Integer> formater;
   private final int columns;

   public IntegerFieldConfiguration(IObjectFormater<Integer> formater) {
      this(10, formater);
   }

   public IntegerFieldConfiguration(int columns, IObjectFormater<Integer> formater) {
      this.columns = columns;
      this.formater = formater;
   }

   @Override
   public IObjectFormater<Integer> getObjectFormater(IObjectProvider<Integer> provider) {
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
      return true;
   }

   @Override
   public IFactory<ObjectModel<Integer>, RuntimeException> getModelFactory() {
      return new IFactory<ObjectModel<Integer>, RuntimeException>() {
         public ObjectModel<Integer> createInstance() throws RuntimeException {
            return new ObjectModel<>();
         }
      };
   }
}
