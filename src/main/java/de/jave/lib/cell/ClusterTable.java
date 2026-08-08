package de.jave.lib.cell;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/** Process-local, append-only storage for multi-code-point grapheme clusters. */
public final class ClusterTable {
   public static final ClusterTable INSTANCE = new ClusterTable();
   static final int WARNING_THRESHOLD = 100_000;
   private static final Logger LOGGER = Logger.getLogger(ClusterTable.class.getName());

   private final Map<String, Integer> handlesByText = new HashMap<>();
   private final List<Entry> entries = new ArrayList<>();
   private long internedTextBytes;
   private boolean warnedAboutSize;

   public synchronized int intern(String cluster) {
      Objects.requireNonNull(cluster, "cluster");
      if (cluster.isEmpty()) {
         throw new IllegalArgumentException("A cluster may not be empty");
      }

      Integer existing = this.handlesByText.get(cluster);
      if (existing != null) {
         return existing;
      }

      int handle = GlyphEncoding.indexToHandle(this.entries.size());
      this.entries.add(new Entry(cluster, 1));
      this.handlesByText.put(cluster, handle);
      this.internedTextBytes += cluster.getBytes(StandardCharsets.UTF_8).length;
      if (!this.warnedAboutSize && this.entries.size() >= WARNING_THRESHOLD) {
         this.warnedAboutSize = true;
         LOGGER.warning("The glyph cluster table has reached " + this.entries.size() + " entries");
      }
      return handle;
   }

   public synchronized String textOf(int handle) {
      return this.entryOf(handle).text();
   }

   public synchronized int columnsOf(int handle) {
      return this.entryOf(handle).columns();
   }

   public synchronized int size() {
      return this.entries.size();
   }

   public synchronized long internedTextBytes() {
      return this.internedTextBytes;
   }

   private Entry entryOf(int handle) {
      int index = GlyphEncoding.handleToIndex(handle);
      if (index >= this.entries.size()) {
         throw new IllegalArgumentException("Unknown cluster handle: " + handle);
      }
      return this.entries.get(index);
   }

   private record Entry(String text, int columns) {
   }
}
