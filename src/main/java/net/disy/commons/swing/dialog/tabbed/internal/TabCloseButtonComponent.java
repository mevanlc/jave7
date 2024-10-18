package net.disy.commons.swing.dialog.tabbed.internal;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.SimpleBlock;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogIconResources;

public final class TabCloseButtonComponent extends JComponent {
   private TabCloseButtonState state = TabCloseButtonState.HIDDEN;
   private final BooleanModel isActiveTabModel;

   public TabCloseButtonComponent(JComponent container, BooleanModel isActiveTabModel, final SimpleBlock closeHandlerBlock) {
      Ensure.ensureArgumentNotNull(container);
      Ensure.ensureArgumentNotNull(isActiveTabModel);
      Ensure.ensureArgumentNotNull(closeHandlerBlock);
      this.isActiveTabModel = isActiveTabModel;
      container.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseExited(MouseEvent e) {
            TabCloseButtonComponent.this.setState(TabCloseButtonState.HIDDEN);
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            TabCloseButtonComponent.this.setState(TabCloseButtonState.INACTIVE);
         }
      });
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (!e.isMetaDown()) {
               TabCloseButtonComponent.this.setState(TabCloseButtonState.PRESSED);
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            if (TabCloseButtonComponent.this.state == TabCloseButtonState.PRESSED) {
               TabCloseButtonComponent.this.setState(TabCloseButtonState.ACTIVE);
               closeHandlerBlock.execute();
            }
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            TabCloseButtonComponent.this.setState(TabCloseButtonState.ACTIVE);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            TabCloseButtonComponent.this.setState(TabCloseButtonState.HIDDEN);
         }
      });
   }

   private void setState(TabCloseButtonState state) {
      if (this.state != state) {
         this.state = state;
         this.repaint();
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(16, 16);
   }

   @Override
   protected void paintComponent(Graphics g) {
      switch (this.state) {
         case ACTIVE:
            DisyCommonsSwingDialogIconResources.TAB_CLOSE_ACTIVE.paintIcon(this, g, 0, 0);
            break;
         case INACTIVE:
            DisyCommonsSwingDialogIconResources.TAB_CLOSE_INACTIVE.paintIcon(this, g, 0, 0);
            break;
         case HIDDEN:
            if (this.isActiveTab()) {
               DisyCommonsSwingDialogIconResources.TAB_CLOSE_INACTIVE.paintIcon(this, g, 0, 0);
            }
            break;
         case PRESSED:
            DisyCommonsSwingDialogIconResources.TAB_CLOSE_ACTIVE.paintIcon(this, g, 1, 1);
      }
   }

   private boolean isActiveTab() {
      return this.isActiveTabModel.getValue();
   }
}
