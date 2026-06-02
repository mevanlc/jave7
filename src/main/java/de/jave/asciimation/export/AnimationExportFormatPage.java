package de.jave.asciimation.export;

import de.jave.jave.JaveGlobalRessources;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.wizard.AbstractWizardPage;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class AnimationExportFormatPage extends AbstractWizardPage {
   private JList list;
   private JTextArea descriptionTextArea;
   private final AnimationExportWizardModel model;

   public AnimationExportFormatPage(String title, AnimationExportWizardModel model) {
      super(title, "Select an export format for animation export.");
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.model.getExportOptions().getFormat() == null
         ? new BasicMessage("There is no format selected. Please select a format for export.", MessageType.ERROR)
         : this.getDefaultMessage();
   }

   @Override
   public JComponent createContent() {
      IAnimationExportFormat[] formats = AnimationExportFormatRegistry.getAllExportFormats();
      this.list = new JList<>(formats);
      this.list.setSelectionMode(1);
      this.list.setCellRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<IAnimationExportFormat>() {
         public String getLabel(IAnimationExportFormat value) {
            return value.getName();
         }

         public Icon getIcon(IAnimationExportFormat value) {
            return value.getIcon();
         }
      }));
      this.list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            AnimationExportFormatPage.this.updateDescriptionLabel();
            AnimationExportFormatPage.this.model.getExportOptions().setFormat((IAnimationExportFormat)AnimationExportFormatPage.this.list.getSelectedValue());
            AnimationExportFormatPage.this.checkInputValid();
         }
      });
      this.list.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
               AnimationExportFormatPage.this.getWizard().getContainer().requestNext();
            }
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JLabel("Select an export destination:"));
      panel.add(new JScrollPane(this.list), GridDialogLayoutData.FILL_BOTH);
      panel.add(new JLabel("Description:"));
      this.descriptionTextArea = new JTextArea(5, 50);
      this.descriptionTextArea.setFont(JaveGlobalRessources.FONT_SMALL);
      this.descriptionTextArea.setEditable(false);
      this.descriptionTextArea.setLineWrap(true);
      this.descriptionTextArea.setWrapStyleWord(true);
      panel.add(new JScrollPane(this.descriptionTextArea), GridDialogLayoutData.FILL_HORIZONTAL);
      this.updateDescriptionLabel();
      return panel;
   }

   private void updateDescriptionLabel() {
      IAnimationExportFormat format = this.model.getExportOptions().getFormat();
      if (format == null) {
         this.descriptionTextArea.setText(" - No format selected -");
      } else {
         this.descriptionTextArea.setText(format.getDescription());
      }
   }

   @Override
   public boolean canFlipToNextPage() {
      if (this.getNextPage() == null) {
         return false;
      } else {
         return this.getMessage().getType() == MessageType.ERROR ? false : this.createCurrentMessage().getType() != MessageType.ERROR;
      }
   }

   @Override
   public boolean canFinish() {
      return false;
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }
}
