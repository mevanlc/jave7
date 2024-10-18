package net.disy.commons.swing.objectfield;

import net.disy.commons.core.model.IBooleanModel;
import net.disy.commons.core.model.IObjectModel;
import net.disy.commons.swing.component.IComponentContainer;

public interface IObjectField<T> extends IComponentContainer {
   IObjectModel<T> getModel();

   IBooleanModel getValidStateModel();
}
