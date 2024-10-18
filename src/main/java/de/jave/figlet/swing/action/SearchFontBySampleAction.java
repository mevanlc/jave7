package de.jave.figlet.swing.action;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.file.FigFileName;
import de.jave.figlet.file.IFigFileLibrary;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.figlet.util.FigException;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.progress.ProgressMonitorDialog;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public final class SearchFontBySampleAction extends SmartAction {
   private static final String SAMPLE_TEXT = "abcdefghijklmnopqrstuvwxyz1234567890 a b c d e f g h i j k l m n o p q r s t u v w x y z ABCDEFGHIJKLMNOPQRSTUVWXYZ A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
   private final IFigDriver figDriver;
   private final File tmpFolder;

   public SearchFontBySampleAction(File tmpFolder, IFigDriver figDriver) {
      super(FigletIcons.DYNAMICALY_GENERATED_FONT_CATEGORY_ICON);
      Ensure.ensureArgumentNotNull(tmpFolder);
      Ensure.ensureArgumentNotNull(figDriver);
      this.tmpFolder = tmpFolder;
      this.figDriver = figDriver;
   }

   @Override
   protected void execute(Component parentComponent) {
      FontSampleInputDialogPage dialogPage = new FontSampleInputDialogPage();
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<>(dialogPage));
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         String searchSample = dialogPage.getSampleText();
         final CharacterRanking ranking = CharacterRanking.compile(searchSample);
         IFigFileLibrary fileLibrary = this.figDriver.getFileLibrary();
         final String[] fontNames = fileLibrary.getAllFontNames();
         ProgressMonitorDialog dialog = new ProgressMonitorDialog(parentComponent, "Create Samples");

         try {
            dialog.run(new IInterruptableRunnableWithProgress() {
               @Override
               public void run(IProgressMonitor monitor, ICancelable cancelable) throws InterruptedException, InvocationTargetException {
                  long time0 = System.currentTimeMillis();
                  SearchFontBySampleAction.this.createFontSamplesIfNeccessary(monitor, fontNames, cancelable);
                  monitor.beginTask("Comparing font samples", fontNames.length);
                  List<RankedItem<String>> rankedItems = new ArrayList<>();

                  for (String fontName : fontNames) {
                     ProgressUtilities.checkInterrupted(cancelable);
                     monitor.subTask(fontName);

                     try {
                        FigLetFontSample sample = FigLetFontSample.load(SearchFontBySampleAction.this.getSampleFile(fontName));
                        double rank = ranking.rank(sample);
                        rankedItems.add(new RankedItem<>(rank, fontName));
                     } catch (IOException var13) {
                        var13.printStackTrace();
                     }

                     monitor.worked(1);
                  }

                  Collections.sort(rankedItems);

                  for (RankedItem<String> item : rankedItems) {
                     System.err.println(item.getRank() + " " + item.getValue());
                  }

                  long time1 = System.currentTimeMillis();
                  System.err.println(time1 - time0 + "ms");
               }
            });
         } catch (InterruptedException var11) {
            return;
         } catch (InvocationTargetException var12) {
            var12.printStackTrace();
         }
      }
   }

   private void createFontSamplesIfNeccessary(IProgressMonitor monitor, String[] fontNames, ICancelable cancelable) throws InterruptedException {
      monitor.beginTask("Creating font samples", fontNames.length);

      for (String fontName : fontNames) {
         ProgressUtilities.checkInterrupted(cancelable);
         monitor.subTask(fontName);

         try {
            File file = this.getSampleFile(fontName);
            if (!file.exists() || file.lastModified() < this.figDriver.getFileLibrary().getFileResource().getLastModified(new FigFileName(fontName))) {
               FigLetFontSample sample = this.createFontSample(fontName);
               sample.write(file);
            }
         } catch (FigException var10) {
            var10.printStackTrace();
         } catch (IOException var11) {
            var11.printStackTrace();
         }

         monitor.worked(1);
      }
   }

   private FigLetFontSample createFontSample(String fontName) throws FigException {
      String sampleText = this.figDriver
         .figletize(
            "abcdefghijklmnopqrstuvwxyz1234567890 a b c d e f g h i j k l m n o p q r s t u v w x y z ABCDEFGHIJKLMNOPQRSTUVWXYZ A B C D E F G H I J K L M N O P Q R S T U V W X Y Z",
            fontName
         );
      char[] characters = CharacterRanking.getContainedCharacters(sampleText);
      return new FigLetFontSample(characters, sampleText);
   }

   private File getSampleFile(String fontName) {
      return new File(this.tmpFolder, fontName + ".flfsample");
   }
}
