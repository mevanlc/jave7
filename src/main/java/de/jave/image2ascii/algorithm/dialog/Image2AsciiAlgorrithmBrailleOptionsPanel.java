package de.jave.image2ascii.algorithm.dialog;

import de.jave.braille.table.BrailleTables;
import de.jave.image2ascii.algorithm.AlgorithmBrailleOptionsModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;

public class Image2AsciiAlgorrithmBrailleOptionsPanel implements IDisposableComponentContainer {
   private final AlgorithmBrailleOptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final JComponent content;

   public Image2AsciiAlgorrithmBrailleOptionsPanel(final AlgorithmBrailleOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(optionsModel);
      this.optionsModel = optionsModel;
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      final JComboBox chMode = new JComboBox<>(BrailleTables.getAvailableTableNames());
      chMode.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setBrailleTableName((String)chMode.getSelectedItem());
         }
      });
      chMode.setSelectedItem(optionsModel.getBrailleTableName());
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            chMode.setSelectedItem(optionsModel.getBrailleTableName());
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      panel.add(new JLabel("Braille Table:"));
      panel.add(chMode);
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void dispose() {
      this.optionsModel.removeChangeListener(this.optionsModelChangeListener);
   }
}
