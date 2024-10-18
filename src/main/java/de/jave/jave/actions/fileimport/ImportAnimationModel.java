package de.jave.jave.actions.fileimport;

import de.jave.javeplayer.JaveAnimationFile;
import java.io.File;
import net.disy.commons.core.model.AbstractChangeableModel;

public class ImportAnimationModel extends AbstractChangeableModel {
   private File sourceFile;
   private JaveAnimationFile animationFile;

   public JaveAnimationFile getAnimationFile() {
      return this.animationFile;
   }

   public File getSourceFile() {
      return this.sourceFile;
   }

   public void setAnimation(File sourceFile, JaveAnimationFile animationFile) {
      this.sourceFile = sourceFile;
      this.animationFile = animationFile;
      this.fireChangeEvent();
   }
}
