package net.disy.commons.swing.button;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;

public class DropDownButton {
   private static final Icon ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("button/drop_down.gif");
   private static final Icon DISABLED_ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("button/drop_down_disabled.gif");
   private JPopupMenu menu;
   private final JButton button;
   private final JComponent content;
   private final AbstractButton parentButton;

   public DropDownButton() {
      this(null);
   }

   public DropDownButton(final AbstractButton parentButton) {
      this.parentButton = parentButton;
      this.button = new JButton();
      this.button.setIcon(ICON);
      this.button.setDisabledIcon(DISABLED_ICON);
      this.setPreferredDropDownButtonHeight(22);
      this.button.setMargin(new Insets(0, 0, 0, 0));
      this.button.setFocusPainted(false);
      this.button.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (DropDownButton.this.button.isEnabled()) {
               int menuWidth = DropDownButton.this.menu.getPreferredSize().width;
               int x;
               if (parentButton != null) {
                  x = -parentButton.getWidth();
               } else {
                  x = DropDownButton.this.button.getWidth() - menuWidth;
               }

               DropDownButton.this.menu.show(DropDownButton.this.button, x, DropDownButton.this.button.getHeight());
            }
         }
      });
      if (parentButton != null) {
         connectButtonModelsForRollover(parentButton.getModel(), this.button.getModel());
         parentButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               DropDownButton.this.updateEnabled();
            }
         });
         parentButton.addPropertyChangeListener("ToolTipText", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
               DropDownButton.this.adaptToolTipText();
            }
         });
         this.adaptToolTipText();
         JPanel panel = new JPanel(new BorderLayout());
         panel.add(parentButton, "Center");
         panel.add(this.button, "East");
         this.content = panel;
      } else {
         this.content = this.button;
      }

      this.updateEnabled();
   }

   private void adaptToolTipText() {
      this.button.setToolTipText(this.parentButton.getToolTipText());
   }

   public void setPreferredDropDownButtonHeight(int height) {
      this.button.setPreferredSize(new Dimension(13, height));
   }

   private static void connectButtonModelsForRollover(final ButtonModel model1, final ButtonModel model2) {
      model1.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model2.setRollover(model1.isRollover());
         }
      });
      model2.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model1.setRollover(model2.isRollover());
         }
      });
   }

   private void updateEnabled() {
      this.button.setEnabled(this.menu != null && (this.parentButton == null || this.parentButton.isEnabled()));
   }

   public void setMenu(JPopupMenu menu) {
      this.menu = menu;
      this.updateEnabled();
   }

   public JComponent getComponent() {
      return this.content;
   }

   public JComponent[] getSeparateComponents() {
      return this.parentButton == null ? new JComponent[]{this.button} : new JComponent[]{this.parentButton, this.button};
   }

   public void setToolTipText(String text) {
      this.button.setToolTipText(text);
   }
}
