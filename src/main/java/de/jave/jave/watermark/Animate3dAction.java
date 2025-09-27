package de.jave.jave.ascii3d;

import de.jave.asciimation.export.FileTypeIcons;
import de.jave.jave.JavEApplication;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.progress.ProgressMonitorDialog;

final class Animate3dAction extends SmartAction {
   private final Render3DTool render3DTool;
   private final JavEApplication application;
   private final Navigate3dModel model;
   private final Filter filter;

   public Animate3dAction(Render3DTool render3DTool, JavEApplication application, Navigate3dModel model, Filter filter) {
      super("Convert to Animation", FileTypeIcons.ANIMATION_ICON);
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(render3DTool);
      Ensure.ensureArgumentNotNull(application);
      Ensure.ensureArgumentNotNull(filter);
      this.model = model;
      this.render3DTool = render3DTool;
      this.application = application;
      this.filter = filter;
   }

   @Override
   protected void execute(Component parentComponent) {
      final JaveAnimationFile animationFile = new JaveAnimationFile();
      ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(parentComponent, "3D Animation");

      try {
         progressDialog.run(new IInterruptableRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor, ICancelable cancelable) throws InterruptedException, InvocationTargetException {
               monitor.beginTask("Render 3D frames", 360);
               Dimension plateSize = Animate3dAction.this.render3DTool.getPreferredPlateSize();
               double dx = plateSize.width / 2;
               double dy = plateSize.height / 2;
               int style = Animate3dAction.this.render3DTool.chStyle.getSelectedIndex();
               String gradient = (String)Animate3dAction.this.render3DTool.tfGradient.getSelectedItem();
               if (gradient.length() == 0) {
                  gradient = " ";
               }

               animationFile.getMetaData().setTitle("3D Rendering Tool");
               animationFile.getMetaData().setDate(Toolbox.getDateString());
               animationFile.getProperties().setBackgroundColor(Color.BLACK);
               animationFile.getProperties().setForegroundColor(Color.WHITE);
               PixelPlate plate = new PixelPlate(new Rectangle(plateSize), Animate3dAction.this.filter);
               plate.setMode(PixelPlateMode.DOT);
               int alpha = Animate3dAction.this.model.getAlpha();
               double zoom = Animate3dAction.this.model.getZoom();
               int stepDegrees = 3;

               for (int angle = 0; angle < 360; angle += 3) {
                  ProgressUtilities.checkInterrupted(cancelable);
                  plate.clear();
                  Render3DTool.render(plate, Animate3dAction.this.render3DTool.taScript.getText(), style, gradient, alpha + angle, 3.0, zoom, dx, dy);
                  CharacterPlate resultPlate = plate.convert();
                  resultPlate.replace('\u0000', ' ');
                  JaveAnimationFrame frame = new JaveAnimationFrame();
                  frame.setContent(resultPlate);
                  animationFile.add(frame);
                  monitor.worked(3);
               }
            }
         });
      } catch (InterruptedException var5) {
         return;
      } catch (InvocationTargetException var6) {
         throw new RuntimeException(var6);
      }

      this.render3DTool.dispose();
      this.application.getDocumentManager().getCurrentDocument().setModified(false);
      this.application.doClose(parentComponent);
      this.application.showToolOptionsDialog();
      this.application.openJaveAnimation(animationFile);
   }
}
