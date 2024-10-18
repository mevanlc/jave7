package net.disy.commons.core.model;

public class ConjunctiveCombinationBooleanModel extends AbstractCombinationBooleanModel {
   public ConjunctiveCombinationBooleanModel(IBooleanModel firstModel, IBooleanModel secondModel) {
      super(firstModel, secondModel);
   }

   @Override
   protected boolean combine(boolean firstValue, boolean secondValue) {
      return firstValue && secondValue;
   }
}
