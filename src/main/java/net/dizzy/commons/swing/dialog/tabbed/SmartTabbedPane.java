package net.dizzy.commons.swing.dialog.tabbed;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.dizzy.commons.core.model.listener.IChangeListener;

public class SmartTabbedPane {
   private final JPanel content = new JPanel(new BorderLayout());
   private final JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
   private final JPanel cardPanel = new JPanel(new CardLayout());
   private final List<Tab> tabs = new ArrayList<>();
   private final List<IChangeListener> selectionChangeListeners = new ArrayList<>();
   private final ISmartTabbedPaneCloseHandler closeHandler;
   private int selectedIndex = -1;

   public SmartTabbedPane(ISmartTabbedPaneCloseHandler closeHandler) {
      this.closeHandler = closeHandler;
      this.content.add(this.tabBar, BorderLayout.NORTH);
      this.content.add(this.cardPanel, BorderLayout.CENTER);
   }

   public void addTab(String title, Component component) {
      addTab(title, null, component);
   }

   public void addTab(String title, Icon icon, Component component) {
      Tab tab = new Tab(title, icon, component);
      this.tabs.add(tab);
      this.tabBar.add(tab.button);
      this.cardPanel.add(component, tab.cardName);
      if (this.selectedIndex < 0) {
         setSelectedTabIndex(0);
      }
      this.content.revalidate();
      this.content.repaint();
   }

   public void removeTab(int index) {
      int previousSelectedIndex = this.selectedIndex;
      boolean selectedTabRemoved = index == this.selectedIndex;
      Tab tab = this.tabs.remove(index);
      this.tabBar.remove(tab.button);
      this.cardPanel.remove(tab.component);
      if (this.tabs.isEmpty()) {
         this.selectedIndex = -1;
      } else if (index < this.selectedIndex) {
         this.selectedIndex--;
      } else if (this.selectedIndex >= this.tabs.size()) {
         this.selectedIndex = this.tabs.size() - 1;
      }
      showSelectedTab();
      if (selectedTabRemoved || this.selectedIndex != previousSelectedIndex) {
         fireSelectionChanged();
      }
      this.content.revalidate();
      this.content.repaint();
   }

   public void setTitleAt(int index, String title) {
      Tab tab = this.tabs.get(index);
      tab.button.setText(title);
   }

   public int getSelectedTabIndex() {
      return this.selectedIndex;
   }

   public void setSelectedTabIndex(int index) {
      if (index == this.selectedIndex) {
         return;
      }
      this.selectedIndex = index;
      showSelectedTab();
      fireSelectionChanged();
   }

   public JComponent getContent() {
      return this.content;
   }

   public void addTabSelectionChangeListener(IChangeListener listener) {
      this.selectionChangeListeners.add(listener);
   }

   public ISmartTabbedPaneCloseHandler getCloseHandler() { return closeHandler; }

   private void showSelectedTab() {
      for (int i = 0; i < this.tabs.size(); i++) {
         this.tabs.get(i).button.setSelected(i == this.selectedIndex);
      }
      if (this.selectedIndex >= 0) {
         ((CardLayout)this.cardPanel.getLayout()).show(this.cardPanel, this.tabs.get(this.selectedIndex).cardName);
      }
   }

   private void fireSelectionChanged() {
      for (IChangeListener listener : this.selectionChangeListeners) {
         listener.stateChanged();
      }
   }

   private final class Tab {
      private final Component component;
      private final String cardName;
      private final JToggleButton button;

      private Tab(String title, Icon icon, Component component) {
         this.component = component;
         this.cardName = "tab-" + SmartTabbedPane.this.tabs.size();
         this.button = new JToggleButton(title, icon);
         this.button.setFocusable(false);
         this.button.setMargin(new Insets(1, 6, 1, 6));
         this.button.addActionListener(event -> SmartTabbedPane.this.setSelectedTabIndex(SmartTabbedPane.this.tabs.indexOf(this)));
      }
   }
}
