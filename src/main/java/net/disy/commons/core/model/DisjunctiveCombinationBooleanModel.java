package net.disy.commons.core.model;

public class DisjunctiveCombinationBooleanModel extends AbstractCombinationBooleanModel {
   public DisjunctiveCombinationBooleanModel(IBooleanModel firstModel, IBooleanModel secondModel) {
      super(firstModel, secondModel);
   }

   @Override
   protected boolean combine(boolean firstValue, boolean secondValue) {
      return firstValue || secondValue;
   }
}
