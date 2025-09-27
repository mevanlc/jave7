package de.jave.jave.figlet.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class FigletExportWizardPage3 extends AbstractFigletExportWizardPage {
   private JTextArea taRaster;

   public FigletExportWizardPage3(FigletExportModel model) {
      super(model, "Character Arrangement", "Edit the character mask below to make the raster fit to the character arrangement in the main editor document.");
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   protected JComponent createContent() {
      final JComboBox comboBox = new JComboBox<>(CharacterArrangement.ALL);
      this.taRaster = new JTextArea();
      this.taRaster.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FigletExportWizardPage3.this.getModel().setCharacterArrangement(FigletExportWizardPage3.this.taRaster.getText());
         }
      });
      comboBox.setRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<CharacterArrangement>() {
         public String getLabel(CharacterArrangement value) {
            return value.getName();
         }
      }));
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FigletExportWizardPage3.this.getModel().setCharacterArrangement(((CharacterArrangement)comboBox.getSelectedItem()).getField());
         }
      });
      this.getModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FigletExportWizardPage3.this.updateView();
         }
      });
      this.updateView();
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      data.setHorizontalSpan(2);
      panel.add(new JScrollPane(this.taRaster), data);
      panel.add(new JLabel("Set to Default Arrangements:"));
      panel.add(comboBox);
      return panel;
   }

   private void updateView() {
      if (!this.getModel().getCharacterArrangement().equals(this.taRaster.getText())) {
         this.taRaster.setText(this.getModel().getCharacterArrangement());
      }
   }

   @Override
   public boolean canFinish() {
      return this.getModel().canFinish();
   }

   @Override
   public void requestFocus() {
      this.taRaster.requestFocus();
   }
}
