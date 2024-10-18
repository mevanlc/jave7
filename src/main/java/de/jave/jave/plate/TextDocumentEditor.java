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
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class TextDocumentEditor extends AbstractDocumentEditor {
   private final Plate plate;
   private final String stopGapName;

   public TextDocumentEditor(
      String stopGapName,
      PlateDocument doc,
      JavEApplication jave,
      PlatePreferences platePreferences,
      ToolManager toolManager,
      FontModel displayFontModel,
      ColorScheme colorScheme,
      CharacterSets characterSets
   ) {
      Ensure.ensureArgumentNotNull(stopGapName);
      Ensure.ensureArgumentNotNull(doc);
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(platePreferences);
      Ensure.ensureArgumentNotNull(toolManager);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(colorScheme);
      Ensure.ensureArgumentNotNull(characterSets);
      this.stopGapName = stopGapName;
      this.plate = new Plate(doc, jave, platePreferences, toolManager, displayFontModel, new ObjectModel<>(colorScheme), characterSets);
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
      return JaveDocumentType.TEXT;
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
