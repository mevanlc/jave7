package de.jave.jave.figlet;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.ui.FontCategoriesListCellRenderer;
import de.jave.figlet.util.FigException;
import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class FigletToolOptionsPanel implements IInlineToolOptions {
   private static final int MIN_PREFIX_LEN = 4;
   private static final String LEAF_INDENT = "  ";

   private final JComboBox chCategory;
   private final JComboBox chFont;
   private final JComponent content;
   private final FigFontModel fontModel;
   private final IFigDriver figDriver;
   private final BooleanModel mixCharactersModel;

   public FigletToolOptionsPanel(IFigDriver figDriver, BooleanModel mixCharactersModel) {
      Ensure.ensureArgumentNotNull(figDriver);
      this.mixCharactersModel = mixCharactersModel;
      this.figDriver = figDriver;
      this.fontModel = new FigFontModel();
      IFigFontCategory[] sc = figDriver.getFileLibrary().getFontCategorization().getAllNonEmptyCategories();
      this.chCategory = new JComboBox<>(sc);
      this.chCategory.setRenderer(new FontCategoriesListCellRenderer());
      this.chFont = new JComboBox();
      this.chFont.setRenderer(new FontEntryRenderer());
      this.chCategory.setSelectedItem(figDriver.getFileLibrary().getFontCategorization().getDefaultCategory());
      this.chCategory.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            FigletToolOptionsPanel.this.updateFontChoice();
         }
      });
      this.updateFontChoice();
      this.selectFontByName(figDriver.getFileLibrary().getDefaultFontName());
      this.chFont.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FontEntry entry = (FontEntry) FigletToolOptionsPanel.this.chFont.getSelectedItem();
            if (entry != null && entry.isHeader()) {
               int next = FigletToolOptionsPanel.this.findLeafFrom(FigletToolOptionsPanel.this.chFont.getSelectedIndex(), 1);
               if (next >= 0) {
                  FigletToolOptionsPanel.this.chFont.setSelectedIndex(next);
               }
               return;
            }
            FigletToolOptionsPanel.this.updateFont();
         }
      });
      this.updateFont();
      JPanel optionsPanel = new JPanel(new GridDialogLayout(1, false));
      optionsPanel.add(new JLabel("Font Category:"));
      optionsPanel.add(this.chCategory, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(new JLabel("Font:"));
      optionsPanel.add(this.chFont, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(new MergeCharactersPanel(mixCharactersModel).getContent());
      this.content = optionsPanel;
   }

   private void updateFont() {
      try {
         String name = this.getSelectedFontName();
         if (name == null) {
            return;
         }
         FigFont font = this.figDriver.getFont(name);
         this.fontModel.setFont(font);
      } catch (FigException var2) {
         var2.printStackTrace();
      }
   }

   private void updateFontChoice() {
      IFigFontCategory category = (IFigFontCategory)this.chCategory.getSelectedItem();
      String[] fontNames = category.getFontNames();
      List<FontEntry> entries = groupFonts(fontNames);
      this.chFont.setModel(new DefaultComboBoxModel<>(entries.toArray(new FontEntry[0])));
      int firstLeaf = this.findLeafFrom(-1, 1);
      this.chFont.setSelectedIndex(firstLeaf >= 0 ? firstLeaf : 0);
      this.updateFont();
   }

   private void selectFontByName(String fontName) {
      if (fontName == null) {
         return;
      }
      for (int i = 0; i < this.chFont.getItemCount(); i++) {
         FontEntry e = (FontEntry) this.chFont.getItemAt(i);
         if (!e.isHeader() && fontName.equals(e.fontName)) {
            this.chFont.setSelectedIndex(i);
            return;
         }
      }
   }

   /** Find the next/prev non-header index starting from {@code start}+{@code step}. */
   private int findLeafFrom(int start, int step) {
      int i = start + step;
      while (i >= 0 && i < this.chFont.getItemCount()) {
         FontEntry e = (FontEntry) this.chFont.getItemAt(i);
         if (!e.isHeader()) {
            return i;
         }
         i += step;
      }
      return -1;
   }

   private String getSelectedFontName() {
      FontEntry entry = (FontEntry) this.chFont.getSelectedItem();
      return entry == null ? null : entry.fontName;
   }

   public FigFontModel getFontModel() {
      return this.fontModel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   public BooleanModel getOptionsModel() {
      return this.mixCharactersModel;
   }

   public void selectNextFont() {
      int next = this.findLeafFrom(this.chFont.getSelectedIndex(), 1);
      if (next >= 0) {
         this.chFont.setSelectedIndex(next);
      } else {
         this.content.getToolkit().beep();
      }
   }

   public void selectPreviousFont() {
      int prev = this.findLeafFrom(this.chFont.getSelectedIndex(), -1);
      if (prev >= 0) {
         this.chFont.setSelectedIndex(prev);
      } else {
         this.content.getToolkit().beep();
      }
   }

   /**
    * Group font names by shared leading prefix (≥{@value #MIN_PREFIX_LEN}
    * chars). Each run of ≥2 fonts that share the longest such prefix
    * gets a non-selectable header row showing the prefix; leaves under
    * the header show only the suffix (with leading separator chars
    * stripped) so the combo's natural width shrinks. Solo fonts render
    * unchanged.
    */
   private static List<FontEntry> groupFonts(String[] fontNames) {
      String[] sorted = fontNames.clone();
      Arrays.sort(sorted);
      List<FontEntry> result = new ArrayList<>();
      int i = 0;
      while (i < sorted.length) {
         int j = i + 1;
         while (j < sorted.length && commonPrefixLen(sorted[i], sorted[j]) >= MIN_PREFIX_LEN) {
            j++;
         }
         int groupSize = j - i;
         if (groupSize >= 2) {
            String prefix = sorted[i];
            for (int k = i + 1; k < j; k++) {
               prefix = commonPrefix(prefix, sorted[k]);
            }
            result.add(FontEntry.header(prefix));
            for (int k = i; k < j; k++) {
               String full = sorted[k];
               String suffix = full.substring(prefix.length());
               String stripped = stripLeadingSeparators(suffix);
               String label = LEAF_INDENT + (stripped.isEmpty() ? full : stripped);
               result.add(FontEntry.leaf(label, full));
            }
         } else {
            result.add(FontEntry.leaf(sorted[i], sorted[i]));
         }
         i = j;
      }
      reExpandLeavesThatFit(result);
      return result;
   }

   /**
    * Once collapsed leaves have established a max-label width, re-expand
    * each prefix-headered group's leaves back to their full font names
    * — but only if <em>every</em> leaf in the group fits. Mixed state
    * within a single group (some expanded, some collapsed) is uglier
    * than uniform truncation, so it's all-or-nothing per group. Solo
    * leaves (no header) are never touched.
    */
   private static void reExpandLeavesThatFit(List<FontEntry> entries) {
      int maxLen = 0;
      for (FontEntry e : entries) {
         maxLen = Math.max(maxLen, e.displayLabel.length());
      }
      int idx = 0;
      while (idx < entries.size()) {
         FontEntry e = entries.get(idx);
         if (!e.isHeader()) {
            idx++;
            continue;
         }
         int groupStart = idx + 1;
         int groupEnd = groupStart;
         while (groupEnd < entries.size()
               && !entries.get(groupEnd).isHeader()
               && entries.get(groupEnd).displayLabel.startsWith(LEAF_INDENT)) {
            groupEnd++;
         }
         boolean allFit = true;
         for (int k = groupStart; k < groupEnd; k++) {
            String fullLabel = LEAF_INDENT + entries.get(k).fontName;
            if (fullLabel.length() > maxLen) {
               allFit = false;
               break;
            }
         }
         if (allFit) {
            for (int k = groupStart; k < groupEnd; k++) {
               String fontName = entries.get(k).fontName;
               entries.set(k, FontEntry.leaf(LEAF_INDENT + fontName, fontName));
            }
         }
         idx = groupEnd;
      }
   }

   private static int commonPrefixLen(String a, String b) {
      int n = Math.min(a.length(), b.length());
      for (int i = 0; i < n; i++) {
         if (a.charAt(i) != b.charAt(i)) {
            return i;
         }
      }
      return n;
   }

   private static String commonPrefix(String a, String b) {
      return a.substring(0, commonPrefixLen(a, b));
   }

   private static String stripLeadingSeparators(String s) {
      int i = 0;
      while (i < s.length() && !Character.isLetterOrDigit(s.charAt(i))) {
         i++;
      }
      return s.substring(i);
   }

   private static final class FontEntry {
      final String displayLabel;
      final String fontName;

      private FontEntry(String displayLabel, String fontName) {
         this.displayLabel = displayLabel;
         this.fontName = fontName;
      }

      static FontEntry header(String label) { return new FontEntry(label, null); }
      static FontEntry leaf(String label, String fontName) { return new FontEntry(label, fontName); }
      boolean isHeader() { return this.fontName == null; }

      @Override
      public String toString() {
         return this.displayLabel;
      }
   }

   private static final class FontEntryRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (value instanceof FontEntry) {
            FontEntry e = (FontEntry) value;
            this.setText(e.displayLabel);
            if (e.isHeader()) {
               this.setEnabled(false);
               this.setForeground(Color.GRAY);
               if (isSelected) {
                  this.setBackground(list.getBackground());
               }
            }
         }
         return this;
      }
   }
}
