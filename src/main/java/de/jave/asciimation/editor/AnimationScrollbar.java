package de.jave.asciimation.editor;

import de.jave.javeplayer.JaveAnimationFile;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class AnimationScrollbar {
   private final JScrollBar frameScrollbar;
   private final AnimationEditorModel model;

   public AnimationScrollbar(final AnimationEditorModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.frameScrollbar = new JScrollBar(0);
      this.frameScrollbar.setModel(this.createRangeModel());
      this.frameScrollbar.addAdjustmentListener(new AdjustmentListener() {
         @Override
         public void adjustmentValueChanged(AdjustmentEvent e) {
            model.getCurrentFrameIndexModel().setCurrentFrameIndex(AnimationScrollbar.this.frameScrollbar.getValue());
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationScrollbar.this.frameScrollbar.setModel(AnimationScrollbar.this.createRangeModel());
         }
      });
      model.getCurrentFrameIndexModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationScrollbar.this.frameScrollbar.setModel(AnimationScrollbar.this.createRangeModel());
         }
      });
   }

   private DefaultBoundedRangeModel createRangeModel() {
      JaveAnimationFile animationFile = this.model.getAnimationFile();
      return animationFile.getFrameCount() == 0
         ? new DefaultBoundedRangeModel(0, 0, 0, 0)
         : new DefaultBoundedRangeModel(this.model.getCurrentFrameIndexModel().getCurrentFrameIndex(), 0, 0, this.model.getAnimationFile().getFrameCount() - 1);
   }

   public JComponent getContent() {
      return this.frameScrollbar;
   }
}
