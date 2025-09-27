package de.jave.jave.clipart;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class ClipartNewEntryPage extends AbstractDialogPage {
   private final ObjectModel<ClipartGroup> groupSelectionModel;
   private final ClipartNewEntryModel model;
   private final ClipartManager clipartManager;
   private final FontModel displayFontModel;
   private JTextField nameTextField;

   public ClipartNewEntryPage(
      ObjectModel<ClipartGroup> groupSelectionModel, ClipartNewEntryModel model, ClipartManager clipartManager, FontModel displayFontModel
   ) {
      super("In order to add the new clipart to the library please enter a name and choose a category to add the clipart to.");
      Ensure.ensureArgumentNotNull(groupSelectionModel);
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(clipartManager);
      Ensure.ensureArgumentNotNull(displayFontModel);
      this.groupSelectionModel = groupSelectionModel;
      this.model = model;
      this.clipartManager = clipartManager;
      this.displayFontModel = displayFontModel;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      if (this.groupSelectionModel.getValue() == null) {
         return new BasicMessage("There is no category chosen for the clipart. Please choose a category.", MessageType.ERROR);
      } else {
         return this.model.getName() != null && this.model.getName().length() != 0
            ? this.getDefaultMessage()
            : new BasicMessage("You have not entered a name for the clipart. Please enter a name.", MessageType.ERROR);
      }
   }

   @Override
   public String getTitle() {
      return "Add New Clipart";
   }

   @Override
   public JComponent createContent() {
      ClipartGroupListPanel listPanel = new ClipartGroupListPanel(this.clipartManager, this.groupSelectionModel);
      NewClipartFolderAction newFolderAction = new NewClipartFolderAction(this.clipartManager, listPanel);
      AsciiTextAreaProperties properties = new AsciiTextAreaProperties(this.displayFontModel);
      properties.setEditable(false);
      AsciiTextArea taPreview = new AsciiTextArea(new Dimension(45, 16), properties);
      JPanel clipartPanel = new JPanel(new GridDialogLayout(2, false));
      clipartPanel.add(new JLabel("Name:"), GridDialogLayoutData.RIGHT);
      this.nameTextField = new JTextField("", 15);
      this.nameTextField.setEditable(true);
      clipartPanel.add(this.nameTextField);
      clipartPanel.add(new JLabel("Author:"), GridDialogLayoutData.RIGHT);
      final JTextField tfAuthor = new JTextField("", 15);
      tfAuthor.setEditable(true);
      clipartPanel.add(tfAuthor);
      clipartPanel.add(new JLabel("Size:"), GridDialogLayoutData.RIGHT);
      JTextField tfSize = new JTextField("", 9);
      tfSize.setEditable(false);
      clipartPanel.add(tfSize);
      GridDialogLayoutData previewData = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      previewData.setHorizontalSpan(2);
      clipartPanel.add(taPreview.getContent(), previewData);
      taPreview.setText(this.model.getCode().toString());
      tfSize.setText(this.model.getCode().getWidth() + " x " + this.model.getCode().getHeight());
      JPanel categoriesPanel = new JPanel(new GridDialogLayout(2, false));
      categoriesPanel.add(new JLabel("Category:"));
      categoriesPanel.add(new JButton(newFolderAction));
      previewData.setHorizontalSpan(2);
      categoriesPanel.add(listPanel.getContent(), previewData);
      JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentGroupsSpacing(), LayoutUtilities.getComponentSpacing()));
      panel.add(categoriesPanel, "Center");
      panel.add(clipartPanel, "East");
      tfAuthor.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            ClipartNewEntryPage.this.model.setAuthor(tfAuthor.getText());
         }
      });
      this.nameTextField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            ClipartNewEntryPage.this.model.setName(ClipartNewEntryPage.this.nameTextField.getText());
         }
      });
      this.groupSelectionModel.addChangeListener(this.getCheckInputValidListener());
      this.model.addChangeListener(this.getCheckInputValidListener());
      return panel;
   }

   @Override
   public void requestFocus() {
      this.nameTextField.requestFocus();
   }
}
