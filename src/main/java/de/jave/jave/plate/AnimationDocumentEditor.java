package de.jave.jave.plate;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.asciimation.editor.AnimationEditorPanel;
import de.jave.jave.CharacterSets;
import de.jave.jave.DocumentListener;
import de.jave.jave.JavEApplication;
import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class AnimationDocumentEditor extends AbstractDocumentEditor {
   private final Plate plate;
   private final JComponent content;
   private final AnimationEditorPanel animationEditorPanel;
   private final String stopGapName;

   public AnimationDocumentEditor(
      String stopGapName,
      JaveAnimationFile animationFile,
      JavEApplication jave,
      final PlateDocument document,
      PlatePreferences platePreferences,
      JaveApplicationPreferences preferences,
      ToolManager toolManager,
      AnimationExportPreferences animationExportPreferences,
      FileModel currentDirectoryModel,
      CharacterSets characterSets
   ) {
      Ensure.ensureArgumentNotNull(stopGapName);
      Ensure.ensureArgumentNotNull(animationFile);
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(platePreferences);
      Ensure.ensureArgumentNotNull(toolManager);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(characterSets);
      this.stopGapName = stopGapName;
      document.setFile(animationFile.getFile());
      if (animationFile.getFrameCount() > 0) {
         document.setContent(new CharacterPlate(animationFile.getFrame(0).getContent()));
      }

      document.setColorScheme(ColorScheme.BLACK_ON_WHITE);
      this.plate = new Plate(
         document,
         jave,
         platePreferences,
         toolManager,
         preferences.getDisplayFontModel(),
         new ObjectModel<>(preferences.getDefaultColorSchemeModel().getValue()),
         characterSets
      );
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(this.plate.getComponent(), "Center");
      AnimationEditorModel model = new AnimationEditorModel();
      model.setAnimationFile(animationFile);
      this.animationEditorPanel = new AnimationEditorPanel(model, currentDirectoryModel, this.plate, preferences, animationExportPreferences);
      panel.add(this.animationEditorPanel.getContent(), "South");
      this.content = panel;
      this.animationEditorPanel.getCurrentFrameIndexModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationDocumentEditor.this.updatePlateContent();
         }
      });
      this.animationEditorPanel.getModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationDocumentEditor.this.updatePlateContent();
         }
      });
      document.addDocumentListener(new DocumentListener() {
         @Override
         public void documentChanged() {
            // Layers: animation remains a single CharacterPlate/frame path in MVP1.
            // Do not feed layered composites here without revisiting animation support.
            AnimationDocumentEditor.this.animationEditorPanel.getModel().setCurrentFrameContent(document.getContent());
         }

         @Override
         public void documentShowing() {
         }

         @Override
         public void documentHiding() {
         }

         @Override
         public void documentClosing() {
         }
      });
   }

   @Override
   public void dispose() {
      this.plate.dispose();
   }

   private void updatePlateContent() {
      JaveAnimationFile animationFile = this.animationEditorPanel.getModel().getAnimationFile();
      int currentFrameIndex = this.animationEditorPanel.getCurrentFrameIndexModel().getCurrentFrameIndex();
      this.plate.setContent(new CharacterPlate(animationFile.getFrame(currentFrameIndex).getContent()));
   }

   @Deprecated
   public AnimationEditorPanel getAnimationEditorPanel() {
      return this.animationEditorPanel;
   }

   @Override
   public Plate getPlate() {
      return this.plate;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public JaveDocumentType getType() {
      return JaveDocumentType.ANIMATION;
   }

   public AnimationEditorModel getModel() {
      return this.animationEditorPanel.getModel();
   }

   @Override
   public boolean isModified() {
      return this.getModel().isModified();
   }

   @Override
   public File getFile() {
      return this.getModel().getAnimationFile().getFile();
   }

   @Override
   public String getStopGapName() {
      return this.stopGapName;
   }
}
