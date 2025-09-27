package net.disy.commons.swing.events.mouse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class OverallMouseListeningPanel extends JPanel {
   private final List<Component> managedComponents = new ArrayList<>();
   private final List<MouseListener> mouseListeners = new ArrayList<>();
   private final List<MouseMotionListener> mouseMotionListeners = new ArrayList<>();

   public OverallMouseListeningPanel(JComponent content) {
      super(new GridLayout(1, 0));
      super.add(content);
      this.updateManagedComponentsList();
   }

   @Override
   public synchronized void invalidate() {
      this.updateManagedComponentsAndListeners();
      super.invalidate();
   }

   private void updateManagedComponentsAndListeners() {
      this.removeMouseListenersFromManagedComponents();
      this.removeMouseMotionListenersFromManagedComponents();
      this.updateManagedComponentsList();
      this.addMouseListenersToManagedComponents();
      this.addMouseMotionListenersToManagedComponents();
   }

   @Override
   public void validate() {
      this.updateManagedComponentsAndListeners();
      super.validate();
   }

   @Override
   protected void validateTree() {
      this.updateManagedComponentsAndListeners();
      super.validateTree();
   }

   @Override
   public synchronized void addMouseListener(MouseListener listener) {
      this.mouseListeners.add(listener);
      this.addMouseListenerToManagedComponents(listener);
   }

   @Override
   public synchronized void removeMouseListener(MouseListener listener) {
      this.mouseListeners.remove(listener);
      this.removeMouseListenerFromManagedComponents(listener);
   }

   @Override
   public synchronized void addMouseMotionListener(MouseMotionListener listener) {
      this.mouseMotionListeners.add(listener);
      this.addMouseMotionListenerToManagedComponents(listener);
   }

   @Override
   public synchronized void removeMouseMotionListener(MouseMotionListener listener) {
      this.mouseMotionListeners.remove(listener);
      this.removeMouseMotionListenerFromManagedComponents(listener);
   }

   private void addMouseListenerToManagedComponents(MouseListener listener) {
      for (int i = 0; i < this.managedComponents.size(); i++) {
         Component component = this.managedComponents.get(i);
         component.addMouseListener(listener);
      }
   }

   private void removeMouseListenerFromManagedComponents(MouseListener listener) {
      for (int i = 0; i < this.managedComponents.size(); i++) {
         Component component = this.managedComponents.get(i);
         component.removeMouseListener(listener);
      }
   }

   private void addMouseMotionListenerToManagedComponents(MouseMotionListener listener) {
      for (int i = 0; i < this.managedComponents.size(); i++) {
         Component component = this.managedComponents.get(i);
         component.addMouseMotionListener(listener);
      }
   }

   private void removeMouseMotionListenerFromManagedComponents(MouseMotionListener listener) {
      for (int i = 0; i < this.managedComponents.size(); i++) {
         Component component = this.managedComponents.get(i);
         component.removeMouseMotionListener(listener);
      }
   }

   private void addMouseListenersToManagedComponents() {
      for (int i = 0; i < this.mouseListeners.size(); i++) {
         MouseListener listener = this.mouseListeners.get(i);
         this.addMouseListenerToManagedComponents(listener);
      }
   }

   private void removeMouseListenersFromManagedComponents() {
      for (int i = 0; i < this.mouseListeners.size(); i++) {
         MouseListener listener = this.mouseListeners.get(i);
         this.removeMouseListenerFromManagedComponents(listener);
      }
   }

   private void addMouseMotionListenersToManagedComponents() {
      for (int i = 0; i < this.mouseMotionListeners.size(); i++) {
         MouseMotionListener listener = this.mouseMotionListeners.get(i);
         this.addMouseMotionListenerToManagedComponents(listener);
      }
   }

   private void removeMouseMotionListenersFromManagedComponents() {
      for (int i = 0; i < this.mouseMotionListeners.size(); i++) {
         MouseMotionListener listener = this.mouseMotionListeners.get(i);
         this.removeMouseMotionListenerFromManagedComponents(listener);
      }
   }

   private void updateManagedComponentsList() {
      this.managedComponents.clear();
      this.collectComponents(this.getComponent(0));
   }

   private void collectComponents(Component component) {
      this.managedComponents.add(component);
      if (component instanceof Container && !(component instanceof OverallMouseListeningPanel)) {
         Container container = (Container)component;
         Component[] components = container.getComponents();

         for (Component component2 : components) {
            this.collectComponents(component2);
         }
      }
   }

   @Override
   public final Component add(Component comp) {
      throw new UnsupportedOperationException("Adding components to an OverallMouseListeningPanel not allowed.");
   }

   @Override
   public final void add(Component comp, Object constraints) {
      throw new UnsupportedOperationException("Adding components to an OverallMouseListeningPanel not allowed.");
   }

   @Override
   public Component add(Component comp, int index) {
      throw new UnsupportedOperationException("Adding components to an OverallMouseListeningPanel not allowed.");
   }

   @Override
   public void add(Component comp, Object constraints, int index) {
      throw new UnsupportedOperationException("Adding components to an OverallMouseListeningPanel not allowed.");
   }
}
