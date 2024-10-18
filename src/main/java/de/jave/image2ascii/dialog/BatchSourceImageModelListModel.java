package de.jave.image2ascii.dialog;

import javax.swing.AbstractListModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class BatchSourceImageModelListModel extends AbstractListModel {
   private final BatchSourceImageModel model;

   public BatchSourceImageModelListModel(BatchSourceImageModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            BatchSourceImageModelListModel.this.fireContentsChanged(BatchSourceImageModelListModel.this, 0, BatchSourceImageModelListModel.this.getSize() - 1);
         }
      });
   }

   @Override
   public int getSize() {
      return this.model.getFileCount();
   }

   @Override
   public Object getElementAt(int index) {
      return this.model.getFile(index);
   }
}
