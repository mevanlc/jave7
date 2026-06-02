package de.jave.jave.figlet;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.figlet.engine.FigConversionJobProcessor;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.processing.FigletJobFactory;
import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.JaveGlobalRessources;
import de.jave.lib.job.IResultConsumer;
import de.jave.lib.job.NullWarningCollector;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.dizzy.commons.core.exception.PrintStackTraceExceptionHandler;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;

public class FigletFontTestDialog {
   private static final String INITIAL_TEXT = "Figlet";
   private final AsciiTextArea taInput;
   private final AsciiTextArea taOutput;
   private final JDialog dialog;
   private final IFigDriver figDriver;
   private final AsynchronousDroppingJobProcessor<IFigletJob> processor;
   private final FigFont predefinedFont;

   public FigletFontTestDialog(Component parentComponent, String title, FigFont predefinedFont, IFigDriver figDriver) {
      Ensure.ensureArgumentNotNull(predefinedFont);
      Ensure.ensureArgumentNotNull(figDriver);
      this.predefinedFont = predefinedFont;
      this.figDriver = figDriver;
      this.dialog = JDialogFactory.createJDialog(parentComponent, title, true);
      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            FigletFontTestDialog.this.close();
         }

         @Override
         public void windowActivated(WindowEvent e) {
            FigletFontTestDialog.this.taInput.requestFocus();
         }
      });
      AsciiTextAreaProperties properties = new AsciiTextAreaProperties(new FontModel(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH));
      this.taOutput = new AsciiTextArea(new Dimension(80, 15), properties);
      properties.setEditable(false);
      AsciiTextAreaProperties inputProperties = new AsciiTextAreaProperties();
      this.taInput = new AsciiTextArea(new Dimension(80, 5), inputProperties);
      this.taInput.setText("Figlet");
      this.taInput.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            FigletFontTestDialog.this.figletize();
         }
      });
      JPanel p = new JPanel(new GridDialogLayout(1, false));
      p.add(new JLabel("Preview:"));
      p.add(this.taOutput.getContent(), GridDialogLayoutData.FILL_BOTH);
      p.add(new JLabel("Text:"));
      p.add(this.taInput.getContent(), GridDialogLayoutData.FILL_BOTH);
      this.dialog.getContentPane().add(p, "Center");
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      SmartAction closeAction = new SmartAction("&Close") {
         @Override
         protected void execute(Component parentComponent) {
            FigletFontTestDialog.this.close();
         }
      };
      buttonPanelBuilder.add(closeAction);
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      FigConversionJobProcessor figProcessor = new FigConversionJobProcessor(figDriver, new IResultConsumer() {
         @Override
         public void putResult(Object o) {
            FigletFontTestDialog.this.taOutput.setText(String.valueOf(o));
         }
      }, new NullWarningCollector());
      this.processor = new AsynchronousDroppingJobProcessor<>(figProcessor, new PrintStackTraceExceptionHandler());
      this.dialog.pack();
   }

   public void setText(String text) {
      this.taInput.setText(text);
   }

   public void show() {
      this.dialog.setVisible(true);
      if (this.taInput != null) {
         this.taInput.selectAll();
         this.taInput.requestFocus();
      }
   }

   private void figletize() {
      String text = this.taInput.getText();
      this.processor.startJob(FigletJobFactory.createJob(text, this.predefinedFont, this.predefinedFont.getLayout()));
   }

   private void close() {
      this.dialog.setVisible(false);
   }

   public void dispose() {
      this.dialog.dispose();
   }

   public JDialog getDialog() {
      return this.dialog;
   }
}
