package net.disy.commons.swing.objectfield;

import net.disy.commons.core.creation.IFactory;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.provider.IObjectProvider;

public interface IObjectFieldConfiguration<T> {
   IObjectFormater<T> getObjectFormater(IObjectProvider<T> var1);

   int getColumns();

   int getHorizontalAlignment();

   boolean isEditable();

   IFactory<ObjectModel<T>, RuntimeException> getModelFactory();
}
