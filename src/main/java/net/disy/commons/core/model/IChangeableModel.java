package net.disy.commons.core.model;

import net.disy.commons.core.model.listener.IChangeListener;

public interface IChangeableModel {
   void addChangeListener(IChangeListener var1);

   void removeChangeListener(IChangeListener var1);
}
