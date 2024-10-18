package net.disy.commons.swing.toolbar;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.component.VerticalLine;
import net.disy.commons.swing.layout.util.LayoutDirection;
import net.disy.commons.swing.widgets.HorizontalLine;

public class ToolBarUtilities {
   public static final Insets TOOLBAR_MARGIN = new Insets(1, 1, 1, 1);
   private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

   @Deprecated
   public static AbstractButton createToolBarButton(Action action) {
      return createToolBarButton(action, new DefaultToolBarButtonConfiguration());
   }

   public static AbstractButton createToolBarButton(Action action, IToolBarButtonConfiguration configuration) {
      AbstractButton button;
      if (action instanceof SmartToggleAction) {
         button = ActionWidgetFactory.createToggleButton((SmartToggleAction)action);
      } else {
         button = new JButton();
      }

      button.setAction(action);
      configureToolBarButton(button, configuration);
      return button;
   }

   @Deprecated
   public static void configureToolBarButton(AbstractButton button) {
      configureToolBarButton(button, new DefaultToolBarButtonConfiguration());
   }

   public static void configureToolBarButton(AbstractButton button, IToolBarButtonConfiguration configuration) {
      button.setFocusPainted(configuration.isFocusPainted());
      button.setMargin(TOOLBAR_BUTTON_MARGIN);
      if (button.getToolTipText() == null) {
         button.setToolTipText(button.getText());
      }

      if (button.getIcon() != null) {
         button.setText(null);
      }
   }

   public static JToolBar createEmptyToolBar() {
      return createEmptyToolBar(LayoutDirection.HORIZONTAL);
   }

   public static JToolBar createEmptyToolBar(IToolBarConfiguration configuration) {
      JToolBar toolBar = createEmptyToolBar(configuration.getOrientation());
      toolBar.setFloatable(configuration.isFloatable());
      toolBar.setRollover(configuration.isRolloverEffectEnabled());
      return toolBar;
   }

   public static JToolBar createEmptyToolBar(final LayoutDirection orientation) {
      int swingOrientation = getSwingConstant(orientation);
      JToolBar toolBar = new JToolBar(swingOrientation) {
         @Override
         public Component add(Component c) {
            if (c instanceof JToolBar) {
               JToolBar addedToolBar = (JToolBar)c;
               addedToolBar.setBorder(BorderFactory.createEmptyBorder());
               addedToolBar.setMargin(new Insets(0, 0, 0, 0));
               addedToolBar.putClientProperty("Plastic.is3D", Boolean.FALSE);
               addedToolBar.setOpaque(false);
            }

            return super.add(c);
         }

         @Override
         public void addSeparator() {
            switch (orientation) {
               case HORIZONTAL:
                  super.add(new VerticalLine());
                  return;
               case VERTICAL:
                  super.add(new HorizontalLine());
                  return;
               default:
                  throw new RuntimeException("Unsupported orientation " + orientation);
            }
         }
      };
      if (orientation == LayoutDirection.HORIZONTAL) {
         toolBar.setLayout(new FlowLayout(0, 0, 0));
      }

      setToolBarProperties(toolBar);
      return toolBar;
   }

   private static int getSwingConstant(LayoutDirection orientation) {
      switch (orientation) {
         case HORIZONTAL:
            return 0;
         case VERTICAL:
            return 1;
         default:
            throw new RuntimeException("Unsupported orientation " + orientation);
      }
   }

   public static void setToolBarProperties(JToolBar toolBar) {
      toolBar.setFloatable(false);
      toolBar.setOpaque(false);
      toolBar.setMargin(TOOLBAR_MARGIN);
      toolBar.setRollover(true);
   }

   public static void addToolBarButton(JToolBar toolBar, Action action) {
      toolBar.add(createToolBarButton(action));
   }
}
