package net.disy.commons.swing.fontchooser.view;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public abstract class AbstractFontAdjustListPanel extends AbstractFontAdjustComponent {
   private JLabel titleLabel;
   private static final int VISIBLE_LIST_ROW_COUNT = 7;
   private JTextField textField;
   private JList list;

   public AbstractFontAdjustListPanel(FontModel fontModel, IFontDialogProperties properties) {
      super(fontModel, properties);
      this.attachTextFieldListener();
      this.attachListListener();
   }

   protected abstract void attachListListener();

   private void attachTextFieldListener() {
      this.getTextField().getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            AbstractFontAdjustListPanel.this.textChanged();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            AbstractFontAdjustListPanel.this.textChanged();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
         }
      });
   }

   private JLabel getTitleLabel() {
      if (this.titleLabel == null) {
         this.titleLabel = new JLabel(this.getTitle());
      }

      return this.titleLabel;
   }

   @Override
   protected final JComponent createContent() {
      JPanel panel = new JPanel(new GridDialogLayout(1, false, LayoutUtilities.getComponentSpacing(), 2));
      panel.add(this.getTitleLabel());
      panel.add(this.getTextField(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new JScrollPane(this.getList()), GridDialogLayoutData.FILL_BOTH);
      return panel;
   }

   private void textChanged() {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            String text = AbstractFontAdjustListPanel.this.getTextField().getText().trim();
            Object selectedValue = AbstractFontAdjustListPanel.this.getList().getSelectedValue();
            if (selectedValue == null || !text.equalsIgnoreCase(AbstractFontAdjustListPanel.this.getListValueUIText(selectedValue))) {
               int rowIndex = AbstractFontAdjustListPanel.this.getBestFitListRowIndex(text);
               if (AbstractFontAdjustListPanel.this.getListItemAsString(rowIndex).equalsIgnoreCase(text)) {
                  AbstractFontAdjustListPanel.this.getList().setSelectedIndex(rowIndex);
               } else {
                  AbstractFontAdjustListPanel.this.getList().setSelectedIndex(-1);
               }

               AbstractFontAdjustListPanel.this.ensureListRowVisibleOnTop(rowIndex);
            }
         }
      });
   }

   protected String getListValueUIText(Object value) {
      return value.toString();
   }

   private void ensureListRowVisibleOnTop(int rowIndex) {
      int topRowIndex = rowIndex + 7 - 1;
      if (topRowIndex >= this.getList().getModel().getSize()) {
         topRowIndex = this.getList().getModel().getSize() - 1;
      }

      this.getList().ensureIndexIsVisible(rowIndex);
      this.getList().ensureIndexIsVisible(topRowIndex);
   }

   protected int getBestFitListRowIndex(String text) {
      String lowerCaseText = text.toLowerCase();

      for (int i = 0; i < this.getList().getModel().getSize(); i++) {
         String item = this.getListItemAsString(i);
         if (item.toLowerCase().startsWith(lowerCaseText)) {
            return i;
         }
      }

      return 0;
   }

   private String getListItemAsString(int rowIndex) {
      Object value = this.getList().getModel().getElementAt(rowIndex);
      return this.getListValueUIText(value);
   }

   protected abstract String getTitle();

   protected abstract JList createListComponent();

   @Override
   protected abstract void updateFontModelView();

   protected JList getList() {
      if (this.list == null) {
         this.list = this.createListComponent();
         this.list.setSelectionMode(0);
         this.list.setVisibleRowCount(7);
      }

      return this.list;
   }

   protected JTextField getTextField() {
      if (this.textField == null) {
         this.textField = new JTextField();
      }

      return this.textField;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.getList().setEnabled(enabled);
      this.getTextField().setEnabled(enabled);
      this.getTitleLabel().setEnabled(enabled);
   }

   @Override
   public boolean isEnabled() {
      return this.getTextField().isEnabled();
   }
}
