package de.jave.asciimation.export;

import de.jave.javeplayer.AnimationProperties;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import net.dizzy.commons.core.exception.MessageException;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.progress.ProgressUtilities;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.dialog.progress.ProgressMonitorDialog;

public class AnimationFileExporter {
   public static void performExport(Component parentComponent, final JaveAnimationFile animationFile, AnimationExportOptions options) {
      final IAnimationExporter exporter = options.getFormat().createExporter(options);
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(parentComponent, "JavE Animation Editor");

      try {
         dialog.run(new IInterruptableRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor, ICancelable cancelable) throws InterruptedException, InvocationTargetException {
               monitor.beginTask("Exporting " + animationFile.getFrameCount() + " frames...", animationFile.getFrameCount());
               Dimension maxFrameSize = new Dimension(0, 0);
               CharacterPlate content = null;

               for (int i = 0; i < animationFile.getFrameCount(); i++) {
                  ProgressUtilities.checkInterrupted(cancelable);
                  JaveAnimationFrame frame = animationFile.getFrame(i);
                  content = new CharacterPlate(frame.getContent());
                  if (content.getWidth() > maxFrameSize.width) {
                     maxFrameSize.width = content.getWidth();
                  }

                  if (content.getHeight() > maxFrameSize.height) {
                     maxFrameSize.height = content.getHeight();
                  }
               }

               try {
                  AnimationProperties properties = animationFile.getProperties();
                  exporter.init(maxFrameSize, properties, animationFile.getFrameCount(), animationFile.getMetaData());

                  for (int i = 0; i < animationFile.getFrameCount(); i++) {
                     ProgressUtilities.checkInterrupted(cancelable);
                     monitor.subTask("Writing frame " + (i + 1));
                     JaveAnimationFrame framex = animationFile.getFrame(i);
                     content = new CharacterPlate(framex.getContent());
                     int[][] chSelection = framex.getSelection();
                     if (chSelection != null) {
                        new CharacterPlate(chSelection).pasteInto(content, framex.getSelectionX(), framex.getSelectionY());
                     }

                     exporter.writeFrame(content);
                     monitor.worked(1);
                  }

                  exporter.finish();
               } catch (InterruptedException var9) {
                  throw var9;
               } catch (Exception var10) {
                  throw new InvocationTargetException(var10);
               }
            }
         });
         String message = "Export finished successfully.";
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("JavE Animation Editor", "Export finished successfully.", MessageType.INFORMATION));
      } catch (InterruptedException var6) {
         exporter.rollBack();
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("JavE Animation Editor", "Export aborted.", MessageType.WARNING));
      } catch (InvocationTargetException var7) {
         exporter.rollBack();
         if (var7.getCause() instanceof OutOfMemoryError) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message("JavE Animation Editor", "Error exporting file: The result file is to big.", var7.getCause())
            );
         } else if (var7.getCause() instanceof MessageException) {
            MessageDialogFactory.showMessageDialog(parentComponent, ((MessageException)var7.getCause()).getMessageObject());
         } else {
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("JavE Animation Editor", "Error exporting file.", var7.getCause()));
         }
      }
   }
}
