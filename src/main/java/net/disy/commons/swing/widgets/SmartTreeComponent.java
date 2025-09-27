package net.disy.commons.swing.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionGroupMenuBuilder;
import net.disy.commons.swing.action.GroupedMenuItem;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.ui.IObjectUi;

public class SmartTreeComponent<T> implements IComponentContainer {
   private final ListenerList<ITreeNodeActionListener<T>> nodeActionListeners = new ListenerList<>();
   private final JComponent content;
   private final EnhancedJTree<T> tree;
   private final SmartTreeSelectionModel<T> selectionModel;
   private final List<ISmartTreeActionFactory<T>> actionFactories = new ArrayList<>();
   private ISmartTree<T> smartTree;

   public SmartTreeComponent(ISmartTree<T> smartTree, IObjectUi<T> objectUi) {
      this.smartTree = smartTree;
      Ensure.ensureArgumentNotNull(smartTree);
      Ensure.ensureArgumentNotNull(objectUi);
      this.tree = new EnhancedJTree<>(smartTree, objectUi);
      this.tree.addMouseListener(new TreeDoubleClickMouseActionListener<>(this.nodeActionListeners));
      this.content = new JScrollPane(this.tree);
      this.selectionModel = new SmartTreeSelectionModel<>(this.tree.getSelectionModel(), this.tree.getSmartTreeModel());
      this.tree.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent event) {
            if (event.isMetaDown()) {
               SmartTreeComponent.this.showContextMenu(event);
            }
         }
      });
   }

   private void showContextMenu(MouseEvent event) {
      T[] selectionPath = this.getSelectionModel().getSelectionPath();
      ActionGroupMenuBuilder menuBuilder = new ActionGroupMenuBuilder();

      for (ISmartTreeActionFactory<T> actionFactory : this.actionFactories) {
         if (actionFactory.supportsPath(selectionPath)) {
            GroupedMenuItem menuItem = actionFactory.createMenuItem(selectionPath);
            if (menuItem != GroupedMenuItem.NO_ITEM) {
               menuBuilder.add(menuItem);
            }
         }
      }

      if (!menuBuilder.isEmpty()) {
         JPopupMenu contextMenu = menuBuilder.createPopupMenu();
         this.tree.add(contextMenu);
         contextMenu.show(this.tree, event.getX(), event.getY());
      }
   }

   public ISmartTree<T> getSmartTree() {
      return this.smartTree;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   public final void addNodeActionListener(ITreeNodeActionListener<T> listener) {
      this.nodeActionListeners.add(listener);
   }

   public final void removeNodeActionListener(ITreeNodeActionListener<T> listener) {
      this.nodeActionListeners.add(listener);
   }

   public void setRootVisible(boolean rootVisible) {
      this.tree.setRootVisible(rootVisible);
      this.tree.setShowsRootHandles(!rootVisible);
   }

   public void setSmartTree(ISmartTree<T> smartTree) {
      this.smartTree = smartTree;
      this.tree.setModel(new SmartTreeModel<>(smartTree));
   }

   public SmartTreeSelectionModel<T> getSelectionModel() {
      return this.selectionModel;
   }

   public void expandPath(T[] path) {
      this.tree.expandPath(new TreePath(this.tree.getSmartTreeModel().getPathInModel(path)));
   }

   public void addActionFactory(ISmartTreeActionFactory<T> actionFactory) {
      this.actionFactories.add(actionFactory);
   }

   public void applyAddition(ISmartTreeAddition<T> addition) {
      addition.applyTo(this, this.tree);
   }

   public void setSelectionMode(TreeSelectionMode selectionMode) {
      this.tree.getSelectionModel().setSelectionMode(selectionMode.getTreeSelectionMode());
   }

   public void nodeInserted(T[] nodePath) {
      SmartTreeModel<T> smartTreeModel = this.tree.getSmartTreeModel();
      SmartTreeModelNode<T>[] pathInModel = smartTreeModel.getPathInModel(nodePath);
      smartTreeModel.nodeWasInserted(
         pathInModel[pathInModel.length - 2], this.smartTree.getIndexOfChild(nodePath[nodePath.length - 2], nodePath[nodePath.length - 1])
      );
   }

   public void startEditing(T[] path) {
      this.tree.startEditingAtPath(new TreePath(this.tree.getSmartTreeModel().getPathInModel(path)));
   }
}
