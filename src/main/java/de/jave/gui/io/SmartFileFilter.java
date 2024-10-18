package de.jave.gui.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public abstract class SmartFileFilter extends FileFilter {
   public abstract File makeComplete(File var1);
}
