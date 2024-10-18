package net.disy.commons.swing.dispose;

import java.util.ArrayList;
import java.util.List;

public class AggregatedDisposable implements IDisposable {
   private final List<IDisposable> allDisposables = new ArrayList<>();

   @Override
   public void dispose() {
      for (IDisposable disposable : this.allDisposables) {
         disposable.dispose();
      }
   }

   public void add(IDisposable disposable) {
      this.allDisposables.add(disposable);
   }
}
