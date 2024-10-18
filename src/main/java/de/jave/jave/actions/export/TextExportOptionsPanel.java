package de.jave.jave.actions.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.FontChooserButton;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class TextExportOptionsPanel {
   private final JComponent content;
   private final JCheckBox connectedLinesViewCheckBox;
   private final FontChooserButton fontChooserButton;
   private final FixedOptionsObjectSelectionModel<ITextExportFormat> formatModel;

   public TextExportOptionsPanel(final TextExportOptionsModel model, FixedOptionsObjectSelectionModel<ITextExportFormat> formatModel) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(formatModel);
      this.formatModel = formatModel;
      final JCheckBox trimCheckBox = new JCheckBox("Trim", model.isTrim());
      trimCheckBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setTrim(trimCheckBox.isSelected());
         }
      });
      final FontModel fontModel = new FontModel(model.getFont());
      fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            model.setFont(fontModel.getFont());
         }
      });
      this.fontChooserButton = new FontChooserButton(fontModel);
      this.connectedLinesViewCheckBox = new JCheckBox("Connected Lines View", model.isConnectedLinesView());
      this.connectedLinesViewCheckBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setConnectedLinesView(TextExportOptionsPanel.this.connectedLinesViewCheckBox.isSelected());
         }
      });
      formatModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            TextExportOptionsPanel.this.updateEnabled();
         }
      });
      this.updateEnabled();
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(trimCheckBox);
      panel.add(this.connectedLinesViewCheckBox);
      panel.add(this.fontChooserButton.getContent());
      this.content = panel;
   }

   private void updateEnabled() {
      ITextExportFormat format = this.formatModel.getFirstSelectedValue();
      if (format != null) {
         this.connectedLinesViewCheckBox.setEnabled(format.isConnectedLinesViewSupported());
         this.fontChooserButton.setEnabled(format.isFontSupported());
      }
   }

   public JComponent getContent() {
      return this.content;
   }
}
