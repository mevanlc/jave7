package de.jave.figlet.engine;

import de.jave.core.collections.LruCache;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.util.FigException;
import java.util.Map;
import net.disy.commons.core.util.Ensure;

public class FigFontCache implements IFigFontProvider {
   private static final int CAPACITY = 50;
   private final Map<String, FigFontCache.FigFontCacheEntry> entries = new LruCache<>(50);
   private final IFigFontLoader fontLoader;

   public FigFontCache(IFigFontLoader fontLoader) {
      Ensure.ensureArgumentNotNull(fontLoader);
      this.fontLoader = fontLoader;
   }

   @Override
   public synchronized FigFont getFont(String name) throws FigException {
      FigFontCache.FigFontCacheEntry entry = this.entries.get(name);
      if (entry == null) {
         FigFont font = this.fontLoader.loadFont(name);
         long lastModificationTime = this.fontLoader.getLastModificationTime(name);
         this.entries.put(name, new FigFontCache.FigFontCacheEntry(font, lastModificationTime));
         return font;
      } else {
         long cachedLastModificationTime = entry.getLastModificationTime();
         long lastModificationTime = this.fontLoader.getLastModificationTime(name);
         if (lastModificationTime > cachedLastModificationTime) {
            FigFont font = this.fontLoader.loadFont(name);
            this.entries.put(name, new FigFontCache.FigFontCacheEntry(font, lastModificationTime));
            return font;
         } else {
            return entry.getFont();
         }
      }
   }

   private static final class FigFontCacheEntry {
      private final FigFont font;
      private final long lastModificationTime;

      public FigFontCacheEntry(FigFont font, long lastModificationTime) {
         Ensure.ensureArgumentNotNull(font);
         this.font = font;
         this.lastModificationTime = lastModificationTime;
      }

      public FigFont getFont() {
         return this.font;
      }

      public long getLastModificationTime() {
         return this.lastModificationTime;
      }
   }
}
