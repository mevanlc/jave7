package net.jmge.gif.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.io.IOUtilities;
import net.jmge.gif.Gif89Frame;
import net.jmge.gif.IGifFrameProvider;

public class TmpFileFrameContainer implements IGifFrameProvider {
   private final List<File> files = new ArrayList<>();

   public void add(Gif89Frame gifFrame) throws IOException {
      File file = File.createTempFile("asciimation", ".bin");
      FileOutputStream fileOutputStream = null;

      try {
         fileOutputStream = new FileOutputStream(file);
         ObjectOutputStream stream = new ObjectOutputStream(fileOutputStream);
         stream.writeObject(gifFrame);
         stream.flush();
         this.files.add(file);
      } catch (IOException var8) {
         this.cleanUp();
         throw var8;
      } finally {
         IOUtilities.close(fileOutputStream);
      }
   }

   @Override
   public int getSize() {
      return this.files.size();
   }

   @Override
   public Gif89Frame get(int index) throws IOException {
      File file = this.files.get(index);
      FileInputStream fileInputStream = null;

      Gif89Frame var6;
      try {
         fileInputStream = new FileInputStream(file);
         ObjectInputStream stream = new ObjectInputStream(fileInputStream);
         Object gifFrame = stream.readObject();
         var6 = (Gif89Frame)gifFrame;
      } catch (ClassNotFoundException var10) {
         throw new UnreachableCodeReachedException(var10);
      } finally {
         IOUtilities.close(fileInputStream);
      }

      return var6;
   }

   public void cleanUp() {
      for (File file : this.files) {
         file.delete();
      }

      this.files.clear();
   }
}
