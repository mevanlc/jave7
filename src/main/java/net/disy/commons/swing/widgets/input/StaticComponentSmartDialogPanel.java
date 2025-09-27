package net.disy.commons.swing.dialog.input;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public final class StaticComponentSmartDialogPanel implements ISmartDialogPanel {
   private final JComponent component;

   public StaticComponentSmartDialogPanel(IComponentContainer componentContainer) {
      this(componentContainer.getContent());
   }

   public StaticComponentSmartDialogPanel(JComponent component) {
      Ensure.ensureArgumentNotNull(component);
      this.component = component;
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      panel.add(this.component, GridDialogLayoutDataFactory.createHorizontalSpanData(columnCount, GridDialogLayoutData.FILL_BOTH));
   }

   @Override
   public void requestFocus() {
      this.component.requestFocus();
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return null;
   }

   @Override
   public void addRequestFinishListener(IRequestFinishListener requestFinishListener) {
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
   }
}
