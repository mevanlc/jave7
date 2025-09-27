package net.disy.commons.swing.tree;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import net.disy.commons.swing.ui.DelegatingObjectUi;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiTreeCellRenderer;

public class EnhancedJTree<T> extends JTree implements Autoscroll {
   private static final Insets AUTOSCROLL_INSETS = new Insets(5, 5, 5, 5);
   private final List<IPainter> additinalPainters = new ArrayList<>();

   public EnhancedJTree(ISmartTree<T> smartTree, IObjectUi<T> objectUi) {
      super(new SmartTreeModel<>(smartTree));
      this.activateToolTips();
      this.setCellRenderer(new ObjectUiTreeCellRenderer(new DelegatingObjectUi<SmartTreeModelNode<T>, T>(objectUi) {
         protected T getDelegatingValue(SmartTreeModelNode<T> value) {
            return value.getNodeInSmartTree();
         }
      }));
      this.addMouseListener(new RightMouseClickTreeSelectionMouseListener());
   }

   private void activateToolTips() {
      ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
      toolTipManager.registerComponent(this);
   }

   public void addAdditionalPainter(IPainter painter) {
      this.additinalPainters.add(painter);
   }

   @Override
   public Insets getAutoscrollInsets() {
      Rectangle r = this.getVisibleRect();
      Dimension size = this.getSize();
      return new Insets(
         r.y + AUTOSCROLL_INSETS.top,
         r.x + AUTOSCROLL_INSETS.left,
         size.height - r.y - r.height + AUTOSCROLL_INSETS.bottom,
         size.width - r.x - r.width + AUTOSCROLL_INSETS.right
      );
   }

   @Override
   public void autoscroll(Point location) {
      JScrollPane scroller = (JScrollPane)SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
      if (scroller != null) {
         JScrollBar hBar = scroller.getHorizontalScrollBar();
         JScrollBar vBar = scroller.getVerticalScrollBar();
         Rectangle r = this.getVisibleRect();
         if (location.x <= r.x + AUTOSCROLL_INSETS.left) {
            hBar.setValue(hBar.getValue() - hBar.getUnitIncrement(-1));
         }

         if (location.y <= r.y + AUTOSCROLL_INSETS.top) {
            vBar.setValue(vBar.getValue() - vBar.getUnitIncrement(-1));
         }

         if (location.x >= r.x + r.width - AUTOSCROLL_INSETS.right) {
            hBar.setValue(hBar.getValue() + hBar.getUnitIncrement(1));
         }

         if (location.y >= r.y + r.height - AUTOSCROLL_INSETS.bottom) {
            vBar.setValue(vBar.getValue() + vBar.getUnitIncrement(1));
         }
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      for (IPainter painter : this.additinalPainters) {
         painter.paint(g);
      }
   }

   public SmartTreeModel<T> getSmartTreeModel() {
      return (SmartTreeModel<T>)this.getModel();
   }
}
