package de.jave.jave.rectangle;

import de.jave.gui.CharField;
import de.jave.gui.CharacterModel;
import de.jave.gui.layout.Gap;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.RectangleAlgorithm;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.plate.MouseCharacterPanel;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class RectangleStylePanel implements ItemListener, ItemSelectable {
   private final JComboBox chMode;
   private final CharField[] charFields;
   private final CharacterModel[] charModels;
   private final MouseCharacterModel mouseCharacterModel;
   private transient ItemListener itemListener;
   private static final RectangleStylePanel.RectangleStyleItem USER_DEFINED = new RectangleStylePanel.RectangleStyleItem("User Defined", null);
   private final JComponent content;
   private final MouseCharacterPanel mouseCharacterPanel;
   private boolean applyingChars;

   public RectangleStylePanel(MouseCharacterModel mouseCharacterModel) {
      this.mouseCharacterModel = mouseCharacterModel;
      this.mouseCharacterPanel = new MouseCharacterPanel(mouseCharacterModel);
      RectangleStyle[] allStyles = RectangleStyle.values();
      RectangleStylePanel.RectangleStyleItem[] items = new RectangleStylePanel.RectangleStyleItem[allStyles.length + 1];

      for (int i = 0; i < allStyles.length; i++) {
         RectangleStyle value = allStyles[i];
         items[i] = new RectangleStylePanel.RectangleStyleItem(new RectangleStyleObjectUi().getLabel(value), value);
      }

      items[items.length - 1] = USER_DEFINED;
      this.chMode = new JComboBox<>(items);
      this.chMode.setSelectedIndex(0);
      this.chMode.addItemListener(this);
      JPanel p1 = new JPanel(new GridDialogLayout(1, false));
      p1.add(new JLabel("Style:"));
      p1.add(this.chMode, GridDialogLayoutData.FILL_HORIZONTAL);
      char[] chars = RectangleAlgorithm.getCharsForStyle(((RectangleStylePanel.RectangleStyleItem)this.chMode.getSelectedItem()).getStyle());
      this.charModels = new CharacterModel[8];
      this.charFields = new CharField[8];
      IChangeListener changeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            if (!RectangleStylePanel.this.applyingChars) {
               RectangleStylePanel.this.charFieldEdited();
            }
         }
      };

      for (int i = 0; i < 8; i++) {
         this.charModels[i] = new CharacterModel(chars[i]);
         this.charFields[i] = new CharField(this.charModels[i]);
         this.charFields[i].setFont(JaveGlobalRessources.FONT_DEFAULT);
         this.charModels[i].addChangeListener(changeListener);
      }

      JPanel p2 = new JPanel(new GridDialogLayout(3, false, 0, 0));
      p2.add(this.charFields[0]);
      p2.add(this.charFields[1], GridDialogLayoutData.FILL_HORIZONTAL);
      p2.add(this.charFields[2]);
      p2.add(this.charFields[3], GridDialogLayoutData.FILL_VERTICAL);
      p2.add(new Gap(55, 5), GridDialogLayoutData.FILL_BOTH);
      p2.add(this.charFields[4], GridDialogLayoutData.FILL_VERTICAL);
      p2.add(this.charFields[5]);
      p2.add(this.charFields[6], GridDialogLayoutData.FILL_HORIZONTAL);
      p2.add(this.charFields[7]);
      JPanel panel = new JPanel(new BorderLayout(2, 3));
      panel.add(p1, "North");
      panel.add(p2, "Center");
      panel.add(this.mouseCharacterPanel.getContent(), "South");
      this.updateMouseCharacterPanelEnabled();
      mouseCharacterModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (RectangleStylePanel.this.getStyle() == RectangleStyle.CHARACTERS) {
               RectangleStylePanel.this.initializeTextFieldsFromMouseCharacter();
               RectangleStylePanel.this.fireItemStateChanged();
            }
         }
      });
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }

   @Override
   public synchronized void addItemListener(ItemListener l) {
      this.itemListener = AWTEventMulticaster.add(this.itemListener, l);
   }

   @Override
   public synchronized void removeItemListener(ItemListener l) {
      this.itemListener = AWTEventMulticaster.remove(this.itemListener, l);
   }

   public boolean isUnderLineStyle() {
      return this.charModels[1].getCharacter() == '_';
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      if (evt == null || evt.getStateChange() == ItemEvent.SELECTED) {
         RectangleStyle style = this.getStyle();
         if (style == RectangleStyle.CHARACTERS) {
            this.initializeTextFieldsFromMouseCharacter();
         } else {
            this.applyChars(RectangleAlgorithm.getCharsForStyle(style), true);
         }

         this.updateMouseCharacterPanelEnabled();
         this.fireItemStateChanged();
      }
   }

   private void initializeTextFieldsFromMouseCharacter() {
      char[] ch = new char[8];
      Arrays.fill(ch, this.mouseCharacterModel.getCharacter1());
      this.applyChars(ch, false);
   }

   /**
    * Writes the given characters into the character fields without letting the change listener of the
    * fields interpret them as a manual edit, which would switch the style back to "User Defined".
    */
   private void applyChars(char[] ch, boolean editable) {
      this.applyingChars = true;

      try {
         for (int i = 0; i < 8; i++) {
            this.charModels[i].setCharacter(ch[i]);
            this.charFields[i].setEditable(editable);
         }
      } finally {
         this.applyingChars = false;
      }
   }

   private void charFieldEdited() {
      RectangleAlgorithm.setUserDefinedChars(this.getCurrentChars());
      if (this.chMode.getSelectedItem() == USER_DEFINED) {
         this.fireItemStateChanged();
      } else {
         this.chMode.setSelectedItem(USER_DEFINED);
      }
   }

   private void fireItemStateChanged() {
      if (this.itemListener != null) {
         this.itemListener.itemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, null, ItemEvent.SELECTED));
      }
   }

   private void updateMouseCharacterPanelEnabled() {
      this.mouseCharacterPanel.setEnabled(this.getStyle() == RectangleStyle.CHARACTERS);
   }

   public void setStyle(RectangleStyle style) {
      for (int i = 0; i < this.chMode.getItemCount(); i++) {
         if (((RectangleStylePanel.RectangleStyleItem)this.chMode.getItemAt(i)).getStyle() == style) {
            if (this.chMode.getSelectedIndex() == i) {
               this.itemStateChanged(null);
            } else {
               this.chMode.setSelectedIndex(i);
            }

            return;
         }
      }
   }

   /** Returns the selected style, or <code>null</code> if the characters are user defined. */
   public RectangleStyle getStyle() {
      return ((RectangleStylePanel.RectangleStyleItem)this.chMode.getSelectedItem()).getStyle();
   }

   public char[] getCurrentChars() {
      char[] ch = new char[8];

      for (int i = 0; i < 8; i++) {
         ch[i] = this.charModels[i].getCharacter();
      }

      return ch;
   }

   @Override
   public Object[] getSelectedObjects() {
      return new Object[]{this.getCurrentChars()};
   }

   private static final class RectangleStyleItem {
      private final RectangleStyle style;
      private final String printName;

      public RectangleStyleItem(String printName, RectangleStyle style) {
         Ensure.ensureArgumentNotNull(printName);
         this.printName = printName;
         this.style = style;
      }

      public String getPrintName() {
         return this.printName;
      }

      public RectangleStyle getStyle() {
         return this.style;
      }

      @Override
      public String toString() {
         return this.getPrintName();
      }
   }
}
