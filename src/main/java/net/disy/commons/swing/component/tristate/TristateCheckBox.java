package net.disy.commons.swing.component.tristate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

public class TristateCheckBox extends JCheckBox {
   private final TristateCheckBox.TristateDecorator model;

   public TristateCheckBox(String text, Icon icon, TriState initial, boolean notCareState) {
      super(text, icon);
      super.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            TristateCheckBox.this.grabFocus();
            TristateCheckBox.this.model.nextState();
         }
      });
      ActionMap map = new ActionMapUIResource();
      map.put("pressed", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            TristateCheckBox.this.grabFocus();
            TristateCheckBox.this.model.nextState();
         }
      });
      map.put("released", null);
      SwingUtilities.replaceUIActionMap(this, map);
      this.model = new TristateCheckBox.TristateDecorator(this.getModel(), notCareState);
      this.setModel(this.model);
      this.setState(initial);
   }

   public TristateCheckBox(String text, TriState initial, boolean notCareState) {
      this(text, null, initial, notCareState);
   }

   public TristateCheckBox(String text, boolean notCareState) {
      this(text, TriState.DONT_CARE, notCareState);
   }

   public TristateCheckBox(boolean notCareState) {
      this(null, notCareState);
   }

   @Override
   public void addMouseListener(MouseListener l) {
   }

   public void setState(TriState state) {
      this.model.setState(state);
   }

   public TriState getState() {
      return this.model.getState();
   }

   @Override
   public void setSelected(boolean b) {
      if (b) {
         this.setState(TriState.SELECTED);
      } else {
         this.setState(TriState.NOT_SELECTED);
      }
   }

   private class TristateDecorator implements ButtonModel {
      private final ButtonModel other;
      private final boolean notCareSelection;

      private TristateDecorator(ButtonModel other, boolean notCareSelection) {
         this.other = other;
         this.notCareSelection = notCareSelection;
      }

      private void setState(TriState state) {
         if (state == TriState.NOT_SELECTED) {
            this.other.setArmed(false);
            this.setPressed(false);
            this.setSelected(false);
         } else if (state == TriState.SELECTED) {
            this.other.setArmed(false);
            this.setPressed(false);
            this.setSelected(true);
         } else {
            this.other.setArmed(true);
            this.setPressed(true);
            this.setSelected(this.notCareSelection);
         }
      }

      private TriState getState() {
         if (this.isArmed()) {
            return TriState.DONT_CARE;
         } else {
            return this.isSelected() && !this.isArmed() ? TriState.SELECTED : TriState.NOT_SELECTED;
         }
      }

      private void nextState() {
         TriState current = this.getState();
         if (current == TriState.NOT_SELECTED) {
            this.setState(TriState.SELECTED);
         } else if (current == TriState.SELECTED) {
            this.setState(TriState.DONT_CARE);
         } else if (current == TriState.DONT_CARE) {
            this.setState(TriState.NOT_SELECTED);
         }
      }

      @Override
      public void setArmed(boolean b) {
      }

      @Override
      public void setEnabled(boolean b) {
         TristateCheckBox.this.setFocusable(b);
         this.other.setEnabled(b);
      }

      @Override
      public boolean isArmed() {
         return this.other.isArmed();
      }

      @Override
      public boolean isSelected() {
         return this.other.isSelected();
      }

      @Override
      public boolean isEnabled() {
         return this.other.isEnabled();
      }

      @Override
      public boolean isPressed() {
         return this.other.isPressed();
      }

      @Override
      public boolean isRollover() {
         return this.other.isRollover();
      }

      @Override
      public void setSelected(boolean b) {
         this.other.setSelected(b);
      }

      @Override
      public void setPressed(boolean b) {
         this.other.setPressed(b);
      }

      @Override
      public void setRollover(boolean b) {
         this.other.setRollover(b);
      }

      @Override
      public void setMnemonic(int key) {
         this.other.setMnemonic(key);
      }

      @Override
      public int getMnemonic() {
         return this.other.getMnemonic();
      }

      @Override
      public void setActionCommand(String s) {
         this.other.setActionCommand(s);
      }

      @Override
      public String getActionCommand() {
         return this.other.getActionCommand();
      }

      @Override
      public void setGroup(ButtonGroup group) {
         this.other.setGroup(group);
      }

      @Override
      public void addActionListener(ActionListener l) {
         this.other.addActionListener(l);
      }

      @Override
      public void removeActionListener(ActionListener l) {
         this.other.removeActionListener(l);
      }

      @Override
      public void addItemListener(ItemListener l) {
         this.other.addItemListener(l);
      }

      @Override
      public void removeItemListener(ItemListener l) {
         this.other.removeItemListener(l);
      }

      @Override
      public void addChangeListener(ChangeListener l) {
         this.other.addChangeListener(l);
      }

      @Override
      public void removeChangeListener(ChangeListener l) {
         this.other.removeChangeListener(l);
      }

      @Override
      public Object[] getSelectedObjects() {
         return this.other.getSelectedObjects();
      }
   }
}
