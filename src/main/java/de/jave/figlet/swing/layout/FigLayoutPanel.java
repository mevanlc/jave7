package de.jave.figlet.swing.layout;

import de.jave.figlet.IFigLayoutListener;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.primitives.FigLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JToolBar;

public class FigLayoutPanel {
   private final Collection<IFigLayoutListener> listeners = new ArrayList<>();
   private final JComponent content;
   private final HorizontalLayoutPanel horizontalLayoutPanel;
   private final VerticalLayoutPanel verticalLayoutPanel;
   private final HorizontalAlignmentPanel horizontalAlignmentPanel;
   private final PrintDirectionPanel printDirectionPanel;

   public FigLayoutPanel() {
      FigLayoutPanel.LayoutSelectionChangeListener listener = new FigLayoutPanel.LayoutSelectionChangeListener(this);
      this.horizontalLayoutPanel = new HorizontalLayoutPanel(listener);
      this.verticalLayoutPanel = new VerticalLayoutPanel(listener);
      this.printDirectionPanel = new PrintDirectionPanel(listener);
      JToolBar panel = new JToolBar("Layout options");
      panel.setFloatable(false);
      this.horizontalAlignmentPanel = new HorizontalAlignmentPanel(listener);
      panel.add(this.horizontalAlignmentPanel.getComponent());
      panel.add(Box.createHorizontalStrut(6));
      panel.add(this.horizontalLayoutPanel.getContent());
      panel.add(Box.createHorizontalStrut(6));
      panel.add(this.verticalLayoutPanel.getContent());
      panel.add(Box.createHorizontalStrut(6));
      panel.add(this.printDirectionPanel.getComponent());
      panel.add(Box.createHorizontalGlue());
      this.content = panel;
   }

   public void setLayout(FigLayout layout) {
      this.horizontalLayoutPanel.setLayout(layout);
      this.verticalLayoutPanel.setLayout(layout);
      this.printDirectionPanel.setSelectedDirection(layout.getPrintDirection());
   }

   public JComponent getContent() {
      return this.content;
   }

   public FigLayout getLayout() {
      FigLayout layout = new FigLayout();
      this.horizontalLayoutPanel.applyTo(layout);
      this.verticalLayoutPanel.applyTo(layout);
      layout.setPrintDirection(this.printDirectionPanel.getSelectedDirection());
      return layout;
   }

   public HorizontalAlignment getAlignment() {
      return this.horizontalAlignmentPanel.getSelectedAlignment();
   }

   public synchronized void addLayoutListener(IFigLayoutListener listener) {
      this.listeners.add(listener);
   }

   public synchronized void removeLayoutListener(IFigLayoutListener listener) {
      this.listeners.remove(listener);
   }

   protected synchronized void fireLayoutChangedEvent() {
      for (IFigLayoutListener listener : new ArrayList<>(this.listeners)) {
         listener.layoutChanged();
      }
   }

   private static class LayoutSelectionChangeListener implements ActionListener {
      private final FigLayoutPanel panel;

      public LayoutSelectionChangeListener(FigLayoutPanel panel) {
         this.panel = panel;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         this.panel.fireLayoutChangedEvent();
      }
   }
}
