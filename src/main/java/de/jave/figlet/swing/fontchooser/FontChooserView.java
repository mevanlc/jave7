package de.jave.figlet.swing.fontchooser;

import de.jave.figlet.IFontCategorySelectionListener;
import de.jave.figlet.IFontSelectionListener;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.file.IFigFontCategorization;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.action.FontSelectionActions;
import de.jave.figlet.util.FigException;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import net.dizzy.commons.core.model.listener.ListenerList;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.core.util.IClosure;

public class FontChooserView {
   private final JComponent content;
   private final FontListView fontListView;
   private final ListenerList<IFontSelectionListener> listeners = new ListenerList<>();
   private FigFont currentFont;
   private final CategoriesView categoriesView;

   public FontChooserView(IFigFontCategorization categorization, final IFigDriver figDriver, File tmpFolder) {
      Ensure.ensureArgumentNotNull(categorization);
      Ensure.ensureArgumentNotNull(figDriver);
      Ensure.ensureArgumentNotNull(tmpFolder);

      try {
         this.currentFont = figDriver.getFont(figDriver.getFileLibrary().getDefaultFontName());
      } catch (FigException var7) {
         var7.printStackTrace();
      }

      this.categoriesView = new CategoriesView(categorization);
      this.fontListView = new FontListView(figDriver, tmpFolder);
      this.categoriesView
         .addFontCategorySelectionListener(
            new IFontCategorySelectionListener() {
               @Override
               public void categorySelectionChanged() {
                  IFigFontCategory selectedCategory = FontChooserView.this.categoriesView.getSelectedCategory();
                  FontChooserView.this.fontListView.setFontCategory(selectedCategory);
                  if (FontChooserView.this.currentFont != null
                     && selectedCategory != null
                     && selectedCategory.containsFont(FontChooserView.this.currentFont.getName())) {
                     FontChooserView.this.fontListView.setSelectedFont(FontChooserView.this.currentFont.getName());
                  }
               }
            }
         );
      this.fontListView
         .addFontSelectionListener(
            new IFontSelectionListener() {
               @Override
               public void fontSelectionChanged() {
                  String selectedFontName = FontChooserView.this.getSelectedFontName();
                  if (selectedFontName != null) {
                     if (!selectedFontName.equals(FontChooserView.this.currentFont.getName())) {
                        FigFont font;
                        try {
                           font = figDriver.getFont(selectedFontName);
                        } catch (FigException var4) {
                           JOptionPane.showMessageDialog(
                              FontChooserView.this.content,
                              "Unable to load the selected font '" + selectedFontName + "'\n" + "(" + var4.getMessage() + ")",
                              "FIGlet font error",
                              0
                           );
                           if (FontChooserView.this.currentFont != null) {
                              FontChooserView.this.setSelectedFont(FontChooserView.this.currentFont);
                           }

                           return;
                        }

                        FontChooserView.this.currentFont = font;
                        FontChooserView.this.fireFontSelectionChanged();
                     }
                  }
               }
            }
         );
      JPanel leftPanel = new JPanel(new BorderLayout());
      leftPanel.add(new JLabel("Font category:"), "North");
      leftPanel.add(this.categoriesView.getContent(), "Center");
      JSplitPane pane = new JSplitPane(1, leftPanel, this.fontListView.getContent());
      pane.setBorder(null);
      pane.setContinuousLayout(true);
      this.content = pane;
      IFigFontCategory defaultCategory = categorization.getDefaultCategory();
      this.categoriesView.setSelectedCategory(defaultCategory);
      this.fontListView.setSelectedFont(figDriver.getFileLibrary().getDefaultFontName());
   }

   public JComponent getContent() {
      return this.content;
   }

   private String getSelectedFontName() {
      return this.fontListView.getSelectedFont();
   }

   public IFigFontCategory getSelectedFontCategory() {
      return this.categoriesView.getSelectedCategory();
   }

   public void addFontSelectionListener(IFontSelectionListener listener) {
      this.listeners.add(listener);
   }

   public void removeFontSelectionListener(IFontSelectionListener listener) {
      this.listeners.remove(listener);
   }

   protected void fireFontSelectionChanged() {
      this.listeners.forAllDo(new IClosure<IFontSelectionListener>() {
         public void execute(IFontSelectionListener each) throws RuntimeException {
            each.fontSelectionChanged();
         }
      });
   }

   public FontSelectionActions getFontSelectionActions() {
      return this.fontListView.getFontSelectionActions();
   }

   public void setSelectedFont(FigFont font) {
      this.currentFont = font;
      this.fontListView.setSelectedFont(font.getName());
   }

   public FigFont getSelectedFont() {
      return this.currentFont;
   }

   public void setSelectedCategory(IFigFontCategory category) {
      this.categoriesView.setSelectedCategory(category);
   }
}
