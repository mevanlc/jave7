package net.disy.commons.swing.dialog.tabbed;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.model.listener.NotifyChangeListenerClosure;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.dialog.tabbed.internal.ExtendedJTabbedPane;
import net.disy.commons.swing.dialog.tabbed.internal.ITabCloseRequestHandler;
import net.disy.commons.swing.dialog.tabbed.internal.SmartTabbedPaneUi;
import net.disy.commons.swing.dialog.tabbed.internal.TabTitleComponent;
import net.disy.commons.swing.util.GuiUtilities;

public class SmartTabbedPane implements IComponentContainer {
   private final ListenerList<IChangeListener> selectionChangeListenerList = new ListenerList<>();
   private final ExtendedJTabbedPane tabbedPane;
   private ISmartTabbedPanePopupMenuFactory popupMenuFactory;
   private final ISmartTabbedPaneCloseHandler optionalCloseHandler;

   public SmartTabbedPane(ISmartTabbedPaneCloseHandler closeHandler) {
      this(TabPlacement.TOP, closeHandler);
   }

   public SmartTabbedPane(TabPlacement placement, ISmartTabbedPaneCloseHandler optionalCloseHandler) {
      Ensure.ensureArgumentNotNull(placement);
      this.optionalCloseHandler = optionalCloseHandler;
      this.tabbedPane = new ExtendedJTabbedPane(placement, 1);
      this.tabbedPane.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            SmartTabbedPane.this.selectionChangeListenerList.forAllDo(NotifyChangeListenerClosure.INSTANCE);
            SmartTabbedPane.this.updateAllTitleComponentsForActive();
         }
      });
      SmartTabbedPaneUi ui = new SmartTabbedPaneUi();
      this.tabbedPane.setUI(ui);
   }

   private void updateAllTitleComponentsForActive() {
      int selectedTabIndex = this.tabbedPane.getSelectedIndex();

      for (int index = 0; index < this.tabbedPane.getTabCount(); index++) {
         TabTitleComponent titleComponent = (TabTitleComponent)this.tabbedPane.getTabComponentAt(index);
         if (titleComponent != null) {
            titleComponent.setIsActiveTab(index == selectedTabIndex);
         }
      }
   }

   public void addTabSelectionChangeListener(IChangeListener listener) {
      this.selectionChangeListenerList.add(listener);
   }

   @Override
   public JComponent getContent() {
      return this.tabbedPane;
   }

   public void addTab(String title, Icon icon, JComponent content) {
      this.tabbedPane.addTab(null, content);
      int tabIndex = this.tabbedPane.getTabCount() - 1;
      ITabCloseRequestHandler closeRequestHandler = this.optionalCloseHandler == null ? null : new ITabCloseRequestHandler() {
         @Override
         public void handleCloseRequested(TabTitleComponent titleComponent) {
            int index = SmartTabbedPane.this.getTabIndexByTitleComponent(titleComponent);
            SmartTabbedPane.this.optionalCloseHandler.handleTabClosing(SmartTabbedPane.this, index);
         }
      };
      final TabTitleComponent tabTitleComponent = new TabTitleComponent(title, icon, closeRequestHandler);
      this.tabbedPane.setTabComponentAt(tabIndex, tabTitleComponent);
      this.updateAllTitleComponentsForActive();
      tabTitleComponent.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (!e.isMetaDown()) {
               int index = SmartTabbedPane.this.getTabIndexByTitleComponent(tabTitleComponent);
               SmartTabbedPane.this.tabbedPane.setSelectedIndex(index);
               if (!GuiUtilities.containsFocusOwner(SmartTabbedPane.this.tabbedPane)) {
                  SmartTabbedPane.this.tabbedPane.getComponentAt(index).requestFocusInWindow();
               }
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            if (e.isMetaDown()) {
               if (SmartTabbedPane.this.popupMenuFactory != null) {
                  int currentTabIndex = SmartTabbedPane.this.getTabIndexByTitleComponent(tabTitleComponent);
                  JPopupMenu popupMenu = SmartTabbedPane.this.popupMenuFactory.createPopupMenu(currentTabIndex);
                  if (popupMenu != null) {
                     SmartTabbedPane.this.tabbedPane.setHasPopupMenuVisible(true);

                     try {
                        popupMenu.show(tabTitleComponent, e.getX(), e.getY());
                     } finally {
                        SmartTabbedPane.this.tabbedPane.setHasPopupMenuVisible(true);
                     }
                  }
               }
            }
         }
      });
   }

   private int getTabIndexByTitleComponent(TabTitleComponent component) {
      for (int index = 0; index < this.tabbedPane.getTabCount(); index++) {
         if (this.tabbedPane.getTabComponentAt(index) == component) {
            return index;
         }
      }

      return -1;
   }

   public void setTitleAt(int tabIndex, String title) {
      TabTitleComponent titleComponent = (TabTitleComponent)this.tabbedPane.getTabComponentAt(tabIndex);
      titleComponent.setText(title);
   }

   public void setIconAt(int tabIndex, Icon icon) {
      TabTitleComponent titleComponent = (TabTitleComponent)this.tabbedPane.getTabComponentAt(tabIndex);
      titleComponent.setIcon(icon);
   }

   public void setSelectedTabIndex(int tabIndex) {
      this.tabbedPane.setSelectedIndex(tabIndex);
   }

   public int getSelectedTabIndex() {
      return this.tabbedPane.getSelectedIndex();
   }

   public void removeTab(int tabIndex) {
      this.tabbedPane.removeTabAt(tabIndex);
   }

   public void setPopupMenuFactory(ISmartTabbedPanePopupMenuFactory popupMenuFactory) {
      this.popupMenuFactory = popupMenuFactory;
   }

   public void setSelectedComponent(JComponent component) {
      this.tabbedPane.setSelectedComponent(component);
   }

   public Component getSelectedComponent() {
      return this.tabbedPane.getSelectedComponent();
   }
}
