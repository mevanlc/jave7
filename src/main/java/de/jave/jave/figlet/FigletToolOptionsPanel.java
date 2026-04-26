package de.jave.jave.figlet;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.action.FontInfoAction;
import de.jave.figlet.swing.fontchooser.FixedIconListCellRenderer;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.figlet.swing.ui.FontCategoriesListCellRenderer;
import de.jave.figlet.util.FigException;
import de.jave.gui.layout.Gap;
import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.actions.ButtonToolbarBuilder;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class FigletToolOptionsPanel implements IInlineToolOptions {
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
      this.chFont.setRenderer(new FixedIconListCellRenderer(FigletIcons.FONT_ICON));
      JButton bFontInfo = ButtonToolbarBuilder.createToolbarButton(new FontInfoAction(figDriver) {
         @Override
         protected String getSelectedFont() {
            return FigletToolOptionsPanel.this.fontModel.getFont().getName();
         }
      });
      this.chCategory.setSelectedItem(figDriver.getFileLibrary().getFontCategorization().getDefaultCategory());
      this.chCategory.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            FigletToolOptionsPanel.this.updateFontChoice();
         }
      });
      this.updateFontChoice();
      this.chFont.setSelectedItem(figDriver.getFileLibrary().getDefaultFontName());
      this.chFont.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FigletToolOptionsPanel.this.updateFont();
         }
      });
      this.updateFont();
      JPanel optionsPanel = new JPanel(new GridDialogLayout(3, false));
      optionsPanel.add(new JLabel("Font Category:"), GridDialogLayoutData.RIGHT);
      optionsPanel.add(this.chCategory, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(new Gap());
      optionsPanel.add(new JLabel("Font:"), GridDialogLayoutData.RIGHT);
      optionsPanel.add(this.chFont, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(bFontInfo);
      optionsPanel.add(new Gap());
      optionsPanel.add(new MergeCharactersPanel(mixCharactersModel).getContent());
      this.content = optionsPanel;
   }

   private void updateFont() {
      try {
         FigFont font = this.figDriver.getFont(this.getSelectedFontName());
         this.fontModel.setFont(font);
      } catch (FigException var2) {
         var2.printStackTrace();
      }
   }

   private void updateFontChoice() {
      IFigFontCategory category = (IFigFontCategory)this.chCategory.getSelectedItem();
      String[] fontNames = category.getFontNames();
      this.chFont.setModel(new DefaultComboBoxModel<>(fontNames));
      this.chFont.setSelectedIndex(0);
      this.updateFont();
   }

   private String getSelectedFontName() {
      return (String)this.chFont.getSelectedItem();
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
      int sel = this.chFont.getSelectedIndex();
      if (sel + 1 < this.chFont.getItemCount()) {
         this.chFont.setSelectedIndex(sel + 1);
      } else {
         this.content.getToolkit().beep();
      }
   }

   public void selectPreviousFont() {
      int sel = this.chFont.getSelectedIndex();
      if (sel > 0) {
         this.chFont.setSelectedIndex(sel - 1);
      } else {
         this.content.getToolkit().beep();
      }
   }
}
