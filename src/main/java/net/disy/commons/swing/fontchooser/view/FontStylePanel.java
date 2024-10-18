package net.disy.commons.swing.fontchooser.view;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class FontStylePanel extends AbstractFontAdjustListPanel {
   public FontStylePanel(FontModel fontModel) {
      this(fontModel, new DefaultFontDialogProperties());
   }

   public FontStylePanel(FontModel fontModel, IFontDialogProperties properties) {
      super(fontModel, properties);
   }

   @Override
   protected void updateFontModelView() {
      FontStyle style = this.getFontModel().getFontStyle();
      this.getTextField().setText(new FontStyleUI().getName(style));
      this.getList().setSelectedValue(style, true);
   }

   @Override
   protected String getTitle() {
      return DisyCommonsSwingFontChooserMessages.getString("FontStylePanel.style");
   }

   @Override
   protected JList createListComponent() {
      JList list = new JList<>(FontStyle.values());
      list.setCellRenderer(new FontStyleListCellRenderer(this.getFontModel(), this.getFontDialogProperties()));
      return list;
   }

   @Override
   protected void attachListListener() {
      this.getList().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            FontStyle fontStyle = (FontStyle)FontStylePanel.this.getList().getSelectedValue();
            if (fontStyle != null) {
               FontStylePanel.this.getFontModel().setFontStyle(fontStyle);
            }
         }
      });
   }
}
