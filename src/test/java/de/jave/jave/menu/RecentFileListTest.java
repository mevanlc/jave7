package de.jave.jave.menu;

import de.jave.preferences.SmartPreferences;
import de.jave.util.RecentFileList;
import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Assert;
import org.junit.Test;

public class RecentFileListTest {
   @Test
   public void dynamicMenuUpdatesOnceAndOpensTheCapturedFileAfterReordering() throws Exception {
      RecentFileList files = new RecentFileList(new SmartPreferences(new ProbePreferencesFactory().userRoot()));
      File first = Files.createTempFile("jave-recent-first", ".txt").toFile();
      File second = Files.createTempFile("jave-recent-second", ".txt").toFile();
      try {
         AtomicInteger changes = new AtomicInteger();
         AtomicReference<File> opened = new AtomicReference<>();
         files.addChangeListener(changes::incrementAndGet);
         files.setRecentFileOpenListener((parent, file) -> opened.set(file));
         files.add(first);
         files.add(second);
         files.add(new File(first.getParentFile(), "./" + first.getName()));
         Assert.assertEquals(3, changes.get());
         Assert.assertEquals(2, files.getSize());
         files.open(second, null);
         Assert.assertEquals(second, opened.get());
         Files.delete(second.toPath());
         files.check();
         Assert.assertEquals(4, changes.get());
         Assert.assertArrayEquals(new File[]{first}, files.getFiles());
      } finally {
         Files.deleteIfExists(first.toPath());
         Files.deleteIfExists(second.toPath());
      }
   }
}
