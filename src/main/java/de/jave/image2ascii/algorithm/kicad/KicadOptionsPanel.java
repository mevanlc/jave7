package de.jave.image2ascii.algorithm.kicad;

import java.text.DecimalFormat;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.layout.grid.GridAlignment;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.dizzy.commons.swing.textfield.DoubleModelTextField;

public final class KicadOptionsPanel implements IDisposableComponentContainer {
   private static final DecimalFormat MM_DECIMAL_FORMAT = new DecimalFormat("0.#");
   private final KiCadOptionsModel optionsModel;

   public KicadOptionsPanel(KiCadOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(optionsModel);
      this.optionsModel = optionsModel;
   }

   @Override
   public void dispose() {
   }

   @Override
   public JComponent getContent() {
      final JTextField textField = new JTextField(this.optionsModel.getModuleName(), 20);
      textField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            KicadOptionsPanel.this.optionsModel.setModuleName(textField.getText());
         }
      });
      final ObjectModel<Double> doubleModel = new ObjectModel<>(this.optionsModel.getWidthInInch());
      JComponent widthTextField = new DoubleModelTextField(8, doubleModel).getContent();
      final JLabel mmLabel = new JLabel();
      this.updateMmLabel(mmLabel);
      this.optionsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            KicadOptionsPanel.this.updateMmLabel(mmLabel);
         }
      });
      doubleModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Double value = doubleModel.getValue();
            if (value != null) {
               KicadOptionsPanel.this.optionsModel.setWidthInInch(value);
            }
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      panel.add(new JLabel("Module name:"), GridDialogLayoutData.RIGHT);
      panel.add(textField, GridDialogLayoutDataFactory.createHorizontalSpanData(2));
      panel.add(new JLabel("Width:"), GridDialogLayoutData.RIGHT);
      panel.add(widthTextField);
      panel.add(mmLabel, new GridDialogLayoutData().setHorizontalAlignment(GridAlignment.BEGINNING));
      return panel;
   }

   private void updateMmLabel(JLabel label) {
      double mms = this.optionsModel.getWidthInInch() * 25.4;
      label.setText("inch (" + MM_DECIMAL_FORMAT.format(mms) + " mm)");
   }
}
