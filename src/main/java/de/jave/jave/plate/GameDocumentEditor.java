package de.jave.jave.plate;

import de.jave.jave.CharacterSets;
import de.jave.jave.JavEApplication;
import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.PlatePreferences;
import java.io.File;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class GameDocumentEditor extends AbstractDocumentEditor {
   private final Plate plate;
   private final String stopGapName;

   public GameDocumentEditor(
      String stopGapName,
      PlateDocument doc,
      JavEApplication jave,
      PlatePreferences platePreferences,
      ToolManager toolManager,
      FontModel displayFontModel,
      ObjectModel<ColorScheme> colorSchemeModel,
      CharacterSets characterSets
   ) {
      Ensure.ensureArgumentNotNull(stopGapName);
      Ensure.ensureArgumentNotNull(doc);
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(platePreferences);
      Ensure.ensureArgumentNotNull(toolManager);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(colorSchemeModel);
      Ensure.ensureArgumentNotNull(characterSets);
      this.plate = new Plate(doc, jave, platePreferences, toolManager, displayFontModel, colorSchemeModel, characterSets);
      this.stopGapName = stopGapName;
   }

   @Override
   public void dispose() {
      this.plate.dispose();
   }

   @Override
   public Plate getPlate() {
      return this.plate;
   }

   @Override
   public JComponent getContent() {
      return this.plate.getComponent();
   }

   @Override
   public JaveDocumentType getType() {
      return JaveDocumentType.GAME;
   }

   @Override
   public boolean isModified() {
      return this.plate.getDocument().isModified();
   }

   @Override
   public File getFile() {
      return this.plate.getDocument().getFile();
   }

   @Override
   public String getStopGapName() {
      return this.stopGapName;
   }
}
