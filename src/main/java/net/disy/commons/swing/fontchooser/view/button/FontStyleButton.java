package net.disy.commons.swing.fontchooser.view.button;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.core.text.font.FontStyleProperty;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.font.FontFactory;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;
import net.disy.commons.swing.fontchooser.view.IFontModelView;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class FontStyleButton implements IFontModelView {
   private final JToggleButton button;
   private final FontModel fontModel;
   private final FontStyleProperty fontStyle;

   public static FontStyleButton createBoldButton(FontModel fontModel) {
      return new FontStyleButton(fontModel, FontStyleProperty.BOLD);
   }

   public static FontStyleButton createItalicsButton(FontModel fontModel) {
      return new FontStyleButton(fontModel, FontStyleProperty.ITALICS);
   }

   private FontStyleButton(FontModel fontModel, FontStyleProperty fontStyle) {
      this.fontModel = fontModel;
      this.fontStyle = fontStyle;
      this.button = this.createButton(new Font("Serif", FontFactory.getAwtStyle(fontStyle), 15), "FontStyleButtons." + fontStyle.getName());
      fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FontStyleButton.this.updateFontModelView();
         }
      });
      this.updateFontModelView();
   }

   protected void updateFontModelView() {
      FontStyle style = this.fontModel.getFontStyle();
      this.button.setSelected(style.isEnabled(this.fontStyle));
   }

   private JToggleButton createButton(final Font font, String name) {
      final String letter = DisyCommonsSwingFontChooserMessages.getString(name);
      final JToggleButton toggleButton = new JToggleButton();
      Icon icon = new Icon() {
         private static final int SIZE = 16;

         @Override
         public int getIconHeight() {
            return 16;
         }

         @Override
         public int getIconWidth() {
            return 16;
         }

         @Override
         public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setFont(font);
            if (toggleButton.isEnabled()) {
               g.setColor(SwingColors.getTextAreaForegroundColor());
            } else {
               g.setColor(SwingColors.getTextAreaInactiveForegroundColor());
            }

            FontMetrics fontMetrics = g.getFontMetrics(font);
            int width = (int)fontMetrics.getStringBounds(letter, g).getWidth();
            int xpos = x + (16 - width) / 2;
            int ypos = y - 3 + 16 - (16 - fontMetrics.getAscent()) / 2;
            g.drawString(letter, xpos, ypos);
         }
      };
      toggleButton.setIcon(icon);
      toggleButton.setFocusPainted(false);
      toggleButton.setToolTipText(DisyCommonsSwingFontChooserMessages.getString(name + ".ToolTip"));
      toggleButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FontStyleButton.this.updateFontStyle();
         }
      });
      toggleButton.setPreferredSize(LayoutUtilities.TOOLBAR_BUTTON_SIZE);
      return toggleButton;
   }

   public JToggleButton getContent() {
      return this.button;
   }

   protected void updateFontStyle() {
      this.fontModel.setFontStyle(this.fontModel.getFontStyle().derive(this.fontStyle, this.button.isSelected()));
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.button.setEnabled(enabled);
   }

   @Override
   public boolean isEnabled() {
      return this.button.isEnabled();
   }

   @Override
   public FontModel getFontModel() {
      return this.fontModel;
   }
}
