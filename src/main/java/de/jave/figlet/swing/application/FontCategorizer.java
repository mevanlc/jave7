package de.jave.figlet.swing.application;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.file.IFigFontCategorization;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.io.FigCategoriesFileWriter;
import de.jave.figlet.swing.fontchooser.CategoriesTreeCellRenderer;
import de.jave.figlet.swing.fontchooser.CategorizationToTreeModelConverter;
import de.jave.figlet.swing.fontchooser.FixedIconListCellRenderer;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.figlet.util.FigException;
import de.jave.figlet.util.FigUtilities;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import de.jave.lib.tree.checkbox.CheckBoxSelectionMode;
import de.jave.lib.tree.checkbox.CheckBoxTreeManager;
import de.jave.lib.tree.checkbox.ICheckBoxNodeAccess;
import de.jdemo.util.GuiUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.swing.action.SmartAction;

public class FontCategorizer {
   private final JFrame frame;
   private final FontCategoriesListView list;
   private final IFigFontCategorization categorization;
   private String fontName;
   private final IFigDriver figlet;
   private final JTextArea sampleTextArea;
   private final JTextArea commentsTextArea;
   private final JTree tree;
   private final FileModel currentDirectoryModel = new FileModel();

   public FontCategorizer(IFigDriver figlet) {
      this.figlet = figlet;
      this.categorization = (IFigFontCategorization)figlet.getFileLibrary().getFontCategorization().clone();
      DefaultListModel model = new DefaultListModel();
      String[] strings = figlet.getFileLibrary().getAllFontNames();

      for (int i = 0; i < strings.length; i++) {
         model.addElement(strings[i]);
      }

      final JList fontList = new JList(model);
      fontList.setCellRenderer(new FixedIconListCellRenderer(FigletIcons.FONT_ICON));
      fontList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            FontCategorizer.this.setSelectedFont((String)fontList.getSelectedValue());
         }
      });
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BorderLayout());
      this.sampleTextArea = new JTextArea(10, 50);
      this.sampleTextArea.setEditable(false);
      topPanel.add(new JScrollPane(this.sampleTextArea), "Center");
      this.commentsTextArea = new JTextArea(10, 50);
      topPanel.add(new JScrollPane(this.commentsTextArea), "South");
      TreeModel treeModel = new CategorizationToTreeModelConverter().createTreeModel(this.categorization);
      this.tree = new JTree(treeModel);
      this.tree.setRootVisible(false);
      this.tree.setShowsRootHandles(true);
      CheckBoxTreeManager manager = new CheckBoxTreeManager(new ICheckBoxNodeAccess() {
         @Override
         public boolean isCheckableNode(TreeNode node) {
            if (!(node instanceof DefaultMutableTreeNode)) {
               return false;
            } else {
               Object userObject = ((DefaultMutableTreeNode)node).getUserObject();
               return !(userObject instanceof IFigFontCategory) ? false : node.isLeaf() && !((IFigFontCategory)userObject).isDynamicallyGenerated();
            }
         }

         @Override
         public boolean isChecked(TreeNode node) {
            return this.getCategory(node).containsFont(FontCategorizer.this.getCurrentFontName());
         }

         private IFigFontCategory getCategory(TreeNode node) {
            return (IFigFontCategory)((DefaultMutableTreeNode)node).getUserObject();
         }

         @Override
         public boolean isCheckEditable(TreeNode node) {
            return true;
         }

         @Override
         public void setChecked(TreeNode node, boolean checked) {
            if (checked) {
               this.getCategory(node).addFontName(FontCategorizer.this.getCurrentFontName());
            } else {
               this.getCategory(node).removeFontName(FontCategorizer.this.getCurrentFontName());
            }

            FontCategorizer.this.updateList();
         }
      }, CheckBoxSelectionMode.SINGLE_SELECTION, new CategoriesTreeCellRenderer());
      this.tree.setEditable(true);
      this.tree.setCellEditor(manager.createTreeEditor());
      this.tree.setCellRenderer(manager.createTreeRenderer());
      GuiUtilities.expandTreeNode(this.tree, (TreeNode)this.tree.getModel().getRoot());
      JScrollPane scrollPane1 = new JScrollPane(this.tree);
      scrollPane1.setPreferredSize(new Dimension(150, 120));
      this.list = new FontCategoriesListView();
      JPanel panelRight = new JPanel(new BorderLayout());
      panelRight.add(this.list.getContent(), "Center");
      panelRight.add(new JLabel("Categories containing this font:"), "North");
      JPanel panelLeft = new JPanel(new BorderLayout());
      panelLeft.add(scrollPane1, "Center");
      panelLeft.add(new JLabel("Categories:"), "North");
      JSplitPane splitPane = new JSplitPane(1, panelLeft, panelRight);
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(splitPane, "Center");
      mainPanel.add(topPanel, "North");
      this.frame = new JFrame();
      this.frame.setTitle("FIGlet font categorization");
      this.frame.getContentPane().setLayout(new BorderLayout());
      this.frame.getContentPane().add(mainPanel, "Center");
      this.frame.getContentPane().add(new JScrollPane(fontList), "West");
      JMenuBar menuBar = new JMenuBar();
      JMenu fileMenu = new JMenu("File");
      fileMenu.add(new SmartAction("Save categories...") {
         @Override
         protected void execute(Component parentComponent) {
            File file = FileChooserUtilities.performSaveFileChooser(parentComponent, new IFileChooserConfiguration() {
               @Override
               public FileModel getCurrentDirectoryModel() {
                  return FontCategorizer.this.currentDirectoryModel;
               }

               @Override
               public String getSaveDialogTitle() {
                  return "Save categoriesTree.txt";
               }

               @Override
               public String getOpenDialogTitle() {
                  return null;
               }

               @Override
               public SmartFileFilter[] getFileFilters() {
                  return new SmartFileFilter[]{ExtensionFileFilters.TXT};
               }

               @Override
               public String getFileNameSuggestion() {
                  return "categoriesTree.txt";
               }

               @Override
               public boolean isMultipleOpenFileSelectionAllowed() {
                  return false;
               }
            });
            if (file != null) {
               try {
                  new FigCategoriesFileWriter().write(FontCategorizer.this.categorization, file);
               } catch (IOException var4) {
                  var4.printStackTrace();
               }
            }
         }
      });
      menuBar.add(fileMenu);
      this.frame.setJMenuBar(menuBar);
      this.frame.pack();
      this.setSelectedFont(figlet.getFileLibrary().getDefaultFontName());
   }

   private String getCurrentFontName() {
      return this.fontName;
   }

   protected void setSelectedFont(String fontName) {
      this.fontName = fontName;
      String sample = null;

      try {
         sample = this.figlet.figletize("font sample", fontName);
      } catch (FigException var6) {
         var6.printStackTrace();
      }

      this.sampleTextArea.setText(sample);
      String comments = null;

      try {
         comments = this.figlet.getFont(fontName).getComments();
      } catch (FigException var5) {
         var5.printStackTrace();
      }

      this.commentsTextArea.setText(comments);
      ((DefaultTreeModel)this.tree.getModel()).nodeChanged((TreeNode)this.tree.getModel().getRoot());
      this.updateList();
   }

   protected void updateList() {
      IFigFontCategory[] containingCategories = FigUtilities.getCategoriesContainingFont(this.categorization, this.fontName);
      this.list.setCategories(containingCategories);
   }

   public void show() {
      this.frame.show();
   }

   public JFrame getFrame() {
      return this.frame;
   }
}
