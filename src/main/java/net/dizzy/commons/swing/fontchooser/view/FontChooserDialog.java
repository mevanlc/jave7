package net.dizzy.commons.swing.fontchooser.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import net.dizzy.commons.swing.dialog.core.DialogResult;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.fontchooser.util.FontUtilities;

public class FontChooserDialog {
   private static final String DEFAULT_TITLE = "Font";
   private static final String PREVIEW_TEXT = "AaBbYyZz 0123456789 ┌─┬─┐";
   private static final String[] STYLE_NAMES = {"Plain", "Bold", "Italic", "Bold italic"};
   private static final int[] STYLES = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};
   private static final int FONT_LIST_VISIBLE_ROWS = 12;
   private static final int FONT_LIST_WIDTH = 280;

   private final Component parent;
   private final FontModel model;
   private final boolean fixedWidthOnly;

   public FontChooserDialog(Component parent, FontModel model) {
      this(parent, model, false);
   }

   public FontChooserDialog(Component parent, FontModel model, boolean fixedWidthOnly) {
      this.parent = parent;
      this.model = model;
      this.fixedWidthOnly = fixedWidthOnly;
   }

   public IDialogResult show() {
      return show(DEFAULT_TITLE);
   }

   public IDialogResult show(String title) {
      Font initialFont = model.getFont();
      String[] fontFamilies = availableFontFamilies(fixedWidthOnly);
      JList<String> familyList = new JList<>(fontFamilies);
      familyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      JScrollPane familyScrollPane = createFamilyScrollPane(familyList);
      familyList.setSelectedValue(initialFont.getFamily(), true);
      if (familyList.isSelectionEmpty() && fontFamilies.length > 0) {
         familyList.setSelectedIndex(0);
      }

      JComboBox<String> styleCombo = new JComboBox<>(STYLE_NAMES);
      styleCombo.setSelectedIndex(styleIndex(initialFont.getStyle()));
      JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(initialFont.getSize(), 6, 144, 1));
      JLabel preview = new JLabel(PREVIEW_TEXT, JLabel.CENTER);
      preview.setBorder(BorderFactory.createTitledBorder("Sample"));
      preview.setFont(initialFont);

      Runnable updatePreview = () -> preview.setFont(
         selectedFont(familyList.getSelectedValue(), styleCombo.getSelectedIndex(), (Integer)sizeSpinner.getValue(), initialFont)
      );
      familyList.addListSelectionListener(event -> {
         if (!event.getValueIsAdjusting()) {
            updatePreview.run();
         }
      });
      styleCombo.addActionListener(event -> updatePreview.run());
      sizeSpinner.addChangeListener(event -> updatePreview.run());

      int option = JOptionPane.showConfirmDialog(
         parent,
         createContent(familyScrollPane, styleCombo, sizeSpinner, preview),
         title,
         JOptionPane.OK_CANCEL_OPTION,
         JOptionPane.PLAIN_MESSAGE
      );
      if (option != JOptionPane.OK_OPTION) {
         return DialogResult.CANCELED;
      }

      model.setFont(selectedFont(familyList.getSelectedValue(), styleCombo.getSelectedIndex(), (Integer)sizeSpinner.getValue(), initialFont));
      return DialogResult.OK;
   }

   public Font getFont() {
      return model.getFont();
   }

   static String[] availableFontFamilies(boolean fixedWidthOnly) {
      Stream<String> families = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
      if (fixedWidthOnly) {
         families = families.filter(name -> FontUtilities.isFixedWidth(new Font(name, Font.PLAIN, 12)));
      }
      return families.sorted(String.CASE_INSENSITIVE_ORDER).toArray(String[]::new);
   }

   static int styleIndex(int style) {
      for (int i = 0; i < STYLES.length; i++) {
         if (STYLES[i] == style) {
            return i;
         }
      }
      return 0;
   }

   static JScrollPane createFamilyScrollPane(JList<String> familyList) {
      int rowHeight = familyList.getFontMetrics(familyList.getFont()).getHeight() + 2;
      familyList.setFixedCellHeight(rowHeight);
      familyList.setVisibleRowCount(FONT_LIST_VISIBLE_ROWS);

      JScrollPane scrollPane = new JScrollPane(familyList);
      scrollPane.getViewport().setPreferredSize(new Dimension(FONT_LIST_WIDTH, rowHeight * FONT_LIST_VISIBLE_ROWS));
      Dimension fixedSize = scrollPane.getPreferredSize();
      scrollPane.setPreferredSize(fixedSize);
      scrollPane.setMinimumSize(fixedSize);
      return scrollPane;
   }

   private static Font selectedFont(String family, int styleIndex, int size, Font fallback) {
      String selectedFamily = family == null ? fallback.getFamily() : family;
      int selectedStyle = styleIndex >= 0 && styleIndex < STYLES.length ? STYLES[styleIndex] : Font.PLAIN;
      return new Font(selectedFamily, selectedStyle, size);
   }

   private static JPanel createContent(JScrollPane familyScrollPane, JComboBox<String> styleCombo, JSpinner sizeSpinner, JLabel preview) {
      JPanel controls = new JPanel(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(4, 4, 4, 4);
      constraints.anchor = GridBagConstraints.WEST;

      constraints.gridx = 0;
      constraints.gridy = 0;
      controls.add(new JLabel("Font:"), constraints);
      constraints.gridx = 1;
      constraints.weightx = 1;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      controls.add(familyScrollPane, constraints);

      constraints.gridx = 0;
      constraints.gridy = 1;
      constraints.weightx = 0;
      constraints.fill = GridBagConstraints.NONE;
      controls.add(new JLabel("Font style:"), constraints);
      constraints.gridx = 1;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      controls.add(styleCombo, constraints);

      constraints.gridx = 0;
      constraints.gridy = 2;
      constraints.fill = GridBagConstraints.NONE;
      controls.add(new JLabel("Size:"), constraints);
      constraints.gridx = 1;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      controls.add(sizeSpinner, constraints);

      JPanel content = new JPanel(new BorderLayout(8, 8));
      content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      content.add(controls, BorderLayout.CENTER);
      content.add(preview, BorderLayout.SOUTH);
      return content;
   }
}
