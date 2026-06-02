package de.jave.jave.icon;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JaveIconsTest {
   @Test
   public void preferredIconPathUsesSizedPngWhenAvailable() {
      assertEquals("tool/24/freehand.png", JaveIcons.resolvePreferredIconPath("tool/freehand.gif", 24));
      assertEquals("tool/32/freehand.png", JaveIcons.resolvePreferredIconPath("tool/freehand.gif", 32));
      assertEquals("32/open.png", JaveIcons.resolvePreferredIconPath("open.gif", 32));
   }

   @Test
   public void preferredIconPathFallsBackToBaseIconWhenSizedPngIsMissing() {
      assertEquals("tool/tool_algorithmic.gif", JaveIcons.resolvePreferredIconPath("tool/tool_algorithmic.gif", 32));
      assertEquals("tool/freehand.gif", JaveIcons.resolvePreferredIconPath("tool/freehand.gif", 16));
   }
}
