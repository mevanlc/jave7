package net.dizzy.commons.core.model;

import net.dizzy.commons.core.model.listener.IChangeListener;

public interface IChangeableModel {
   void addChangeListener(IChangeListener listener);

   void removeChangeListener(IChangeListener listener);
}
