package de.jave.jave.actions.export;

import de.jave.gui.io.SmartFileFilter;
import de.jave.lib.CharacterPlate;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JComponent;

public interface ITextExportFormat {
   String getId();

   String getName();

   Icon getIcon();

   JComponent createPreviewComponent(CharacterPlate var1, ITextExportOptions var2);

   void convertTo(CharacterPlate var1, ITextExportOptions var2, Clipboard var3);

   void convertTo(CharacterPlate var1, ITextExportOptions var2, File var3) throws IOException;

   SmartFileFilter[] getFileFilters();

   boolean isConnectedLinesViewSupported();

   boolean isFontSupported();
}
