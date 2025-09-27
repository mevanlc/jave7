package net.disy.commons.swing.component;

import net.disy.commons.swing.dispose.AggregatedDisposable;
import net.disy.commons.swing.dispose.IDisposable;

public class DisposableContainer implements IDisposable {
   private final AggregatedDisposable disposables = new AggregatedDisposable();

   protected final void addDisposable(IDisposable disposable) {
      this.disposables.add(disposable);
   }

   @Override
   public void dispose() {
      this.disposables.dispose();
   }
}
