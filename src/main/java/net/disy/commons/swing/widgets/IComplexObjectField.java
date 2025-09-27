package net.disy.commons.swing.objectfield;

import javax.swing.JComponent;
import net.disy.commons.core.model.IBooleanModel;
import net.disy.commons.core.model.IImmutableObjectModel;
import net.disy.commons.core.model.ISettable;

public interface IComplexObjectField<T> {
   JComponent getContent();

   ISettable<T> getSettable();

   IImmutableObjectModel<T> getModel();

   IBooleanModel getValidStateModel();
}
