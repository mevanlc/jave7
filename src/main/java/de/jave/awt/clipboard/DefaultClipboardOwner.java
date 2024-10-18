package de.jave.awt.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

public class DefaultClipboardOwner implements ClipboardOwner {
   private static final DefaultClipboardOwner instance = new DefaultClipboardOwner();

   private DefaultClipboardOwner() {
   }

   public static DefaultClipboardOwner getInstance() {
      return instance;
   }

   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents) {
   }
}
