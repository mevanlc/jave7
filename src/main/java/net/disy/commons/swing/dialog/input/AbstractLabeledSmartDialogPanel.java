package net.disy.commons.swing.dialog.input;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.input.text.IUpdatableSmartDialogPanel;
import net.disy.commons.swing.label.SmartLabel;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public abstract class AbstractLabeledSmartDialogPanel extends AbstractValidatingSmartDialogPanel implements IUpdatableSmartDialogPanel {
   private final SmartLabel label;
   private final GridAlignment verticalLabelAlignment;

   public AbstractLabeledSmartDialogPanel(String label, IMessageProducingValidator validator) {
      this(label, validator, GridAlignment.CENTER);
   }

   protected AbstractLabeledSmartDialogPanel(String label, IMessageProducingValidator validator, GridAlignment verticalLabelAlignment) {
      super(validator);
      Ensure.ensureArgumentNotNull(label);
      Ensure.ensureArgumentNotNull(verticalLabelAlignment);
      this.label = new SmartLabel(label);
      this.verticalLabelAlignment = verticalLabelAlignment;
   }

   @Override
   public final int getColumnCount() {
      return 1 + this.getMainComponentColumnCount();
   }

   @Override
   public final void fillInto(JPanel panel, int columnCount) {
      GridDialogLayoutData labelLayout = new GridDialogLayoutData();
      labelLayout.setHorizontalAlignment(GridAlignment.END);
      labelLayout.setVerticalAlignment(this.verticalLabelAlignment);
      panel.add(this.label, labelLayout);
      JComponent focusComponent = this.fillMainComponentInto(panel, columnCount - 1);
      if (focusComponent != null) {
         this.label.setLabelFor(focusComponent);
      }
   }

   @Override
   public final void setEnabled(boolean enabled) {
      this.label.setEnabled(enabled);
      this.setMainComponentEnabled(enabled);
   }

   protected abstract int getMainComponentColumnCount();

   protected abstract JComponent fillMainComponentInto(JPanel var1, int var2);

   protected abstract void setMainComponentEnabled(boolean var1);

   @Override
   public JComponent[] getComponents() {
      return ArrayUtilities.concat(this.label, this.getOtherComponents(), JComponent.class);
   }

   protected abstract JComponent[] getOtherComponents();

   @Override
   public void update() {
   }
}
