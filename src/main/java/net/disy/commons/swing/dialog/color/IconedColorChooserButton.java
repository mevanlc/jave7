package net.disy.commons.swing.dialog.color;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.button.DropDownButton;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.component.AbstractActionComponent;
import net.disy.commons.swing.icon.CompositeIcon;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;
import net.disy.commons.swing.toolbar.ToolBarBuilder;

public class IconedColorChooserButton extends AbstractActionComponent {
   public static final IconedColorChooserButton.IChooseStrategy DIRECT = new IconedColorChooserButton.IChooseStrategy() {
      @Override
      public ColorModel createColorModel(ColorModel model) {
         return model;
      }

      @Override
      public JComponent[] createContent(JButton button, IconedColorChooserButton chooserButton, ColorModel colorModel) {
         return new JComponent[]{button};
      }

      @Override
      public void execute(Component parentComponent, ColorModel model, IconedColorChooserButton button) {
         button.performColorChooserDialog(parentComponent);
      }

      @Override
      public String amendName(String name) {
         return name + "...";
      }
   };
   private static final Icon TEXT_ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("color/text_color.gif");
   private static final Icon MARKER_ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("color/marked_background_color.gif");
   private static final Icon BACKGROUND_ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("color/background_color.gif");
   private final JComponent content;
   private final JComponent[] components;
   private final ColorModel buttonModel;
   private final DefaultColorChooserConfiguration colorChooserConfiguration;
   private final JButton button;

   public static final IconedColorChooserButton createTextColorChooserButton(ColorModel colorModel, IconedColorChooserButton.IChooseStrategy strategy) {
      return new IconedColorChooserButton(TEXT_ICON, colorModel, DisyCommonsSwingMessages.getString("ColorChooserButton.FontColor.tooltip"), strategy);
   }

   public static final IconedColorChooserButton createMarkerColorChooserButton(ColorModel colorModel, IconedColorChooserButton.IChooseStrategy strategy) {
      return new IconedColorChooserButton(MARKER_ICON, colorModel, DisyCommonsSwingMessages.getString("ColorChooserButton.MarkerColor.tooltip"), strategy);
   }

   public static final IconedColorChooserButton createBackgroundColorChooserButton(ColorModel colorModel, IconedColorChooserButton.IChooseStrategy strategy) {
      return new IconedColorChooserButton(
         BACKGROUND_ICON, colorModel, DisyCommonsSwingMessages.getString("ColorChooserButton.BackgroundColor.tooltip"), strategy
      );
   }

   public IconedColorChooserButton(Icon baseIcon, final ColorModel model, String name, final IconedColorChooserButton.IChooseStrategy strategy) {
      Ensure.ensureArgumentNotNull(baseIcon);
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(strategy);
      this.buttonModel = strategy.createColorModel(model);
      SmartAction action = new SmartAction(strategy.amendName(name), this.createColorIcon(baseIcon)) {
         @Override
         protected void execute(Component parentComponent) {
            strategy.execute(parentComponent, model, IconedColorChooserButton.this);
         }
      };
      this.button = new JButton(action);
      this.button.setPreferredSize(LayoutUtilities.TOOLBAR_BUTTON_SIZE);
      this.button.setFocusPainted(false);
      this.components = strategy.createContent(this.button, this, model);
      ToolBarBuilder builder = new ToolBarBuilder();
      builder.add(this.components);
      this.content = builder.createToolBar();
      this.colorChooserConfiguration = new DefaultColorChooserConfiguration(name, false);
   }

   private Icon createColorIcon(Icon baseIcon) {
      class ColorIcon extends AbstractChangeableModel implements Icon {
         public ColorIcon() {
            IconedColorChooserButton.this.buttonModel.addChangeListener(new IChangeListener() {
               @Override
               public void stateChanged() {
                  ColorIcon.this.fireChangeEvent();
                  IconedColorChooserButton.this.button.repaint();
               }
            });
         }

         @Override
         public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(IconedColorChooserButton.this.buttonModel.getColor());
            g.fillRect(x + 0, y + 16 - 4, 16, 4);
         }

         @Override
         public int getIconWidth() {
            return 16;
         }

         @Override
         public int getIconHeight() {
            return 16;
         }
      }

      return new CompositeIcon(new ColorIcon(), baseIcon);
   }

   private void performColorChooserDialog(Component parentComponent) {
      Color color = this.buttonModel.getColor();
      if (color.getAlpha() == 0) {
         color = Color.WHITE;
      }

      Color newColor = ColorChooserDialog.showDialog(parentComponent, this.colorChooserConfiguration, color);
      if (newColor != null) {
         this.buttonModel.setColor(newColor);
      }

      this.fireActionEvent();
   }

   public JComponent getContent() {
      return this.content;
   }

   public static class DropDownStrategy implements IconedColorChooserButton.IChooseStrategy {
      private final boolean showTransparent;

      public DropDownStrategy(boolean showTransparent) {
         this.showTransparent = showTransparent;
      }

      @Override
      public ColorModel createColorModel(ColorModel model) {
         return new ColorModel(model.getColor());
      }

      @Override
      public JComponent[] createContent(JButton button, final IconedColorChooserButton chooserButton, final ColorModel colorModel) {
         DropDownButton dropDownButton = new DropDownButton(button);
         JPopupMenu popupMenu = new ColorPopupMenu(this.showTransparent) {
            @Override
            protected void performColorChooserDialog(Component parentComponent) {
               chooserButton.performColorChooserDialog(parentComponent);
               DropDownStrategy.this.applyColor(chooserButton, colorModel);
            }

            @Override
            protected void setColor(Color color) {
               chooserButton.buttonModel.setColor(color);
               DropDownStrategy.this.applyColor(chooserButton, colorModel);
            }
         };
         dropDownButton.setMenu(popupMenu);
         return dropDownButton.getSeparateComponents();
      }

      @Override
      public void execute(Component parentComponent, ColorModel model, IconedColorChooserButton button) {
         this.applyColor(button, model);
      }

      private void applyColor(IconedColorChooserButton button, ColorModel model) {
         model.setColor(button.buttonModel.getColor());
      }

      @Override
      public String amendName(String name) {
         return name;
      }
   }

   public interface IChooseStrategy {
      JComponent[] createContent(JButton var1, IconedColorChooserButton var2, ColorModel var3);

      ColorModel createColorModel(ColorModel var1);

      void execute(Component var1, ColorModel var2, IconedColorChooserButton var3);

      String amendName(String var1);
   }
}
