package net.disy.commons.swing.dialog.tabbed.internal;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.SimpleBlock;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.util.GuiUtilities;

public final class TabTitleComponent extends JPanel {
   private final BooleanModel isActiveTabModel = new BooleanModel();
   private final JLabel label;

   public TabTitleComponent(String title, Icon icon, final ITabCloseRequestHandler optionalCloseHandler) {
      super(new BorderLayout(LayoutUtilities.getDpiAdjusted(3), 0));
      Ensure.ensureArgumentNotNull(this.isActiveTabModel);
      this.label = new JLabel(icon, 2) {
         @Override
         protected void paintComponent(Graphics g) {
            if (!GuiUtilities.isContainedInActiveWindow(this) && TabTitleComponent.this.isActiveTabModel.getValue()) {
               this.setForeground(SystemColor.inactiveCaptionText);
            } else {
               this.setForeground(SwingColors.getLabelForegroundColor());
            }

            super.paintComponent(g);
         }
      };
      this.setText(title);
      this.add(this.label, "Center");
      if (optionalCloseHandler != null) {
         TabCloseButtonComponent closeButton = new TabCloseButtonComponent(this, this.isActiveTabModel, new SimpleBlock() {
            @Override
            public void execute() throws RuntimeException {
               optionalCloseHandler.handleCloseRequested(TabTitleComponent.this);
            }
         });
         closeButton.setToolTipText(DisyCommonsSwingDialogMessages.CLOSE);
         this.add(closeButton, "East");
      }

      this.setOpaque(false);
   }

   public void setIsActiveTab(boolean active) {
      this.isActiveTabModel.setValue(active);
   }

   public void setIcon(Icon icon) {
      this.label.setIcon(icon);
   }

   public void setText(String text) {
      this.label.setText(text);
      this.label.setToolTipText(StringUtilities.isNullOrTrimmedEmpty(text) ? null : text);
   }

   @Override
   public synchronized void addMouseListener(MouseListener l) {
      super.addMouseListener(l);
      this.label.addMouseListener(l);
   }

   @Override
   public synchronized void removeMouseListener(MouseListener l) {
      super.removeMouseListener(l);
      this.label.removeMouseListener(l);
   }
}
