package net.disy.commons.core.model;

import java.util.ArrayList;
import java.util.Collection;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.ObjectUtilities;

public class BooleanModelObjectModelConnector<T> {
   private final ObjectModel<T> objectModel;
   private final T emptyValue;
   private final Collection<BooleanModel> modelList = new ArrayList<>();

   public BooleanModelObjectModelConnector(ObjectModel<T> objectModel, T emptyValue) {
      this.objectModel = objectModel;
      this.emptyValue = emptyValue;
      objectModel.setValue(emptyValue);
   }

   public void connect(final BooleanModel booleanModel, final T value) {
      this.updateObjectModel(booleanModel, value);
      booleanModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            BooleanModelObjectModelConnector.this.updateObjectModel(booleanModel, value);
         }
      });
      this.objectModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            BooleanModelObjectModelConnector.this.updateBooleanModel(booleanModel, value);
         }
      });
      this.modelList.add(booleanModel);
   }

   private void updateBooleanModel(BooleanModel booleanModel, T value) {
      booleanModel.setValue(ObjectUtilities.equals(this.objectModel.getValue(), value));
   }

   private void updateObjectModel(BooleanModel booleanModel, T value) {
      if (booleanModel.getValue()) {
         this.objectModel.setValue(value);
      } else {
         boolean modelListContainsTrueModel = false;

         for (BooleanModel model : this.modelList) {
            if (model.getValue()) {
               modelListContainsTrueModel = true;
            }
         }

         if (!modelListContainsTrueModel) {
            this.objectModel.setValue(this.emptyValue);
         }
      }
   }
}
