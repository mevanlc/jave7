package de.jave.jave.open;

import de.jave.jave.JavEApplication;
import de.jave.jave.browser.JaveFileType;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dizzy.commons.core.exception.UnreachableCodeReachedException;
import net.dizzy.commons.core.util.Ensure;

public class JaveDropFileOpener implements DropTargetListener {
   private final JavEApplication application;

   private JaveDropFileOpener(JavEApplication application) {
      Ensure.ensureArgumentNotNull(application);
      this.application = application;
   }

   @Override
   public void dragEnter(DropTargetDragEvent event) {
      if (this.isSupported(event)) {
         event.acceptDrag(1);
      }
   }

   private boolean isSupported(DropTargetDragEvent event) {
      return event.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
   }

   @Override
   public void dragOver(DropTargetDragEvent event) {
      if (this.isSupported(event)) {
         event.acceptDrag(1);
      }
   }

   @Override
   public void dropActionChanged(DropTargetDragEvent event) {
      if (this.isSupported(event)) {
         event.acceptDrag(1);
      }
   }

   @Override
   public void dragExit(DropTargetEvent event) {
   }

   @Override
   public void drop(DropTargetDropEvent event) {
      if (!event.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
         event.rejectDrop();
      } else {
         event.acceptDrop(1);
         Transferable transferable = event.getTransferable();

         try {
            List<File> files = (List<File>)transferable.getTransferData(DataFlavor.javaFileListFlavor);
            final File file = files.get(0);
            if (!file.isFile()) {
               event.dropComplete(false);
            } else {
               JaveFileType fileType = JaveFileType.guessType(file);
               if (fileType == null) {
                  event.dropComplete(false);
               } else {
                  final Component parentComponent = event.getDropTargetContext().getComponent();
                  SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                        JaveDropFileOpener.this.application.getFrame().toFront();
                        JaveDropFileOpener.this.application.open(parentComponent, file);
                     }
                  });
                  event.dropComplete(true);
               }
            }
         } catch (UnsupportedFlavorException var7) {
            event.dropComplete(false);
            throw new UnreachableCodeReachedException(var7);
         } catch (IOException var8) {
            event.dropComplete(false);
         }
      }
   }

   public static void attachTo(JavEApplication application, JComponent component) {
      new DropTarget(component, new JaveDropFileOpener(application));
   }
}
