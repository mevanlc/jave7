package net.disy.commons.swing.fontchooser.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.model.IFontFamilyNameFilter;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;
import net.disy.commons.swing.fontchooser.util.FontUtilities;

public class FontFamilyPanel extends AbstractFontAdjustListPanel {
   private IFontFamilyNameFilter filter;
   private FontFamilyNameListModel listModel;

   public FontFamilyPanel(FontModel fontModel) {
      this(fontModel, new DefaultFontDialogProperties());
   }

   public FontFamilyPanel(FontModel fontModel, IFontDialogProperties properties) {
      super(fontModel, properties);
   }

   @Override
   protected void updateFontModelView() {
      String fontName = this.getFontModel().getFontFamilyName();
      this.getTextField().setText(fontName);
      this.getList().setSelectedValue(fontName, true);
   }

   @Override
   protected String getTitle() {
      return DisyCommonsSwingFontChooserMessages.getString("FontFamilyPanel.font");
   }

   @Override
   protected JList createListComponent() {
      this.listModel = new FontFamilyNameListModel();
      this.listModel.setFontNames(this.getAvailableFontFamilyNames());
      JList list = new JList(this.listModel);
      list.setCellRenderer(new FontFamilyListCellRenderer(this.getFontDialogProperties()));
      return list;
   }

   @Override
   protected void attachListListener() {
      this.getList().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  String fontFamilyName = (String)FontFamilyPanel.this.getList().getSelectedValue();
                  if (fontFamilyName != null) {
                     FontFamilyPanel.this.getFontModel().setFontFamilyName(fontFamilyName);
                  }
               }
            });
         }
      });
   }

   public void setFontFamilyNameFilter(IFontFamilyNameFilter filter) {
      this.filter = filter;
      this.listModel.setFontNames(this.getAvailableFontFamilyNames());
      String fontFamilyName = this.getFontModel().getFontFamilyName();
      int index = this.listModel.indexOf(fontFamilyName);
      if (index == -1) {
         this.getList().clearSelection();
      } else {
         this.getList().setSelectedIndex(index);
         this.getList().ensureIndexIsVisible(index);
      }
   }

   private String[] getAvailableFontFamilyNames() {
      String[] names = FontUtilities.getAvailableFontFamilyNames();
      if (this.filter == null) {
         return names;
      } else {
         List<String> result = new ArrayList<>();

         for (int i = 0; i < names.length; i++) {
            if (this.filter.accept(names[i])) {
               result.add(names[i]);
            }
         }

         return result.toArray(new String[0]);
      }
   }
}
