package de.jave.gui.io;

import de.jave.jave.swing.JaveSwingMessages;

public class ExtensionFileFilters {
   public static final ExtensionFileFilter SUPPORTED_IMAGES = new ExtensionFileFilter(
      JaveSwingMessages.FileFormat_ImageFiles, ImageIOUtilities.getSupportedReaderFileFormatExtensions()
   );
   public static final ExtensionFileFilter HTML = new ExtensionFileFilter(JaveSwingMessages.FileFormat_HtmlFile, FileExtensions.HTML, FileExtensions.HTM);
   public static final ExtensionFileFilter ACTIONSCRIPT = new ExtensionFileFilter(JaveSwingMessages.FileFormat_ActionscriptFile, FileExtensions.AS);
   public static final ExtensionFileFilter JMOV = new ExtensionFileFilter(JaveSwingMessages.FileFormat_JavEAnimationFile, FileExtensions.JMOV);
   public static final ExtensionFileFilter TXT = new ExtensionFileFilter(JaveSwingMessages.FileFormat_TextFile, FileExtensions.TXT);
   public static final ExtensionFileFilter VT = new ExtensionFileFilter(JaveSwingMessages.FileFormat_VTAnimationFile, FileExtensions.VT);
   public static final ExtensionFileFilter GIF = new ExtensionFileFilter(JaveSwingMessages.FileFormat_GifImageFile, FileExtensions.GIF);
   public static final ExtensionFileFilter SWF = new ExtensionFileFilter(JaveSwingMessages.FileFormat_MacromediaFlashFile, FileExtensions.SWF);
}
