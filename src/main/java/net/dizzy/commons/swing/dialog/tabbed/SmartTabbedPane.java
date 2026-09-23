package net.dizzy.commons.swing.dialog.tabbed;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.dizzy.commons.core.model.listener.IChangeListener;

public class SmartTabbedPane {
   private final JPanel content = new JPanel(new BorderLayout());
   private final JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
   private final JPanel cardPanel = new JPanel(new CardLayout());
   private final List<Tab> tabs = new ArrayList<>();
   private final ButtonGroup buttonGroup = new ButtonGroup();
   private final List<IChangeListener> selectionChangeListeners = new ArrayList<>();
   private final ISmartTabbedPaneCloseHandler closeHandler;
   private int selectedIndex = -1;
   private int nextCardId = 0;

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
      this.buttonGroup.add(tab.button);
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
      this.buttonGroup.remove(tab.button);
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
         if (index >= 0 && index < this.tabs.size()) {
            this.tabs.get(index).button.setSelected(true);
         }
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
      if (this.selectedIndex >= 0 && this.selectedIndex < this.tabs.size()) {
         this.tabs.get(this.selectedIndex).button.setSelected(true);
         ((CardLayout)this.cardPanel.getLayout()).show(this.cardPanel, this.tabs.get(this.selectedIndex).cardName);
      } else {
         this.buttonGroup.clearSelection();
      }
      for (Tab tab : this.tabs) {
         tab.button.repaint();
      }
   }

   private void fireSelectionChanged() {
      for (IChangeListener listener : this.selectionChangeListeners) {
         listener.stateChanged();
      }
   }

   public static final class TabButton extends JToggleButton {
      public TabButton(String title, Icon icon) {
         super(title, icon);
         setFocusable(false);
         setContentAreaFilled(false);
         setBorderPainted(false);
         setFocusPainted(false);
         setOpaque(false);
         setRolloverEnabled(true);
         setMargin(new Insets(2, 8, 2, 8));
      }

      @Override
      protected void paintComponent(Graphics graphics) {
         Graphics2D g2 = (Graphics2D) graphics.create();
         try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();

            Color bg;
            Color border;
            if (isSelected()) {
               bg = getActiveBackground();
               border = getActiveBorderColor(bg);
            } else if (getModel().isRollover()) {
               bg = getHoverBackground();
               border = getInactiveBorderColor(bg);
            } else {
               bg = getInactiveBackground();
               border = getInactiveBorderColor(bg);
            }

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, width - 1, height - 1, 6, 6);

            g2.setColor(border);
            g2.drawRoundRect(0, 0, width - 1, height - 1, 6, 6);

            setForeground(getTextColor(bg));
         } finally {
            g2.dispose();
         }
         super.paintComponent(graphics);
      }

      public static Color getActiveBackground() {
         Color panelBg = UIManager.getColor("Panel.background");
         if (panelBg == null) {
            return Color.WHITE;
         }
         double brightness = (0.299 * panelBg.getRed() + 0.587 * panelBg.getGreen() + 0.114 * panelBg.getBlue()) / 255.0;
         if (brightness < 0.5) {
            return new Color(
               Math.min(255, panelBg.getRed() + 35),
               Math.min(255, panelBg.getGreen() + 35),
               Math.min(255, panelBg.getBlue() + 35)
            );
         }
         return Color.WHITE;
      }

      public static Color getInactiveBackground() {
         Color panelBg = UIManager.getColor("Panel.background");
         if (panelBg == null) {
            return new Color(214, 214, 214);
         }
         double brightness = (0.299 * panelBg.getRed() + 0.587 * panelBg.getGreen() + 0.114 * panelBg.getBlue()) / 255.0;
         if (brightness < 0.5) {
            return new Color(
               Math.max(0, panelBg.getRed() - 25),
               Math.max(0, panelBg.getGreen() - 25),
               Math.max(0, panelBg.getBlue() - 25)
            );
         }
         return new Color(
            Math.max(0, panelBg.getRed() - 24),
            Math.max(0, panelBg.getGreen() - 24),
            Math.max(0, panelBg.getBlue() - 24)
         );
      }

      public static Color getHoverBackground() {
         Color panelBg = UIManager.getColor("Panel.background");
         if (panelBg == null) {
            return new Color(228, 228, 228);
         }
         double brightness = (0.299 * panelBg.getRed() + 0.587 * panelBg.getGreen() + 0.114 * panelBg.getBlue()) / 255.0;
         if (brightness < 0.5) {
            return new Color(
               Math.min(255, panelBg.getRed() + 15),
               Math.min(255, panelBg.getGreen() + 15),
               Math.min(255, panelBg.getBlue() + 15)
            );
         }
         return new Color(
            Math.max(0, panelBg.getRed() - 10),
            Math.max(0, panelBg.getGreen() - 10),
            Math.max(0, panelBg.getBlue() - 10)
         );
      }

      public static Color getActiveBorderColor(Color bg) {
         double brightness = (0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue()) / 255.0;
         return brightness > 0.5 ? new Color(150, 150, 150) : new Color(120, 120, 120);
      }

      public static Color getInactiveBorderColor(Color bg) {
         double brightness = (0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue()) / 255.0;
         return brightness > 0.5 ? new Color(192, 192, 192) : new Color(60, 60, 60);
      }

      public static Color getTextColor(Color bg) {
         double brightness = (0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue()) / 255.0;
         return brightness > 0.5 ? new Color(30, 30, 30) : Color.WHITE;
      }
   }

   private final class Tab {
      private final Component component;
      private final String cardName;
      private final JToggleButton button;

      private Tab(String title, Icon icon, Component component) {
         this.component = component;
         this.cardName = "tab-" + (SmartTabbedPane.this.nextCardId++);
         this.button = new TabButton(title, icon);
         this.button.addActionListener(event -> SmartTabbedPane.this.setSelectedTabIndex(SmartTabbedPane.this.tabs.indexOf(this)));
         this.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
               if (SwingUtilities.isMiddleMouseButton(event)) {
                  int index = SmartTabbedPane.this.tabs.indexOf(Tab.this);
                  if (index >= 0 && SmartTabbedPane.this.closeHandler != null) {
                     SmartTabbedPane.this.closeHandler.handleTabClosing(SmartTabbedPane.this, index);
                  }
               }
            }
         });
      }
   }
}

