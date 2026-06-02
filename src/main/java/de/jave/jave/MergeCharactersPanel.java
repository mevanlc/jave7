package de.jave.jave;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class MergeCharactersPanel {
   private final JCheckBox checkBox;

   public MergeCharactersPanel(final BooleanModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.checkBox = new JCheckBox(JaveMessages.ToolCheckBox_MergeCharacters_Label, model.getValue());
      this.checkBox.setToolTipText(JaveMessages.ToolCheckBox_MergeCharacters_Tooltip);
      this.checkBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setValue(MergeCharactersPanel.this.checkBox.isSelected());
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            MergeCharactersPanel.this.checkBox.setSelected(model.getValue());
         }
      });
   }

   public JComponent getContent() {
      return this.checkBox;
   }

   public void setEnabled(boolean b) {
      this.checkBox.setEnabled(b);
   }
}
