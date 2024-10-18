package net.disy.commons.swing.widgets.internal;

import net.disy.commons.core.util.Range;

public interface IBlockRenderingHandler {
   void handleText(int var1, String var2, int var3, int var4, int var5, Range var6);

   void handleWhiteSpace(int var1, int var2, int var3, TextPosition var4, int var5, boolean var6);

   void handleLineEndsAt(int var1, int var2, int var3, int var4, int var5);
}
