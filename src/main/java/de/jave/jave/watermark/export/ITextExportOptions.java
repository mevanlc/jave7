package de.jave.jave.actions.export;

import java.awt.Color;
import java.awt.Font;

public interface ITextExportOptions {
   boolean isTrim();

   Color getForegroundColor();

   Color getBackgroundColor();

   Font getFont();

   boolean isConnectedLinesView();
}
