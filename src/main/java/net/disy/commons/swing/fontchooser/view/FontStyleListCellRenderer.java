package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.swing.font.FontFactory;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class FontStyleListCellRenderer extends AbstractFontListCellRenderer {
   private final FontModel fontModel;

   public FontStyleListCellRenderer(FontModel fontModel, IFontDialogProperties properties) {
      super(properties);
      this.fontModel = fontModel;
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      FontStyle style = (FontStyle)value;
      JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      String fontFamilyName = this.fontModel.getFontFamilyName();
      int size = 12;
      Font font = FontFactory.createFont(fontFamilyName, style, 12);
      label.setFont(this.getDisplayFont(font));
      label.setText(new FontStyleUI().getName(style));
      return label;
   }
}
