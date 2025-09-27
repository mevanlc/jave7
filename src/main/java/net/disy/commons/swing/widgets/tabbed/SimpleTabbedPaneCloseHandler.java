package net.disy.commons.swing.dialog.tabbed;

public class SimpleTabbedPaneCloseHandler implements ISmartTabbedPaneCloseHandler {
   @Override
   public void handleTabClosing(SmartTabbedPane tabbedPane, int tabIndex) {
      tabbedPane.removeTab(tabIndex);
   }
}
