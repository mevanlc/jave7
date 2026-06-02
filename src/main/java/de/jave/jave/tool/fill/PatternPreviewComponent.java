package de.jave.jave.tool.fill;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.jave.actions.ClipboardOverride;
import de.jave.jave.algorithm.fill.FillAlgorithm;
import de.jave.jave.algorithm.fill.FillMatchMode;
import de.jave.jave.pattern.Pattern;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import net.dizzy.commons.swing.component.IComponentContainer;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class PatternPreviewComponent implements IComponentContainer {
   private final AsciiTextArea textArea;

   public PatternPreviewComponent(FontModel fontModel) {
      AsciiTextAreaProperties properties = new AsciiTextAreaProperties(fontModel)
         .setEditable(false)
         .setBackground(SystemColor.control)
         .setScrollingEnabled(false);
      this.textArea = new AsciiTextArea(new Dimension(10, 6), properties);
   }

   @Override
   public JComponent getContent() {
      return this.textArea.getContent();
   }

   public void setToolTipText(String text) {
      this.textArea.setToolTipText(text);
   }

   public void addMouseListener(MouseListener mouseListener) {
      this.textArea.addMouseListener(mouseListener);
   }

   public void setPattern(Pattern pattern) {
      CharacterPlate previewPlate = this.createPreviewPlate(pattern);
      this.textArea.setText(previewPlate.asString());
      this.textArea.scrollToTop();
   }

   public void setClipboardOverride(ClipboardOverride override) {
      this.textArea.getContent().putClientProperty(ClipboardOverride.CLIENT_PROPERTY, override);
   }

   private CharacterPlate createPreviewPlate(Pattern pattern) {
      CharacterPlate result = new CharacterPlate(new Dimension(100, 50));
      FillAlgorithm.fillPattern(result, 0, 0, pattern, FillMatchMode.EQUAL_CHARACTER);
      return result;
   }
}
