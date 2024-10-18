package net.disy.commons.swing.toolbar;

import javax.swing.AbstractButton;
import javax.swing.Action;
import net.disy.commons.core.grouped.IGroupedItem;

public interface IGroupedActionContainer {
   IGroupedActionContainer add(IGroupedItem<String, Action> var1);

   IGroupedActionContainer add(String var1, Action var2);

   IGroupedActionContainer add(String var1, AbstractButton var2);
}
