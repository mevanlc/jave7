package net.disy.commons.core.list;

import java.util.Collection;

public interface IMutableListModel<T> extends IListModel<T> {
   void clear();

   void add(T var1);

   void add(T var1, int var2);

   void add(T[] var1);

   void add(Collection<T> var1);

   void remove(T var1);

   void removeItemAt(int var1);

   void replace(T var1, T var2);

   void setItem(T var1, int var2);

   void moveValueUp(int var1);

   void moveValueDown(int var1);
}
