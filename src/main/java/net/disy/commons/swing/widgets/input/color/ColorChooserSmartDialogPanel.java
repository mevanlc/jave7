package net.disy.commons.swing.dialog.input.color;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.dialog.color.ColorChooserButton;
import net.disy.commons.swing.dialog.color.IColorChooserConfiguration;
import net.disy.commons.swing.dialog.input.AbstractLabeledSmartDialogPanel;
import net.disy.commons.swing.dialog.input.NullMessageProducingValidator;

public class ColorChooserSmartDialogPanel extends AbstractLabeledSmartDialogPanel {
   private final ColorModel model;
   private final ColorChooserButton button;

   public ColorChooserSmartDialogPanel(String label, ColorModel model, IColorChooserConfiguration configuration) {
      super(label, new NullMessageProducingValidator());
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.button = new ColorChooserButton(model, configuration);
   }

   @Override
   protected JComponent fillMainComponentInto(JPanel panel, int columnCount) {
      panel.add(this.button.getContent());
      return this.button.getContent();
   }

   @Override
   protected int getMainComponentColumnCount() {
      return 1;
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.model.addChangeListener(listener);
   }

   @Override
   public void requestFocus() {
      this.button.getContent().requestFocus();
   }

   @Override
   protected void setMainComponentEnabled(boolean enabled) {
      this.button.setEnabled(enabled);
   }

   @Override
   protected JComponent[] getOtherComponents() {
      return new JComponent[]{this.button.getContent()};
   }
}
