package net.jmge.gif;

import java.io.IOException;

public interface IGifFrameProvider {
   int getSize();

   Gif89Frame get(int var1) throws IOException;
}
