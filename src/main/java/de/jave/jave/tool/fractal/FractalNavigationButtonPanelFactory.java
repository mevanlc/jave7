package de.jave.jave.tool.fractal;

import de.jave.gui.layout.Gap;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.toolbar.ToolBarUtilities;

public class FractalNavigationButtonPanelFactory {
   public static JComponent createButtonPanel(final FractalCutoutModel model, final Dimension plateSize) {
      SmartAction inAction = new SmartAction("Navigate in", JaveIcons.NAVIGATE_NAVIGATE_IN) {
         @Override
         protected void execute(Component parentComponent) {
            model.setXSpan(model.getXSpan() * 3.0 / 4.0);
         }
      };
      SmartAction outAction = new SmartAction("Navigate out", JaveIcons.NAVIGATE_NAVIGATE_OUT) {
         @Override
         protected void execute(Component parentComponent) {
            model.setXSpan(model.getXSpan() * 4.0 / 3.0);
         }
      };
      SmartAction leftAction = new SmartAction("Navigate left", JaveIcons.NAVIGATE_MOVE_LEFT) {
         @Override
         protected void execute(Component parentComponent) {
            double x = model.getX();
            double span = model.getXSpan();
            double newX = x - span / 5.0;
            model.setX(newX);
         }
      };
      SmartAction rightAction = new SmartAction("Navigate right", JaveIcons.NAVIGATE_MOVE_RIGHT) {
         @Override
         protected void execute(Component parentComponent) {
            double x = model.getX();
            double span = model.getXSpan();
            double newX = x + span / 5.0;
            model.setX(newX);
         }
      };
      SmartAction upAction = new SmartAction("Navigate up", JaveIcons.NAVIGATE_MOVE_UP) {
         @Override
         protected void execute(Component parentComponent) {
            double y = model.getY();
            double xSpan = model.getXSpan();
            double ySpan = xSpan / (double)plateSize.width * (double)plateSize.height * 1.5;
            double newY = y - ySpan / 5.0;
            model.setY(newY);
         }
      };
      SmartAction downAction = new SmartAction("Navigate down", JaveIcons.NAVIGATE_MOVE_DOWN) {
         @Override
         protected void execute(Component parentComponent) {
            double y = model.getY();
            double xSpan = model.getXSpan();
            double ySpan = xSpan / (double)plateSize.width * (double)plateSize.height * 1.5;
            double newY = y + ySpan / 5.0;
            model.setY(newY);
         }
      };
      SmartAction resetAction = new SmartAction("Reset", JaveIcons.NAVIGATE_RESET) {
         @Override
         protected void execute(Component parentComponent) {
            model.reset();
         }
      };
      JPanel p1 = new JPanel(new GridDialogLayout(3, true, 0, 0));
      p1.add(new Gap());
      p1.add(ToolBarUtilities.createToolBarButton(upAction));
      p1.add(new Gap());
      p1.add(ToolBarUtilities.createToolBarButton(leftAction));
      p1.add(new Gap());
      p1.add(ToolBarUtilities.createToolBarButton(rightAction));
      p1.add(new Gap());
      p1.add(ToolBarUtilities.createToolBarButton(downAction));
      JPanel p2 = new JPanel(new GridDialogLayout(1, true, 0, 0));
      p2.add(ToolBarUtilities.createToolBarButton(inAction));
      p2.add(ToolBarUtilities.createToolBarButton(outAction));
      JPanel panel = new JPanel(new GridDialogLayout(3, false, LayoutUtilities.getComponentGroupsSpacing(), 0));
      panel.add(p1);
      panel.add(p2);
      panel.add(ToolBarUtilities.createToolBarButton(resetAction), new GridDialogLayoutData().setVerticalAlignment(GridAlignment.END));
      return panel;
   }
}
