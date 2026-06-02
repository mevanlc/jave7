package de.jave.figlet.swing.fontchooser;

import de.jave.figlet.IFontSelectionListener;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.action.FontSelectionActions;
import de.jave.figlet.swing.ui.FigletIcons;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.toolbar.ToolBarUtilities;

public class FontListView {
   private final JComponent content;
   private final JList list;
   private final List<IFontSelectionListener> listeners = new ArrayList<>();
   private final FontSelectionActions actions;
   private final FixedIconListCellRenderer renderer = new FixedIconListCellRenderer(FigletIcons.FONT_ICON);
   private FontNameFilterPanel filterPanel;
   private String[] currentFontNames;
   private String choosenFont;

   public FontListView(IFigDriver figDriver, File tmpFile) {
      Ensure.ensureArgumentNotNull(figDriver);
      Ensure.ensureArgumentNotNull(tmpFile);
      this.actions = new FontSelectionActions(this, figDriver);
      this.list = new JList(new DefaultListModel());
      this.list.setCellRenderer(this.renderer);
      this.list.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent event) {
            if (FontListView.this.list.isEnabled()) {
               int index = FontListView.this.list.getUI().locationToIndex(FontListView.this.list, event.getPoint());
               if (index >= 0 && index < FontListView.this.list.getModel().getSize()) {
                  FontListView.this.list.setSelectedIndex(index);
               }
            }
         }

         @Override
         public void mouseReleased(MouseEvent event) {
            if (FontListView.this.list.isEnabled()) {
               if (event.isMetaDown()) {
                  FontListView.this.showPopup(event.getPoint());
               }
            }
         }
      });
      this.list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            FontListView.this.updateActionsEnabled();
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  if (FontListView.this.getSelectedFont() != null) {
                     FontListView.this.choosenFont = FontListView.this.getSelectedFont();
                  }

                  FontListView.this.fireFontSelectionChanged();
               }
            });
         }
      });
      JScrollPane scrollPane = new JScrollPane(this.list);
      scrollPane.setPreferredSize(new Dimension(150, 120));
      AbstractButton infoButton = ToolBarUtilities.createToolBarButton(this.actions.getFontInfoAction());
      JPanel filterPanel = new JPanel(new FlowLayout(1, 0, 0));
      filterPanel.add(this.createFilterPanel());
      JPanel headerPanel = new JPanel(new GridDialogLayout(4, false));
      headerPanel.add(new JLabel("Font:", 2), GridDialogLayoutData.RIGHT);
      headerPanel.add(filterPanel, GridDialogLayoutData.FILL_HORIZONTAL);
      headerPanel.add(infoButton);
      JPanel contentPanel = new JPanel(new BorderLayout());
      contentPanel.add(headerPanel, "North");
      contentPanel.add(scrollPane, "Center");
      this.content = contentPanel;
   }

   private JComponent createFilterPanel() {
      this.filterPanel = new FontNameFilterPanel(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FontListView.this.filterFontNames(FontListView.this.filterPanel.getFilterText());
         }
      });
      return this.filterPanel.getComponent();
   }

   protected void filterFontNames(String filterText) {
      if (filterText != null && filterText.length() != 0) {
         this.setListModel(getFiltered(this.currentFontNames, filterText));
      } else {
         this.setListModel(this.currentFontNames);
      }
   }

   private static String[] getFiltered(String[] names, String filterText) {
      filterText = filterText.toLowerCase();
      List<String> list = new ArrayList<>();

      for (int i = 0; i < names.length; i++) {
         if (names[i].toLowerCase().indexOf(filterText) != -1) {
            list.add(names[i]);
         }
      }

      return list.toArray(new String[0]);
   }

   protected void showPopup(Point point) {
      JPopupMenu menu = this.actions.getPopupMenu();
      if (menu != null) {
         menu.show(this.list, point.x, point.y);
      }
   }

   protected void updateActionsEnabled() {
      this.actions.updateEnabled();
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setFontCategory(IFigFontCategory category) {
      this.filterPanel.clear();
      DefaultListModel listModel = (DefaultListModel)this.list.getModel();
      if (category == null) {
         listModel.clear();
      } else {
         this.currentFontNames = category.getFontNames();
         this.setListModel(this.currentFontNames);
         if (this.currentFontNames.length == 0) {
            listModel.addElement(" - category is empty -");
            this.list.setEnabled(false);
            this.filterPanel.setEnabled(false);
            this.list.setCellRenderer(new DefaultListCellRenderer());
         } else {
            this.list.setEnabled(true);
            this.filterPanel.setEnabled(true);
            this.list.setCellRenderer(this.renderer);
         }
      }
   }

   private void setListModel(String[] fontNames) {
      this.list.setValueIsAdjusting(true);
      DefaultListModel listModel = (DefaultListModel)this.list.getModel();
      listModel.clear();

      for (int i = 0; i < fontNames.length; i++) {
         listModel.addElement(fontNames[i]);
      }

      if (this.choosenFont != null) {
         this.setSelectedFont(this.choosenFont);
      }

      this.list.setValueIsAdjusting(false);
   }

   public String getSelectedFont() {
      return this.list.isEnabled() ? (String)this.list.getSelectedValue() : null;
   }

   public synchronized void addFontSelectionListener(IFontSelectionListener listener) {
      this.listeners.add(listener);
   }

   public synchronized void removeFontSelectionListener(IFontSelectionListener listener) {
      this.listeners.remove(listener);
   }

   protected synchronized void fireFontSelectionChanged() {
      for (int i = 0; i < this.listeners.size(); i++) {
         IFontSelectionListener listener = this.listeners.get(i);
         listener.fontSelectionChanged();
      }
   }

   public FontSelectionActions getFontSelectionActions() {
      return this.actions;
   }

   public void setSelectedFont(String fontName) {
      this.choosenFont = fontName;
      DefaultListModel model = (DefaultListModel)this.list.getModel();
      if (model.contains(fontName)) {
         this.list.setSelectedValue(fontName, true);
      } else {
         this.list.clearSelection();
      }
   }
}
