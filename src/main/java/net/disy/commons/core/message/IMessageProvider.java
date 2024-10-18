package net.disy.commons.core.message;

import net.disy.commons.core.model.listener.IChangeListener;

public interface IMessageProvider {
   IBasicMessage getDefaultMessage();

   IBasicMessage getCurrentMessage();

   void addChangeListener(IChangeListener var1);
}
