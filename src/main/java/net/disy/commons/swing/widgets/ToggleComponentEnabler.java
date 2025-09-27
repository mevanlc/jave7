package net.disy.commons.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import net.disy.commons.core.util.Ensure;

public class ToggleComponentEnabler {
   private final JToggleButton button;
   private final IEnableableComponentContainer[] components;

   public static void connect(JToggleButton button, IEnableableComponentContainer... components) {
      connectWithDecorations(button, components, new JComponent[0]);
   }

   public static void connectWithDecoration(JToggleButton button, IEnableableComponentContainer component, JComponent decoration) {
      connectWithDecorations(button, new IEnableableComponentContainer[]{component}, new JComponent[]{decoration});
   }

   public static void connectWithDecorations(JToggleButton button, IEnableableComponentContainer[] components, JComponent[] decorations) {
      new ToggleComponentEnabler(button, components, decorations);
   }

   public static void connect(JToggleButton button, JComponent component) {
      connect(button, new DefaultEnableableComponentContainer(component));
   }

   public static void connectWithDecoration(JToggleButton button, JComponent component, JComponent decoration) {
      connectWithDecoration(button, new DefaultEnableableComponentContainer(component), decoration);
   }

   public static void connect(JToggleButton button, JComponent... components) {
      connect(button, createEnableableComponentContainers(components));
   }

   public static void connectWithDecoration(JToggleButton button, JComponent[] components, JComponent decoration) {
      connectWithDecorations(button, createEnableableComponentContainers(components), new JComponent[]{decoration});
   }

   public static void connectWithDecorations(JToggleButton button, JComponent[] components, JComponent[] decorations) {
      connectWithDecorations(button, createEnableableComponentContainers(components), decorations);
   }

   private static IEnableableComponentContainer[] createEnableableComponentContainers(JComponent[] components) {
      Ensure.ensureArgumentNotNull(components);
      Ensure.ensureArgumentArrayContentsNotNull(components);
      IEnableableComponentContainer[] containers = new IEnableableComponentContainer[components.length];

      for (int i = 0; i < containers.length; i++) {
         containers[i] = new DefaultEnableableComponentContainer(components[i]);
      }

      return containers;
   }

   private ToggleComponentEnabler(JToggleButton button, IEnableableComponentContainer[] components, JComponent[] decorations) {
      Ensure.ensureNotNull(button);
      Ensure.ensureNotNull(components);
      Ensure.ensureArgumentArrayContentsNotNull(components);
      this.button = button;
      this.components = components;
      this.setButtonListeners();
      this.listenToComponentClicks(components);
      this.updateComponentsEnabled();
      this.addDecorationListeners(decorations);
   }

   private void addDecorationListeners(Component[] decorations) {
      for (int i = 0; i < decorations.length; i++) {
         Component currentDecoration = decorations[i];
         currentDecoration.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               ToggleComponentEnabler.this.button.doClick();
            }
         });
         this.addEnabledListener(currentDecoration);
      }
   }

   private void addEnabledListener(final Component component) {
      this.button.addPropertyChangeListener("enabled", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            component.setEnabled(ToggleComponentEnabler.this.button.isEnabled());
         }
      });
      component.setEnabled(this.button.isEnabled());
   }

   private void listenToComponentClicks(IEnableableComponentContainer[] componentsToListenTo) {
      for (int i = 0; i < componentsToListenTo.length; i++) {
         this.listenToComponentClicks(componentsToListenTo[i].getComponents());
      }
   }

   private void listenToComponentClicks(Component[] componentsToListenTo) {
      for (int i = 0; i < componentsToListenTo.length; i++) {
         Component component = componentsToListenTo[i];
         component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               if (ToggleComponentEnabler.this.button.isEnabled() && !ToggleComponentEnabler.this.button.isSelected()) {
                  ToggleComponentEnabler.this.button.doClick();
                  if (e.getSource() instanceof Component) {
                     ((Component)e.getSource()).requestFocus();
                  }
               }
            }
         });
         if (component instanceof Container) {
            this.listenToComponentClicks(((Container)component).getComponents());
         }
      }
   }

   private void setButtonListeners() {
      this.button.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            ToggleComponentEnabler.this.updateComponentsEnabled();
         }
      });
      this.button.addPropertyChangeListener("enabled", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            ToggleComponentEnabler.this.updateComponentsEnabled();
         }
      });
   }

   private void setComponentsEnabled(boolean enabled) {
      for (int i = 0; i < this.components.length; i++) {
         this.components[i].setEnabled(enabled);
      }
   }

   private void updateComponentsEnabled() {
      this.setComponentsEnabled(this.button.isSelected() && this.button.isEnabled());
   }
}
