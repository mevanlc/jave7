package de.jave.jave.actions.export;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.gui.io.SmartFileFilter;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public final class ResultConverterTextExportFormat implements ITextExportFormat {
   private final ResultConverter converter;

   public ResultConverterTextExportFormat(ResultConverter converter) {
      Ensure.ensureArgumentNotNull(converter);
      this.converter = converter;
   }

   @Override
   public String getId() {
      return this.converter.getId();
   }

   @Override
   public String getName() {
      return this.converter.getName();
   }

   @Override
   public Icon getIcon() {
      return FileTypeIcons.TEXT_ICON;
   }

   @Override
   public boolean isConnectedLinesViewSupported() {
      return false;
   }

   @Override
   public boolean isFontSupported() {
      return false;
   }

   @Override
   public SmartFileFilter[] getFileFilters() {
      return new SmartFileFilter[]{this.converter.getFileFilter()};
   }

   @Override
   public JComponent createPreviewComponent(CharacterPlate plate, ITextExportOptions options) {
      String result = this.convert(plate, options);
      AsciiTextAreaProperties properties = new AsciiTextAreaProperties();
      properties.setEditable(false);
      AsciiTextArea textArea = new AsciiTextArea(new Dimension(40, 20), properties);
      textArea.setText(result);
      textArea.selectAll();
      return textArea.getContent();
   }

   @Override
   public void convertTo(CharacterPlate plate, ITextExportOptions options, Clipboard clipboard) {
      String result = this.convert(plate, options);
      StringSelection sel = new StringSelection(result);
      clipboard.setContents(sel, sel);
   }

   @Override
   public void convertTo(CharacterPlate plate, ITextExportOptions options, File file) throws IOException {
      String result = this.convert(plate, options);
      FileWriter writer = null;

      try {
         writer = new FileWriter(file);
         IOUtilities.copyStream(new StringReader(result), writer);
      } finally {
         IOUtilities.close(writer);
      }
   }

   private String convert(CharacterPlate plate, ITextExportOptions options) {
      CharacterPlate exportPlate;
      if (options.isTrim()) {
         Insets insets = plate.getEmptyInsets();
         int newHeight = plate.getHeight() - insets.bottom - insets.top;
         if (newHeight <= 0) {
            exportPlate = new CharacterPlate(new Dimension(1, 1));
         } else {
            exportPlate = plate.getCopy(new Rectangle(0, insets.top, plate.getWidth(), newHeight));
         }
      } else {
         exportPlate = plate;
      }

      return this.converter.convert(exportPlate);
   }
}
