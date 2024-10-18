package de.jave.jave.plate;

import de.jave.jave.Plate;
import de.jave.jave.browser.JaveDocumentType;
import java.io.File;
import javax.swing.JComponent;

public interface IDocumentEditor {
   Plate getPlate();

   JComponent getContent();

   JaveDocumentType getType();

   boolean isModified();

   File getFile();

   String getStopGapName();

   void dispose();
}
