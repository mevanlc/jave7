package net.disy.commons.swing.list;

import javax.swing.AbstractListModel;
import net.disy.commons.core.list.IListModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class ListModelAdapter<T> extends AbstractListModel {
   private final IListModel<T> listModel;

   public ListModelAdapter(final IListModel<T> listModel) {
      this.listModel = listModel;
      listModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ListModelAdapter.this.fireContentsChanged(listModel, 0, ListModelAdapter.this.getSize());
         }
      });
   }

   @Override
   public T getElementAt(int index) {
      return this.listModel.getItem(index);
   }

   @Override
   public int getSize() {
      return this.listModel.getItemCount();
   }
}
