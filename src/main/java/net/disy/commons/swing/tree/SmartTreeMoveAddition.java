package net.disy.commons.swing.tree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import net.disy.commons.swing.color.ColorUtilities;
import net.disy.commons.swing.color.SwingColors;

public class SmartTreeMoveAddition<T> implements ISmartTreeAddition<T> {
   private DragSource dragSource;
   private int currentDropLineIndex = -1;
   private SmartTreeModelNode<T> draggingTreeNode;
   private Timer nodeToggleTimer;
   private SmartTreeModelNode<T> togglingNode;
   private final ISmartTreeMoveStrategy<T> moveStrategy;
   private final DataFlavor defaultDataFlavor;

   public SmartTreeMoveAddition(ISmartTreeMoveStrategy<T> moveStrategy, DataFlavor defaultDataFlavor) {
      this.moveStrategy = moveStrategy;
      this.defaultDataFlavor = defaultDataFlavor;
   }

   @Override
   public void applyTo(SmartTreeComponent<T> smartTreeComponent, final EnhancedJTree<T> tree) {
      final ISmartTree<T> smartTree = smartTreeComponent.getSmartTree();
      this.dragSource = DragSource.getDefaultDragSource();
      this.dragSource.createDefaultDragGestureRecognizer(tree, 2, new DragGestureListener() {
         @Override
         public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
            SmartTreeMoveAddition.this.dragGestureRecognized(dragGestureEvent, tree);
         }
      });
      DropTarget dropTarget = new DropTarget(tree, new DropTargetListener() {
         @Override
         public void dragEnter(DropTargetDragEvent evt) {
         }

         @Override
         public void dragOver(DropTargetDragEvent evt) {
            Point location = evt.getLocation();
            SmartTreeMoveAddition.this.setCurrentDropLineIndex(tree, tree.getClosestRowForLocation(location.x, location.y));
            TreePath draggedTreePath = tree.getClosestPathForLocation(location.x, location.y);
            SmartTreeModelNode<T> node = (SmartTreeModelNode<T>)draggedTreePath.getLastPathComponent();
            if (smartTree.getChildCount(node.getNodeInSmartTree()) > 0) {
               SmartTreeMoveAddition.this.startNodeToggleTimer(tree, node);
            } else {
               SmartTreeMoveAddition.this.cancelNodeToggleTimer();
            }
         }

         @Override
         public void dropActionChanged(DropTargetDragEvent evt) {
            SmartTreeMoveAddition.this.cancelNodeToggleTimer();
         }

         @Override
         public void dragExit(DropTargetEvent evt) {
            SmartTreeMoveAddition.this.cancelNodeToggleTimer();
            SmartTreeMoveAddition.this.setCurrentDropLineIndex(tree, -1);
         }

         @Override
         public void drop(DropTargetDropEvent evt) {
            SmartTreeMoveAddition.this.drop(tree, smartTree);
         }
      });
      dropTarget.setDefaultActions(3);
      tree.addAdditionalPainter(new IPainter() {
         @Override
         public void paint(Graphics g) {
            if (SmartTreeMoveAddition.this.getCurrentDropLineIndex() != -1) {
               SmartTreeMoveAddition.this.paintDrag(tree, smartTree, g);
            }
         }
      });
   }

   protected void drop(EnhancedJTree<T> tree, ISmartTree<T> smartTree) {
      this.cancelNodeToggleTimer();
      int dropLineIndex = this.getCurrentDropLineIndex();
      this.setCurrentDropLineIndex(tree, -1);
      SmartTreeModel<T> model = tree.getSmartTreeModel();
      SmartTreeModelNode<T> oldParent = this.draggingTreeNode.getParent();
      int oldIndex = smartTree.getIndexOfChild(oldParent.getNodeInSmartTree(), this.draggingTreeNode.getNodeInSmartTree());
      TreePath dropLinePath = tree.getPathForRow(dropLineIndex);
      if (dropLinePath != null) {
         SmartTreeModelNode<T> dropLineTreeNode = (SmartTreeModelNode<T>)dropLinePath.getLastPathComponent();
         SmartTreeModelNode<T> newParentTreeNode;
         int newIndex;
         if (model.isLeaf(dropLineTreeNode)) {
            newParentTreeNode = (SmartTreeModelNode<T>)dropLinePath.getParentPath().getLastPathComponent();
            newIndex = model.getIndexOfChild(newParentTreeNode, dropLineTreeNode) + 1;
         } else {
            newParentTreeNode = dropLineTreeNode;
            if (tree.isExpanded(dropLineIndex)) {
               newIndex = 0;
            } else {
               newIndex = smartTree.getChildCount(dropLineTreeNode.getNodeInSmartTree());
            }
         }

         if (oldParent == newParentTreeNode && oldIndex < newIndex) {
            newIndex--;
         }

         if (!tree.getSmartTreeModel().isAncestorOrSame(this.draggingTreeNode, newParentTreeNode)) {
            this.moveStrategy
               .handleNodeMoved(this.draggingTreeNode.getNodeInSmartTree(), oldParent.getNodeInSmartTree(), newParentTreeNode.getNodeInSmartTree(), newIndex);
            model.nodeWasRemoved(oldParent, oldIndex, this.draggingTreeNode);
            model.nodeWasInserted(newParentTreeNode, newIndex);
         }
      }
   }

   public void dragGestureRecognized(DragGestureEvent dragGestureEvent, JTree tree) {
      if (tree.getSelectionCount() == 1) {
         TreePath path = tree.getSelectionPath();
         if (!tree.isEditing() && path != null) {
            this.draggingTreeNode = (SmartTreeModelNode<T>)path.getLastPathComponent();
            if (this.draggingTreeNode != tree.getModel().getRoot() && this.moveStrategy.isMovableTreeNode(this.draggingTreeNode.getNodeInSmartTree())) {
               TreeNodeTransferableAdapter<T> node = new TreeNodeTransferableAdapter<>(this.draggingTreeNode.getNodeInSmartTree(), this.defaultDataFlavor);
               DragSourceListener dragSourceListener = new DragSourceListener() {
                  @Override
                  public void dragDropEnd(DragSourceDropEvent evt) {
                  }

                  @Override
                  public void dragEnter(DragSourceDragEvent evt) {
                  }

                  @Override
                  public void dragExit(DragSourceEvent evt) {
                  }

                  @Override
                  public void dragOver(DragSourceDragEvent evt) {
                  }

                  @Override
                  public void dropActionChanged(DragSourceDragEvent evt) {
                  }
               };
               this.dragSource.startDrag(dragGestureEvent, DragSource.DefaultMoveDrop, node, dragSourceListener);
            } else {
               this.draggingTreeNode = null;
            }
         }
      }
   }

   private void setCurrentDropLineIndex(EnhancedJTree tree, int currentDragLineIndex) {
      if (this.currentDropLineIndex != currentDragLineIndex) {
         this.currentDropLineIndex = currentDragLineIndex;
         tree.repaint();
      }
   }

   private synchronized void startNodeToggleTimer(final EnhancedJTree<T> tree, SmartTreeModelNode<T> node) {
      if (node != this.togglingNode) {
         this.cancelNodeToggleTimer();
         this.togglingNode = node;
         TreePath tp = new TreePath(tree.getSmartTreeModel().getPathToRoot(this.togglingNode));
         int delay = 0;
         short var5;
         if (tree.isExpanded(tp)) {
            var5 = 2000;
         } else {
            var5 = 1000;
         }

         this.nodeToggleTimer = new Timer();
         this.nodeToggleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               TreePath treePath = new TreePath(tree.getSmartTreeModel().getPathToRoot(SmartTreeMoveAddition.this.togglingNode));
               SmartTreeMoveAddition.this.toggleExpanded(tree, treePath);
               SmartTreeMoveAddition.this.togglingNode = null;
               tree.repaint();
            }
         }, var5);
      }
   }

   private synchronized void cancelNodeToggleTimer() {
      if (this.nodeToggleTimer != null) {
         this.nodeToggleTimer.cancel();
         this.nodeToggleTimer = null;
         this.togglingNode = null;
      }
   }

   public final void toggleExpanded(EnhancedJTree<T> tree, TreePath path) {
      if (tree.isExpanded(path)) {
         tree.collapsePath(path);
      } else {
         tree.expandPath(path);
      }
   }

   private int getCurrentDropLineIndex() {
      return this.currentDropLineIndex;
   }

   private Color getDropTargetLineColor(EnhancedJTree<T> tree) {
      Color lineColor = SwingColors.getTreeLineColor();
      if (lineColor == null) {
         lineColor = Color.BLACK;
      }

      Color backgroundColor = tree.getBackground();
      return ColorUtilities.createIntermediateColor(lineColor, backgroundColor);
   }

   private void paintDrag(EnhancedJTree<T> tree, ISmartTree<T> smartTree, Graphics g) {
      TreePath draggedTreePath = tree.getPathForRow(this.getCurrentDropLineIndex());
      Rectangle r = tree.getUI().getPathBounds(tree, draggedTreePath);
      g.setColor(this.getDropTargetLineColor(tree));
      SmartTreeModelNode<T> node = (SmartTreeModelNode<T>)draggedTreePath.getLastPathComponent();
      if (!smartTree.isLeaf(node.getNodeInSmartTree())) {
         int y = r.y + r.height;
         int x0 = r.x + 9;
         int x1 = x0 + Math.max(40, r.width);
         g.drawLine(x0, y - 2, x0, y - 1);
         g.drawLine(x0, y - 1, x0 + 2, y + 1);
         g.drawLine(x0 + 2, y + 1, x1, y + 1);
      } else {
         int levelWidth = 16;
         int y = r.y + r.height;
         int x0 = r.x - 16 + 3;
         int x1 = x0 + Math.max(40, r.width) + 16;
         g.drawLine(x0, y - 7, x0, y - 4);
         g.drawLine(x0, y - 4, x0 + 4, y);
         g.drawLine(x0 + 4, y, x1, y);
      }
   }
}
