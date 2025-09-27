package net.disy.commons.swing.border;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.core.util.NullClosure;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IGridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class TitledPanel extends JPanel {
   public TitledPanel(String title, JComponent content) {
      this(title, content, new GridDialogLayoutData());
   }

   public TitledPanel(String title, JComponent content, IGridDialogLayoutData layoutData) {
      this(title, content, layoutData, new NullClosure<>());
   }

   public TitledPanel(String title, final JComponent content, IGridDialogLayoutData layoutData, IClosure<TitledBorder> decorator) {
      super(new GridLayout(1, 0));
      Ensure.ensureArgumentNotNull(title);
      Ensure.ensureArgumentNotNull(content);
      TitledBorder titledBorder = new TitledBorder(title);
      BorderUtilities.attachDisableableTitledBorder(this, titledBorder);
      decorator.execute(titledBorder);
      this.setBorder(
         new CompoundBorder(
            this.getBorder(),
            new EmptyBorder(
               LayoutUtilities.getDpiAdjusted(2), LayoutUtilities.getDpiAdjusted(4), LayoutUtilities.getDpiAdjusted(4), LayoutUtilities.getDpiAdjusted(4)
            )
         )
      );
      this.add(content, layoutData);
      content.addPropertyChangeListener("enabled", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            TitledPanel.this.setEnabled(content.isEnabled());
         }
      });
      this.setEnabled(content.isEnabled());
   }

   public void setTitle(String title) {
      TitledBorder titledBorder = this.getTitledBorder();
      titledBorder.setTitle(title);
   }

   public String getTitle() {
      return this.getTitledBorder().getTitle();
   }

   private TitledBorder getTitledBorder() {
      CompoundBorder compoundBorder = (CompoundBorder)this.getBorder();
      return (TitledBorder)compoundBorder.getOutsideBorder();
   }
}
