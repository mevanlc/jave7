package net.disy.commons.core.list;

import java.util.List;
import net.disy.commons.core.model.IChangeableModel;

public interface IListModel<T> extends IChangeableModel {
   int getItemCount();

   T getItem(int var1);

   List<T> getItemList();
}
