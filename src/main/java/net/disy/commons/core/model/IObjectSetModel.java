package net.disy.commons.core.model;

public interface IObjectSetModel<T> extends IChangeableModel {
   boolean isEmpty();

   void add(T... var1);

   void remove(T... var1);

   void clear();
}
