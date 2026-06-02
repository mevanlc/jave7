package de.jave.asciimation.editor;

import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.Ensure;

public class AnimationEditorModel extends AbstractChangeableModel {
   private final CurrentFrameIndexModel currentFrameIndexModel = new CurrentFrameIndexModel();
   private JaveAnimationFile animationFile;
   private final ListSelectionModel frameSelectionModel = new DefaultListSelectionModel();
   private boolean modified = false;

   public CurrentFrameIndexModel getCurrentFrameIndexModel() {
      return this.currentFrameIndexModel;
   }

   public JaveAnimationFile getAnimationFile() {
      return this.animationFile;
   }

   public void setAnimationFile(JaveAnimationFile animationFile) {
      this.animationFile = animationFile;
      this.getCurrentFrameIndexModel().setCurrentFrameIndex(0);
      this.fireChangeEvent();
   }

   public ListSelectionModel getFrameSelectionModel() {
      return this.frameSelectionModel;
   }

   public void deleteFrame(int frameIndex) {
      if (this.currentFrameIndexModel.getCurrentFrameIndex() == this.animationFile.getFrameCount() - 1) {
         this.currentFrameIndexModel.setCurrentFrameIndex(this.animationFile.getFrameCount() - 2);
      }

      this.animationFile.deleteFrame(frameIndex);
      this.modified = true;
      this.getFrameSelectionModel().clearSelection();
      this.fireChangeEvent();
   }

   public void duplicateFrame(int frameIndex) {
      JaveAnimationFrame newFrame = this.animationFile.getFrame(frameIndex).duplicate();
      this.animationFile.insertFrame(newFrame, frameIndex + 1);
      this.modified = true;
      this.fireChangeEvent();
   }

   public void addNewFrame(int frameIndex) {
      JaveAnimationFrame newFrame = this.animationFile.getFrame(frameIndex).duplicate();
      CharacterPlate content = new CharacterPlate(newFrame.getContent());
      content.clear();
      newFrame.setContent(content);
      this.animationFile.insertFrame(newFrame, frameIndex + 1);
      this.modified = true;
      this.fireChangeEvent();
   }

   public boolean isModified() {
      return this.modified;
   }

   public void setModified(boolean modified) {
      this.modified = modified;
   }

   public void setMetaData(AnimationMetaData metaData) {
      Ensure.ensureArgumentNotNull(metaData);
      if (!this.animationFile.getMetaData().equals(metaData)) {
         this.animationFile.setMetaData(metaData);
         this.modified = true;
         this.fireChangeEvent();
      }
   }

   public void setProperties(AnimationProperties properties) {
      Ensure.ensureArgumentNotNull(properties);
      if (!this.animationFile.getProperties().equals(properties)) {
         this.animationFile.setProperties(properties);
         this.modified = true;
         this.fireChangeEvent();
      }
   }

   public void doFramesRevert(int startIndex, int endIndex) {
      for (int i = 0; i < (endIndex - startIndex + 1) / 2; i++) {
         JaveAnimationFrame frame = this.animationFile.getFrame(startIndex + i);
         this.animationFile.setFrameAt(this.animationFile.getFrame(endIndex - i), startIndex + i);
         this.animationFile.setFrameAt(frame, endIndex - i);
      }

      this.modified = true;
      this.fireChangeEvent();
   }

   public void moveFrameRight(int frameIndex) {
      this.animationFile.moveFrameRight(frameIndex);
      this.modified = true;
      this.navigateNext();
      this.fireChangeEvent();
   }

   public void moveFrameLeft(int frameIndex) {
      this.animationFile.moveFrameLeft(frameIndex);
      this.modified = true;
      this.navigatePrevious();
      this.fireChangeEvent();
   }

   public void navigatePrevious() {
      int currentFrameIndex = this.currentFrameIndexModel.getCurrentFrameIndex();
      if (currentFrameIndex > 0) {
         this.navigateToFrameIndex(currentFrameIndex - 1);
      }
   }

   public void navigateNext() {
      int currentFrameIndex = this.currentFrameIndexModel.getCurrentFrameIndex();
      if (currentFrameIndex < this.animationFile.getFrameCount() - 1) {
         this.navigateToFrameIndex(currentFrameIndex + 1);
      }
   }

   public void navigateFirst() {
      this.navigateToFrameIndex(0);
   }

   public void navigateLast() {
      this.navigateToFrameIndex(this.animationFile.getFrameCount() - 1);
   }

   private void navigateToFrameIndex(int newFrameIndex) {
      this.currentFrameIndexModel.setCurrentFrameIndex(newFrameIndex);
      this.frameSelectionModel.setSelectionInterval(newFrameIndex, newFrameIndex);
   }

   public void cropAllFrames(Rectangle cropBounds) {
      for (int i = 0; i < this.animationFile.getFrameCount(); i++) {
         JaveAnimationFrame frame = this.animationFile.getFrame(i);
         CharacterPlate plate = new CharacterPlate(frame.getContent());
         plate.changeColumnsRight(-plate.getWidth() + cropBounds.x + cropBounds.width);
         plate.changeLinesBottom(-plate.getHeight() + cropBounds.y + cropBounds.height);
         plate.changeColumnsLeft(-cropBounds.x);
         plate.changeLinesTop(-cropBounds.y);
         frame.setContent(plate);
      }

      this.fireChangeEvent();
   }

   public void setCurrentFrameContent(CharacterPlate content) {
      JaveAnimationFrame animationFrame = this.animationFile.getFrame(this.currentFrameIndexModel.getCurrentFrameIndex());
      animationFrame.setContent(content);
      this.fireChangeEvent();
   }

   public void setNewFrameSize(Dimension newSize) {
      for (int i = 0; i < this.animationFile.getFrameCount(); i++) {
         JaveAnimationFrame frame = this.animationFile.getFrame(i);
         CharacterPlate plate = new CharacterPlate(frame.getContent());
         plate.setSize(newSize.width, newSize.height);
         frame.setContent(plate);
      }

      this.fireChangeEvent();
   }
}
