package de.jave.jave.figlet.export;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.file.FigFileName;
import de.jave.figlet.file.IFigFileResource;
import de.jave.figlet.io.FigFontFileParser;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.figlet.util.FigException;
import de.jave.jave.figlet.FigletFontTestDialog;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class FigletExportWizardPage4 extends AbstractFigletExportWizardPage {
   private JCheckBox[] chSmushing;
   private JComboBox chHDefaultLayout;
   private JComboBox chVDefaultLayout;
   private static final String[] SMUSHING = new String[]{
      "Equal character smushing",
      "Underscore smushing",
      "Hierarchy smushing",
      "Opposite pair smushing",
      "Big X smushing",
      "Hardblank smushing",
      "Equal character smushing",
      "Underscore smushing",
      "Hierarchy smushing",
      "Horizontal line smushing",
      "Vertical line supersmushing"
   };
   private final IFigDriver figDriver;
   private static final String FIGLET_TEST_TITLE = "FIGlet Editor - Test new font";
   private static final int[] SMUSHING_SUMMANDS = new int[]{1, 2, 4, 8, 16, 32, 256, 512, 1024, 2048, 4096};

   public FigletExportWizardPage4(FigletExportModel model, IFigDriver figDriver) {
      super(model, "Layout Adjustments", "Take a minute to carefully adjust the layout rules. Also don't forget to test the font and the settings.");
      Ensure.ensureArgumentNotNull(figDriver);
      this.figDriver = figDriver;
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   protected JComponent createContent() {
      JPanel pb1 = new JPanel();
      pb1.setBorder(new TitledBorder("horizontal"));
      pb1.setLayout(new GridDialogLayout(1, false));
      this.chHDefaultLayout = new JComboBox<>(new String[]{"Smushing", "Kerning", "Full Width"});
      this.chHDefaultLayout.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FigletExportWizardPage4.this.updateModel();
         }
      });
      JPanel pb1a = new JPanel();
      pb1a.add(new JLabel("Default layout:"));
      pb1a.add(this.chHDefaultLayout);
      pb1.add(pb1a);
      this.chSmushing = new JCheckBox[11];

      for (int i = 0; i < 6; i++) {
         this.chSmushing[i] = new JCheckBox(SMUSHING[i], true);
         this.chSmushing[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               FigletExportWizardPage4.this.updateModel();
            }
         });
         pb1.add(this.chSmushing[i]);
      }

      JPanel pb2 = new JPanel();
      pb2.setBorder(new TitledBorder("vertical"));
      pb2.setLayout(new GridDialogLayout(1, false));
      this.chVDefaultLayout = new JComboBox<>(new String[]{"Smushing", "Fitting", "Full Height"});
      this.chVDefaultLayout.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FigletExportWizardPage4.this.updateModel();
         }
      });
      JPanel pb2a = new JPanel();
      pb2a.add(new JLabel("Default layout:"));
      pb2a.add(this.chVDefaultLayout);
      pb2.add(pb2a);

      for (int i = 6; i < 11; i++) {
         this.chSmushing[i] = new JCheckBox(SMUSHING[i], true);
         this.chSmushing[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               FigletExportWizardPage4.this.updateModel();
            }
         });
         pb2.add(this.chSmushing[i]);
      }

      JPanel mainPanel = new JPanel(new GridLayout(0, 2));
      mainPanel.add(pb1);
      mainPanel.add(pb2);
      SmartAction bTest = new SmartAction("Verify the settings by testing the font.", FigletIcons.FIGLET_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            try {
               FigletExportWizardPage4.this.testFont(parentComponent);
            } catch (IOException var3) {
               var3.printStackTrace();
            } catch (FigException var4) {
               var4.printStackTrace();
            }
         }
      };
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(mainPanel, GridDialogLayoutData.FILL_BOTH);
      panel.add(new JButton(bTest));
      return panel;
   }

   private void testFont(Component parentComponent) throws IOException, FigException {
      final File file = File.createTempFile("testFont", ".flf");
      file.deleteOnExit();
      FigletFileExporter.export(this.getModel(), file);
      FigFont font = new FigFontFileParser().load(new FigFileName("test font"), new IFigFileResource() {
         @Override
         public FigFileName[] loadFigletFileNames() throws FigException {
            throw new UnsupportedOperationException();
         }

         @Override
         public InputStream openConfigurationFileInputStream(String fileName) throws FigException {
            throw new UnsupportedOperationException();
         }

         @Override
         public InputStream openFigFileInputStream(FigFileName fileName) throws FigException {
            try {
               return new FileInputStream(file);
            } catch (FileNotFoundException var3) {
               throw new FigException("Font file not found.", var3, 7);
            }
         }

         @Override
         public String getRourceBaseDescription() {
            throw new UnsupportedOperationException();
         }

         @Override
         public long getLastModified(FigFileName fileName) throws FigException {
            throw new UnsupportedOperationException();
         }
      });
      FigletFontTestDialog testFigletDialog = new FigletFontTestDialog(parentComponent, "FIGlet Editor - Test new font", font, this.figDriver);
      testFigletDialog.setText("Testing this great\nnew Font!");
      testFigletDialog.show();
   }

   private void updateModel() {
      int hSmushing = 0;

      for (int i = 0; i < 6; i++) {
         if (this.chSmushing[i].isSelected()) {
            hSmushing += SMUSHING_SUMMANDS[i];
         }
      }

      int vSmushing = 0;

      for (int ix = 6; ix < 11; ix++) {
         if (this.chSmushing[ix].isSelected()) {
            vSmushing += SMUSHING_SUMMANDS[ix];
         }
      }

      int oldLayout = 0;
      int fullLayout = hSmushing + vSmushing;
      switch (this.chHDefaultLayout.getSelectedIndex()) {
         case 0:
            fullLayout += 128;
            oldLayout = hSmushing;
            break;
         case 1:
            fullLayout += 64;
            oldLayout = 0;
            break;
         case 2:
            fullLayout += 0;
            oldLayout = -1;
      }

      switch (this.chVDefaultLayout.getSelectedIndex()) {
         case 0:
            fullLayout += 16384;
            break;
         case 1:
            fullLayout += 8192;
         case 2:
      }

      this.getModel().setLayout(oldLayout, fullLayout);
   }

   @Override
   public boolean canFinish() {
      return this.getModel().canFinish();
   }

   @Override
   public void requestFocus() {
   }
}
