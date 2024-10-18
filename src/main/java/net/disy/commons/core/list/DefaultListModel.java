package net.disy.commons.core.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.ObjectUtilities;

public class DefaultListModel<T> extends AbstractChangeableModel implements IMutableListModel<T> {
   private final List<T> items = new ArrayList<>();

   public DefaultListModel() {
   }

   public DefaultListModel(T... items) {
      this.items.addAll(Arrays.asList(items));
   }

   public DefaultListModel(List<T> items) {
      this.items.addAll(items);
   }

   @Override
   public void add(T item) {
      this.items.add(item);
      this.fireChangeEvent();
   }

   @Override
   public void add(T item, int index) {
      this.items.add(index, item);
      this.fireChangeEvent();
   }

   @Override
   public void add(T[] allItems) {
      this.add(Arrays.asList(allItems));
   }

   @Override
   public void add(Collection<T> allItems) {
      this.items.addAll(allItems);
      this.fireChangeEvent();
   }

   @Override
   public void clear() {
      this.items.clear();
      this.fireChangeEvent();
   }

   @Override
   public int getItemCount() {
      return this.items.size();
   }

   @Override
   public T getItem(int index) {
      return this.items.get(index);
   }

   @Override
   public void remove(T item) {
      this.items.remove(item);
      this.fireChangeEvent();
   }

   @Override
   public void replace(T oldItem, T newItem) {
      if (!ObjectUtilities.equals(oldItem, newItem)) {
         int index = this.items.indexOf(oldItem);
         if (index != -1) {
            this.setItem(newItem, index);
         }
      }
   }

   @Override
   public void setItem(T item, int index) {
      if (!ObjectUtilities.equals(item, this.getItem(index))) {
         this.items.set(index, item);
         this.fireChangeEvent();
      }
   }

   @Override
   public List<T> getItemList() {
      return new ArrayList<>(this.items);
   }

   @Override
   public void removeItemAt(int index) {
      this.items.remove(index);
      this.fireChangeEvent();
   }

   @Override
   public void moveValueDown(int index) {
      T firstElement = this.items.get(index - 1);
      T secondElement = this.items.get(index);
      this.items.set(index - 1, secondElement);
      this.items.set(index, firstElement);
      this.fireChangeEvent();
   }

   @Override
   public void moveValueUp(int index) {
      T firstElement = this.items.get(index);
      T secondElement = this.items.get(index + 1);
      this.items.set(index, secondElement);
      this.items.set(index + 1, firstElement);
      this.fireChangeEvent();
   }

   public void setItems(T[] items) {
      this.items.clear();
      this.items.addAll(Arrays.asList(items));
      this.fireChangeEvent();
   }
}
