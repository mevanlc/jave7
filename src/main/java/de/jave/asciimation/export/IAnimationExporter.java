package de.jave.asciimation.export;

import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;

public interface IAnimationExporter {
   void init(Dimension var1, AnimationProperties var2, int var3, AnimationMetaData var4) throws Exception;

   void writeFrame(CharacterPlate var1) throws Exception;

   void finish() throws Exception;

   void rollBack();
}
