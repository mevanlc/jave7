package de.jave.image2ascii;

import de.jave.image.IValueRaster;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;

public interface IImage2AsciiAlgorithm {
   IChangeableModel getOptionsModel();

   boolean isMonochromeImageRequired();

   Dimension getImageSizeForSettings(Dimension var1, int var2, double var3);

   CharacterPlate convert(IValueRaster var1, IProgressMonitor var2, ICancelable var3) throws InterruptedException;

   Icon getIcon();

   String getName();
}
