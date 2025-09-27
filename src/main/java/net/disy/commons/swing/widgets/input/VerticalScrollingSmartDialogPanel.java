package net.disy.commons.swing.dialog.input;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.message.HighestPriorityMessageBuilder;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.panel.FixedIncrementVerticalScrollablePanel;

public class VerticalScrollingSmartDialogPanel extends AbstractSmartDialogPanel {
   private final ISmartDialogPanel[] panels;
   private final int preferredViewportHeight;
   private final IOptionalCurrentMessageFactory[] optionalCurrentMessageFactories;

   public VerticalScrollingSmartDialogPanel(ISmartDialogPanel[] panels) {
      this(panels, new IOptionalCurrentMessageFactory[0], LayoutUtilities.getDpiAdjusted(200));
   }

   public VerticalScrollingSmartDialogPanel(ISmartDialogPanel[] panels, IOptionalCurrentMessageFactory[] additionalOptionalCurrentMessageFactories) {
      this(
         panels,
         ArrayUtilities.concat(IOptionalCurrentMessageFactory.class, panels, additionalOptionalCurrentMessageFactories),
         LayoutUtilities.getDpiAdjusted(200)
      );
   }

   private VerticalScrollingSmartDialogPanel(
      ISmartDialogPanel[] panels, IOptionalCurrentMessageFactory[] optionalCurrentMessageFactories, int preferredViewportHeight
   ) {
      Ensure.ensureArgumentNotNull(panels);
      this.panels = panels;
      this.optionalCurrentMessageFactories = optionalCurrentMessageFactories;
      this.preferredViewportHeight = preferredViewportHeight;
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      for (ISmartDialogPanel panel : this.panels) {
         panel.addChangeListener(listener);
      }
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      HighestPriorityMessageBuilder messageBuilder = new HighestPriorityMessageBuilder();

      for (IOptionalCurrentMessageFactory panel : this.optionalCurrentMessageFactories) {
         messageBuilder.addMessage(panel.createOptionalCurrentMessage());
      }

      return messageBuilder.getHighestPriorityMessage();
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      FixedIncrementVerticalScrollablePanel container = new FixedIncrementVerticalScrollablePanel(new BorderLayout(), this.preferredViewportHeight);
      GridDialogPanelBuilder dialogPanel = new GridDialogPanelBuilder();

      for (ISmartDialogPanel smartDialogPanel : this.panels) {
         this.addDialogPanel(smartDialogPanel, dialogPanel);
      }

      container.add(dialogPanel.createPanel(), "Center");
      container.setBorder(new EmptyBorder(0, 0, 0, LayoutUtilities.getDpiAdjusted(6)));
      JScrollPane scrollPane = new JScrollPane(container);
      scrollPane.setBorder(null);
      panel.add(scrollPane, GridDialogLayoutData.FILL_BOTH);
   }

   private void addDialogPanel(ISmartDialogPanel dialogPanel, GridDialogPanelBuilder panel) {
      panel.add(dialogPanel);
   }

   @Override
   public void requestFocus() {
      if (this.panels.length > 0) {
         this.panels[0].requestFocus();
      }
   }
}
