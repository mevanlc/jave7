package net.disy.commons.core.progress;

public interface IObservableCancelable extends ICancelable {
   void addCanceledListener(ICanceledListener var1);

   void removeCanceledListener(ICanceledListener var1);
}
