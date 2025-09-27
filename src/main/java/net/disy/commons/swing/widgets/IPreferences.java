package net.disy.commons.swing.preferences;

import java.awt.Color;
import java.io.File;
import net.disy.commons.core.text.font.FontDescription;

public interface IPreferences {
   void putString(String var1, String var2, String var3);

   void putInt(String var1, int var2, int var3);

   void putBoolean(String var1, boolean var2, boolean var3);

   int getInt(String var1, int var2);

   boolean getBoolean(String var1, boolean var2);

   IPreferences node(String var1);

   void flush();

   void remove(String var1);

   void putFontDescription(String var1, FontDescription var2, FontDescription var3);

   void putColor(String var1, Color var2, Color var3);

   Color getColor(String var1, Color var2);

   FontDescription getFontDescription(String var1, FontDescription var2);

   String getString(String var1);

   String getString(String var1, String var2);

   String[] getStringArray(String var1);

   void putStringArray(String var1, String[] var2);

   boolean exists(String var1);

   void removeNode();

   void putFile(String var1, File var2);

   File getFile(String var1);

   boolean nodeExists(String var1);
}
