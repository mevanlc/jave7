package net.disy.commons.swing.dialog.color;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class ColorChooserButton extends AbstractColorChoosingComponent {
   public ColorChooserButton() {
      this(new ColorModel());
   }

   public ColorChooserButton(ColorModel model) {
      super(model);
   }

   public ColorChooserButton(ColorModel model, IColorChooserConfiguration configuration) {
      super(model, configuration);
   }

   @Override
   protected final JComponent createContent() {
      JButton button = this.createButton();
      button.setOpaque(false);
      button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ColorChooserButton.this.performColorChooseDialog();
         }
      });
      button.setToolTipText(this.getTooltipText());
      return button;
   }

   private String getTooltipText() {
      return DisyCommonsSwingMessages.getString("ColorChooseButton.ToolTip");
   }

   private JButton createButton() {
      JButton button = new JButton();
      Icon icon = this.createIcon(button);
      button.setIcon(icon);
      int twoPixels = LayoutUtilities.getDpiAdjusted(2);
      button.setMargin(new Insets(twoPixels, twoPixels, twoPixels, twoPixels));
      return button;
   }

   private Icon createIcon(final JButton button) {
      return new Icon() {
         @Override
         public int getIconHeight() {
            return 13;
         }

         @Override
         public int getIconWidth() {
            return 49;
         }

         @Override
         public void paintIcon(Component c, Graphics g, int x, int y) {
            ColorChooserButton.this.paintColorRectangle(g, x, y, new Dimension(this.getIconWidth(), this.getIconHeight()), button.isEnabled());
         }
      };
   }
}
