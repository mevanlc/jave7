package net.dizzy.commons.swing.dialog.tabbed;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import net.dizzy.commons.core.model.listener.IChangeListener;

public class SmartTabbedPane {
   private final JTabbedPane tabbedPane = new JTabbedPane();
   private final ISmartTabbedPaneCloseHandler closeHandler;

   public SmartTabbedPane(ISmartTabbedPaneCloseHandler closeHandler) {
      this.closeHandler = closeHandler;
      this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
   }

   public void addTab(String title, Component component) { tabbedPane.addTab(title, component); }
   public void addTab(String title, javax.swing.Icon icon, Component component) { tabbedPane.addTab(title, icon, component); }
   public void removeTab(int index) { tabbedPane.removeTabAt(index); }
   public void setTitleAt(int index, String title) { tabbedPane.setTitleAt(index, title); }
   public int getSelectedTabIndex() { return tabbedPane.getSelectedIndex(); }
   public void setSelectedTabIndex(int index) { tabbedPane.setSelectedIndex(index); }
   public JComponent getContent() { return tabbedPane; }
   public void addTabSelectionChangeListener(IChangeListener listener) { tabbedPane.addChangeListener(event -> listener.stateChanged()); }
   public ISmartTabbedPaneCloseHandler getCloseHandler() { return closeHandler; }
}
