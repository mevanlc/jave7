package de.jave.jave.export;

import de.jave.image.swing.ImagePanel;
import de.jave.jave.AsciiToThumbnailConverter;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.border.TitledPanel;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.FontChooserButton;
import net.disy.commons.swing.fontchooser.view.fixedwidth.FixedWidthFontChooserDialogFactory;

public class GifExportOptionsPanel {
   private ImagePanel imagePanel;
   private final JCheckBox cbConnectedLinesView;
   private static final CharacterPlate cpPreview = new CharacterPlate("ABCDEabcde\n12345.:#-+\n_,-'`-._");
   private transient ActionListener actionListener;
   private final JComponent content;
   private final FontModel fontModel;
   private final ColorScheme colorScheme;

   public GifExportOptionsPanel(Font displayFont, boolean preview, ColorScheme colorScheme) {
      this.colorScheme = colorScheme;
      this.fontModel = new FontModel(displayFont);
      this.fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            GifExportOptionsPanel.this.updatePreview();
         }
      });
      FontChooserButton button = new FontChooserButton(this.fontModel, FixedWidthFontChooserDialogFactory.getInstance());
      this.cbConnectedLinesView = new JCheckBox("Connected Lines View", false);
      this.cbConnectedLinesView.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            GifExportOptionsPanel.this.updatePreview();
         }
      });
      JPanel p1 = new JPanel(new FlowLayout(0));
      p1.add(button.getContent());
      p1.add(this.cbConnectedLinesView);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(p1, "North");
      if (preview) {
         this.imagePanel = new ImagePanel(new Dimension(200, 150));
         panel.add(this.imagePanel.getContent(), "Center");
      }

      this.content = new TitledPanel("GIF output options", panel);
      this.updatePreview();
   }

   private void updatePreview() {
      if (this.imagePanel != null) {
         Ascii2ImageOptions options = this.getSelectedOptions();
         this.imagePanel.setImage(AsciiToThumbnailConverter.convert(cpPreview, options));
      }

      if (this.actionListener != null) {
         this.actionListener.actionPerformed(new ActionEvent(this, 0, "font changed"));
      }
   }

   public Ascii2ImageOptions getSelectedOptions() {
      Font fontFont = this.fontModel.getFont();
      boolean connectedLinesView = this.cbConnectedLinesView.isSelected();
      return new Ascii2ImageOptions(fontFont, connectedLinesView, this.colorScheme.getColorText(), this.colorScheme.getColorPlateBackground());
   }

   public synchronized void addActionListener(ActionListener l) {
      this.actionListener = AWTEventMulticaster.add(this.actionListener, l);
   }

   public synchronized void removeActionListener(ActionListener l) {
      this.actionListener = AWTEventMulticaster.remove(this.actionListener, l);
   }

   public JComponent getContent() {
      return this.content;
   }
}
