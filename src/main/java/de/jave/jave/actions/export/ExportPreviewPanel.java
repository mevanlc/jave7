package de.jave.jave.actions.export;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.label.SmartLabel;

public class ExportPreviewPanel {
   private final JComponent content;
   private final JPanel contentPanel;
   private final ObjectModel<JComponent> previewComponentModel;

   public ExportPreviewPanel(ObjectModel<JComponent> previewComponentModel) {
      Ensure.ensureArgumentNotNull(previewComponentModel);
      this.previewComponentModel = previewComponentModel;
      JPanel mainPanel = new JPanel(new BorderLayout());
      JLabel label = new SmartLabel("Preview:");
      mainPanel.add(label, "North");
      this.contentPanel = new JPanel(new BorderLayout());
      this.contentPanel.setPreferredSize(new Dimension(250, 250));
      mainPanel.add(this.contentPanel, "Center");
      this.content = mainPanel;
      previewComponentModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ExportPreviewPanel.this.updateContent();
         }
      });
      this.updateContent();
   }

   private void updateContent() {
      JComponent component = this.previewComponentModel.getValue();
      this.contentPanel.removeAll();
      if (component != null) {
         this.contentPanel.add(component, "Center");
      }

      this.contentPanel.revalidate();
   }

   public JComponent getContent() {
      return this.content;
   }
}
