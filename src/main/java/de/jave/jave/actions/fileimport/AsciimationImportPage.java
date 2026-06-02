package de.jave.jave.actions.fileimport;

import de.jave.jave.PlateDocument;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.javeplayer.JavePlayer;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class AsciimationImportPage extends AbstractImportWizardPage {
   private static final String DEFAULT_MESSAGE = "Select the options for ascii animation import. You can adjust how the file is separated into frames and preview the result in the player panel.";
   private AsciimationImportOptionsPanel optionsPanel;

   public AsciimationImportPage(ImportWizard wizard, ImportWizardModel model) {
      super(
         "Ascii animation import options",
         "Select the options for ascii animation import. You can adjust how the file is separated into frames and preview the result in the player panel.",
         wizard,
         model
      );
   }

   @Override
   protected JComponent createContent() {
      this.getModel().getAsciimationOptionsModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciimationImportPage.this.performImport();
         }
      });
      this.getModel().getImportedAnimationModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciimationImportPage.this.checkInputValid();
         }
      });
      this.optionsPanel = new AsciimationImportOptionsPanel(this.getModel().getAsciimationOptionsModel());
      final JavePlayer javePlayer = new JavePlayer();
      this.getModel().getImportedAnimationModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            javePlayer.setAnimationFile(AsciimationImportPage.this.getModel().getImportedAnimationModel().getAnimationFile());
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JLabel("Options:"));
      GridDialogLayoutData insettedData = new GridDialogLayoutData();
      insettedData.setHorizontalIndent(22);
      panel.add(this.optionsPanel.getContent(), insettedData);
      panel.add(new JLabel("Preview:"));
      GridDialogLayoutData playerData = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      playerData.setHorizontalIndent(22);
      panel.add(javePlayer, playerData);
      return panel;
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public void requestFocus() {
      this.optionsPanel.requestFocus();
   }

   @Override
   public boolean canFinish() {
      return this.getModel().getImportedAnimationModel().getAnimationFile() != null;
   }

   @Override
   public void enter() {
      if (this.getModel().getImportedAnimationModel().getAnimationFile() == null
         || !this.getModel().getImportedAnimationModel().getSourceFile().equals(this.getModel().getSourceFileModel().getValue())) {
         this.performImport();
      }
   }

   private void performImport() {
      try {
         if (this.getModel().getSourceFileContent() == null) {
            String[] lines = PlateDocument.readAsciiFileLines(this.getModel().getSourceFileModel().getValue());
            this.getModel().setSourceFileContent(lines);
            AsciimationImportHeuristic.initialize(lines, this.getModel().getAsciimationOptionsModel());
            return;
         }

         String[] lines = this.getModel().getSourceFileContent();
         JaveAnimationFile animationFile = new JaveAnimationFile();
         animationFile.getProperties().setFrameDuration(this.getModel().getAsciimationOptionsModel().getFrameDuration());
         int lineIndex = 0;
         lineIndex += this.getModel().getAsciimationOptionsModel().getSkipTop();

         while (lineIndex < lines.length) {
            int frameHeight = this.getModel().getAsciimationOptionsModel().getFrameHeight();
            if (frameHeight + lineIndex > lines.length) {
               frameHeight = lines.length - lineIndex;
            }

            String[] currentLines = new String[frameHeight];
            System.arraycopy(lines, lineIndex, currentLines, 0, frameHeight);
            JaveAnimationFrame frame = new JaveAnimationFrame();
            frame.setContent(new CharacterPlate(currentLines));
            animationFile.add(frame);
            lineIndex += frameHeight;
            lineIndex += this.getModel().getAsciimationOptionsModel().getFrameGap();
         }

         adjustFrameSizes(animationFile);
         this.getModel().getImportedAnimationModel().setAnimation(this.getModel().getSourceFileModel().getValue(), animationFile);
      } catch (IOException var7) {
         this.getModel().getImportedAnimationModel().setAnimation(this.getModel().getSourceFileModel().getValue(), null);
         var7.printStackTrace();
      }
   }

   private static void adjustFrameSizes(JaveAnimationFile animationFile) {
      int maxWidth = 0;
      int maxHeight = 0;

      for (int i = 0; i < animationFile.getFrameCount(); i++) {
         Dimension size = new CharacterPlate(animationFile.getFrame(i).getContent()).getSize();
         if (size.width > maxWidth) {
            maxWidth = size.width;
         }

         if (size.height > maxHeight) {
            maxHeight = size.height;
         }
      }

      for (int i = 0; i < animationFile.getFrameCount(); i++) {
         CharacterPlate plate = new CharacterPlate(animationFile.getFrame(i).getContent());
         plate.setSize(maxWidth, maxHeight);
         animationFile.getFrame(i).setContent(plate);
      }
   }
}
