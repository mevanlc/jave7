package net.disy.commons.swing.action;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.disy.commons.core.grouped.ComparableGroupHandler;
import net.disy.commons.core.grouped.IGroupedItem;
import net.disy.commons.core.grouped.IStructuredItemAddable;
import net.disy.commons.core.grouped.SeparatorGroupItemStructureBuilder;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.grouped.MenuItemAddable;
import net.disy.commons.swing.action.grouped.PopupMenuItemAddable;

public class ActionGroupMenuBuilder {
   private final SeparatorGroupItemStructureBuilder<ActionGroupId, JMenuItem> structureBuilder = new SeparatorGroupItemStructureBuilder(
      new ComparableGroupHandler()
   );

   public JPopupMenu createPopupMenu() {
      JPopupMenu menu = new JPopupMenu();
      this.addAllItemsPriorized(new PopupMenuItemAddable(menu));
      return menu;
   }

   public JMenu createMenu(IActionConfiguration menuConfiguration) {
      JMenu menu = new JMenu(new SmartAction(menuConfiguration) {
         @Override
         protected void execute(Component parentComponent) {
         }
      });
      this.addAllItemsPriorized(new MenuItemAddable(menu));
      return menu;
   }

   private void addAllItemsPriorized(IStructuredItemAddable<JMenuItem> addable) {
      this.structureBuilder.addAllItemsTo(addable);
   }

   public void add(ActionGroupId groupId, Action action) {
      this.add(new GroupedMenuItem(action, groupId));
   }

   public void add(ActionGroupId groupId, JMenuItem menuItem) {
      this.add(new GroupedMenuItem(menuItem, groupId));
   }

   public void add(GroupedMenuItem menuItem) {
      Ensure.ensureArgumentNotNull(menuItem);
      this.structureBuilder.add(new ActionGroupMenuBuilder.GroupedItemWrapper(menuItem));
   }

   public void add(GroupedMenuItem[] menuItems) {
      Ensure.ensureArgumentNotNull(menuItems);
      Ensure.ensureArgumentArrayContentsNotNull(menuItems);

      for (GroupedMenuItem menuItem : menuItems) {
         this.structureBuilder.add(new ActionGroupMenuBuilder.GroupedItemWrapper(menuItem));
      }
   }

   public boolean isEmpty() {
      return this.structureBuilder.isEmpty();
   }

   private static final class GroupedItemWrapper implements IGroupedItem<ActionGroupId, JMenuItem> {
      private final GroupedMenuItem menuItem;

      private GroupedItemWrapper(GroupedMenuItem menuItem) {
         this.menuItem = menuItem;
      }

      public ActionGroupId getGroupId() {
         return this.menuItem.getActionGroupId();
      }

      public JMenuItem getItem() {
         return this.menuItem.getMenuItem();
      }
   }
}
