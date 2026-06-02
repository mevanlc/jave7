package de.jave.image2ascii.model;

import de.jave.image2ascii.IImage2AsciiAlgorithm;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;

public class Image2AsciiOptionsModel extends AbstractChangeableModel {
   private final Image2AsciiOutputOptionsModel outputOptionsModel = new Image2AsciiOutputOptionsModel();
   private final Image2AsciiImageProcessingOptionsModel imageProcessingOptionsModel = new Image2AsciiImageProcessingOptionsModel();
   private final ObjectModel<IImage2AsciiAlgorithm> algorithmModel = new ObjectModel<>();

   public Image2AsciiOptionsModel() {
      IChangeListener delegatingListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2AsciiOptionsModel.this.fireChangeEvent();
         }
      };
      this.outputOptionsModel.addChangeListener(delegatingListener);
      this.imageProcessingOptionsModel.addChangeListener(delegatingListener);
      this.algorithmModel.addChangeListener(delegatingListener);
   }

   public Image2AsciiOutputOptionsModel getOutputOptionsModel() {
      return this.outputOptionsModel;
   }

   public Image2AsciiImageProcessingOptionsModel getImageProcessingOptionsModel() {
      return this.imageProcessingOptionsModel;
   }

   public void reset() {
      this.outputOptionsModel.reset();
      this.imageProcessingOptionsModel.reset();
   }

   public IImage2AsciiAlgorithm getAlgorithm() {
      return this.algorithmModel.getValue();
   }

   public void setAlgorithm(IImage2AsciiAlgorithm image2AsciiAlgorithm) {
      this.algorithmModel.setValue(image2AsciiAlgorithm);
   }

   public void fireAlgorithmOptionsChanged() {
      this.fireChangeEvent();
   }
}
