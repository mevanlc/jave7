package net.disy.commons.core.model;

import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public abstract class AbstractCombinationBooleanModel implements IBooleanModel {
   private final IBooleanModel firstModel;
   private final IBooleanModel secondModel;

   public AbstractCombinationBooleanModel(IBooleanModel firstModel, IBooleanModel secondModel) {
      Ensure.ensureArgumentNotNull(firstModel);
      Ensure.ensureArgumentNotNull(secondModel);
      this.firstModel = firstModel;
      this.secondModel = secondModel;
   }

   @Override
   public boolean getValue() {
      boolean firstValue = this.firstModel.getValue();
      boolean secondValue = this.secondModel.getValue();
      return this.combine(firstValue, secondValue);
   }

   protected abstract boolean combine(boolean var1, boolean var2);

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.firstModel.addChangeListener(listener);
      this.secondModel.addChangeListener(listener);
   }

   @Override
   public void removeChangeListener(IChangeListener listener) {
      this.firstModel.removeChangeListener(listener);
      this.secondModel.removeChangeListener(listener);
   }
}
