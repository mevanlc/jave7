package net.disy.commons.swing.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.disy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.disy.commons.core.asynchronous.IJobProcessor;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.list.IListModel;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.predicate.AcceptAllPredicate;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;

public class AsynchronousFilteredListModel<T> extends AbstractChangeableModel implements IListModel<T> {
   private final IListModel<T> listModel;
   private IPredicate<T> filter = new AcceptAllPredicate<>();
   private Object[] filteredObjects = new Object[0];
   private boolean filteringFinished = false;
   private final AsynchronousDroppingJobProcessor<IPredicate<T>> jobProcessor = new AsynchronousDroppingJobProcessor<>(new IJobProcessor<IPredicate<T>>() {
      public void process(ICancelable cancelable, IPredicate<T> processingFilter) throws InterruptedException {
         List<T> filteredValues = new ArrayList<>();

         for (int i = 0; i < AsynchronousFilteredListModel.this.listModel.getItemCount(); i++) {
            ProgressUtilities.checkInterrupted(cancelable);
            T value = AsynchronousFilteredListModel.this.listModel.getItem(i);
            if (processingFilter.evaluate(value)) {
               filteredValues.add(value);
            }
         }

         synchronized (AsynchronousFilteredListModel.this.getMutex()) {
            AsynchronousFilteredListModel.this.filter = processingFilter;
            AsynchronousFilteredListModel.this.filteredObjects = filteredValues.toArray();
            AsynchronousFilteredListModel.this.fireChangeEvent();
            AsynchronousFilteredListModel.this.filteringFinished = true;
            AsynchronousFilteredListModel.this.getMutex().notifyAll();
         }
      }
   }, new IExceptionHandler() {
      @Override
      public void handle(Throwable exception) {
         throw new RuntimeException(exception);
      }
   });

   public AsynchronousFilteredListModel(IListModel<T> listModel) {
      Ensure.ensureArgumentNotNull(listModel);
      this.listModel = listModel;
      IChangeListener changeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            AsynchronousFilteredListModel.this.startFiltering(AsynchronousFilteredListModel.this.filter);
         }
      };
      listModel.addChangeListener(changeListener);
      this.startFiltering(this.filter);
      this.waitForFilteringFinished();
   }

   public void waitForFilteringFinished() {
      synchronized (this.getMutex()) {
         while (!this.hasFinishedFiltering()) {
            try {
               this.getMutex().wait();
            } catch (InterruptedException var4) {
            }
         }
      }
   }

   @Override
   public int getItemCount() {
      synchronized (this.getMutex()) {
         return this.filteredObjects.length;
      }
   }

   @Override
   public T getItem(int index) {
      synchronized (this.getMutex()) {
         return (T)this.filteredObjects[index];
      }
   }

   @Override
   public List<T> getItemList() {
      synchronized (this.getMutex()) {
         return Arrays.asList((T[])this.filteredObjects);
      }
   }

   public void setFilter(IPredicate<T> filter) {
      synchronized (this.getMutex()) {
         this.startFiltering(filter);
      }
   }

   private void startFiltering(IPredicate<T> withFilter) {
      synchronized (this.getMutex()) {
         this.filteringFinished = false;
         this.jobProcessor.startJob(withFilter);
      }
   }

   public boolean hasFinishedFiltering() {
      synchronized (this.getMutex()) {
         return this.filteringFinished;
      }
   }

   public BooleanModel getBusyModel() {
      return this.jobProcessor.getBusyModel();
   }

   public int indexOf(final Object value) {
      synchronized (this.getMutex()) {
         return ArrayUtilities.indexOf(this.filteredObjects, new IPredicate<Object>() {
            @Override
            public boolean evaluate(Object object) {
               return ObjectUtilities.equals(object, value);
            }
         });
      }
   }
}
