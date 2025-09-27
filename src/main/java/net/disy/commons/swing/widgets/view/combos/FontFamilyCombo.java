package net.disy.commons.swing.fontchooser.view.combos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JComboBox;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;
import net.disy.commons.swing.fontchooser.util.FontUtilities;
import net.disy.commons.swing.fontchooser.view.DefaultFontDialogProperties;
import net.disy.commons.swing.fontchooser.view.FontFamilyListCellRenderer;
import net.disy.commons.swing.fontchooser.view.IFontDialogProperties;

public class FontFamilyCombo extends AbstractFontAdjustCombo {
   private final Set<String> validFontNames;

   public FontFamilyCombo(FontModel fontModel) {
      this(fontModel, new TreeSet<>(Arrays.asList(FontUtilities.getAvailableFontFamilyNames())), new DefaultFontDialogProperties());
   }

   public FontFamilyCombo(FontModel fontModel, Set<String> familyNames) {
      this(fontModel, familyNames, new DefaultFontDialogProperties());
   }

   public FontFamilyCombo(FontModel fontModel, IFontDialogProperties properties) {
      this(fontModel, new TreeSet<>(Arrays.asList(FontUtilities.getAvailableFontFamilyNames())), properties);
   }

   public FontFamilyCombo(FontModel fontModel, Set<String> familyNames, IFontDialogProperties properties) {
      super(fontModel, properties);
      Ensure.ensureArgumentNotNull(familyNames);
      this.validFontNames = familyNames;
   }

   @Override
   protected void updateFontModelView() {
      String fontFamilyName = this.getFontModel().getFontFamilyName();
      this.getComboBox().setSelectedItem(fontFamilyName);
   }

   @Override
   protected JComboBox createComboBox() {
      JComboBox comboBox = new JComboBox<>(this.validFontNames.toArray());
      comboBox.setRenderer(new FontFamilyListCellRenderer(this.getFontDialogProperties()));
      comboBox.setEditable(true);
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent event) {
            String selectedFontName = (String)FontFamilyCombo.this.getComboBox().getSelectedItem();
            if (!selectedFontName.equals(FontFamilyCombo.this.getFontModel().getFontFamilyName())) {
               if (FontFamilyCombo.this.validFontNames.contains(selectedFontName)) {
                  FontFamilyCombo.this.getFontModel().setFontFamilyName(selectedFontName);
               } else {
                  FontFamilyCombo.this.rejectUserInput();
               }
            }
         }
      });
      comboBox.setToolTipText(DisyCommonsSwingFontChooserMessages.getString("FontFamilyCombo.Tooltip"));
      return comboBox;
   }
}
