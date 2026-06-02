package de.jave.asciimation.editor;

import de.jave.javeplayer.JaveAnimationFile;
import javax.swing.AbstractListModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class FrameListModel extends AbstractListModel {
   private final AnimationEditorModel model;

   public FrameListModel(AnimationEditorModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FrameListModel.this.fireContentsChanged(this, 0, FrameListModel.this.getSize());
         }
      });
   }

   @Override
   public int getSize() {
      JaveAnimationFile animationFile = this.model.getAnimationFile();
      return animationFile == null ? 0 : animationFile.getFrameCount();
   }

   @Override
   public Object getElementAt(int index) {
      return this.model.getAnimationFile().getFrame(index);
   }
}
