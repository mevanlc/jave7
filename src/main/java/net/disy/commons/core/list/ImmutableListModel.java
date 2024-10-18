package net.disy.commons.core.list;

import java.util.Arrays;
import java.util.List;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class ImmutableListModel<T> implements IListModel<T> {
   private final T[] objects;

   public ImmutableListModel(T[] objects) {
      this.objects = objects;
      Ensure.ensureArgumentNotNull(objects);
   }

   @Override
   public int getItemCount() {
      return this.objects.length;
   }

   @Override
   public T getItem(int index) {
      return this.objects[index];
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
   }

   @Override
   public void removeChangeListener(IChangeListener listener) {
   }

   @Override
   public List<T> getItemList() {
      return Arrays.asList(this.objects);
   }
}
