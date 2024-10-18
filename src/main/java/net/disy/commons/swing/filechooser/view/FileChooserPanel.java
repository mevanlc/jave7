package net.disy.commons.swing.filechooser.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.string.StringFilter;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.AbstractActionComponent;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.filechooser.dialog.FileChooserDialogConfiguration;
import net.disy.commons.swing.filechooser.model.FileChooserModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.list.JListUtilities;
import net.disy.commons.swing.textfield.FilteredTextField;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class FileChooserPanel extends AbstractActionComponent {
   private final JComponent content;
   private FileSelectionPanel fileSelectionPanel;
   private JTextField textField;
   private final FileChooserDialogConfiguration configuration;
   private final FileChooserModel model;
   private FolderSelectionPanel folderSelectionPanel;
   private JComboBox filterCombo;
   private JComponent accessory;
   private JPanel rightPanel;

   public FileChooserPanel(FileChooserModel model, FileChooserDialogConfiguration configuration) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(configuration);
      this.model = model;
      this.configuration = configuration;
      this.content = this.createContent();
      if (configuration.isFileFilterEnabled()) {
         this.updateComboItems();
         this.updateComboSelection();
      }

      this.initListeners();
   }

   private void initListeners() {
      ActionListener actionListener = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            File file = FileChooserPanel.this.model.getFileModel().getValue();
            if (file != null && file.isDirectory()) {
               FileChooserPanel.this.model.getFolderModel().setValue(file);
            } else {
               FileChooserPanel.this.fireActionEvent();
            }
         }
      };
      this.fileSelectionPanel.addActionListener(actionListener);
      this.model.getFileModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FileChooserPanel.this.updateTextField();
         }
      });
      this.textField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            String text = FileChooserPanel.this.textField.getText();
            if (text != null && !text.isEmpty()) {
               FileChooserPanel.this.model.getFileModel().setValue(new File(FileChooserPanel.this.model.getFolderModel().getValue(), text));
            }
         }
      });
      this.textField.addActionListener(actionListener);
      this.model.getFolderModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FileChooserPanel.this.model.getFileModel().setValue(null);
         }
      });
      if (this.configuration.isFileFilterEnabled()) {
         this.model.getChoosableFileFilterModel().addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               FileChooserPanel.this.updateComboItems();
               FileChooserPanel.this.updateComboSelection();
            }
         });
         this.model.getFileFilterModel().addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               FileChooserPanel.this.updateComboSelection();
            }
         });
         this.filterCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               FileFilter selectedFileFilter = (FileFilter)FileChooserPanel.this.filterCombo.getSelectedItem();
               FileChooserPanel.this.model.getFileFilterModel().setFileFilter(selectedFileFilter);
            }
         });
      }
   }

   private void updateComboSelection() {
      this.filterCombo.setSelectedItem(this.model.getFileFilterModel().getFileFilter());
   }

   private void updateComboItems() {
      JListUtilities.setComboItems(this.filterCombo, this.model.getChoosableFileFilterModel().getFileFilters());
   }

   private void updateTextField() {
      File file = this.model.getFileModel().getValue();
      if (file == null) {
         this.textField.setText("");
      } else {
         String displayName = this.configuration.getFileSystemView().getSystemDisplayName(file);
         if (!displayName.equals(this.textField.getText())) {
            this.textField.setText(displayName);
         }
      }
   }

   private JComponent createContent() {
      this.folderSelectionPanel = new FolderSelectionPanel(this.model.getFolderModel(), this.configuration.getFileSystemView(), this.configuration.getRoots());
      this.rightPanel = new JPanel(new BorderLayout());
      this.fileSelectionPanel = new FileSelectionPanel(this.model, this.configuration.getFileSystemView());
      this.rightPanel.add(this.fileSelectionPanel.getContent(), "Center");
      JSplitPane splitPane = new JSplitPane(1, this.folderSelectionPanel.getContent(), this.rightPanel);
      JPanel textFieldPanel = new JPanel(new GridDialogLayout(2, false));
      textFieldPanel.add(new JLabel("Dateiname:"), new GridDialogLayoutData());
      StringFilter filter = new StringFilter() {
         @Override
         public boolean acceptFilterText(String s) {
            if (s.isEmpty()) {
               return true;
            } else if (s.endsWith(File.separator)) {
               return false;
            } else {
               File file = new File(FileChooserPanel.this.model.getFolderModel().getValue(), s);
               return FileChooserPanel.this.model.getFolderModel().getValue().equals(file.getParentFile());
            }
         }
      };
      this.textField = new FilteredTextField(filter);
      textFieldPanel.add(this.textField, GridDialogLayoutData.FILL_HORIZONTAL);
      if (this.configuration.isFileFilterEnabled()) {
         this.addFilterCombo(textFieldPanel);
      }

      JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      panel.add(splitPane, "Center");
      panel.add(textFieldPanel, "South");
      return panel;
   }

   private void addFilterCombo(JPanel textFieldPanel) {
      textFieldPanel.add(new JLabel("Dateityp:"), new GridDialogLayoutData());
      this.filterCombo = new JComboBox();
      this.filterCombo.setRenderer(new ObjectUiListCellRenderer(new FileChooserPanel.FileFilterUi()));
      textFieldPanel.add(this.filterCombo, GridDialogLayoutData.FILL_HORIZONTAL);
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setAccessory(JComponent accessory) {
      if (this.accessory != null) {
         this.rightPanel.remove(this.accessory);
      }

      this.accessory = accessory;
      if (this.accessory != null) {
         this.rightPanel.add(this.accessory, "South");
      }
   }

   public void refresh() {
      this.folderSelectionPanel.refresh();
      this.fileSelectionPanel.refresh();
   }

   private static final class FileFilterUi extends AbstractObjectUi<FileFilter> {
      private FileFilterUi() {
      }

      public String getLabel(FileFilter value) {
         return value == null ? "" : value.getDescription();
      }
   }
}
