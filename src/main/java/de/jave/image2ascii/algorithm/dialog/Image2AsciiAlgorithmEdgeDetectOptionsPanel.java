package de.jave.image2ascii.algorithm.dialog;

import de.jave.image2ascii.algorithm.AlgorithmEdgeDetectOptionsModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmEdgeDetectOptionsPanel implements IDisposableComponentContainer {
   private final AlgorithmEdgeDetectOptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final JComponent content;

   public Image2AsciiAlgorithmEdgeDetectOptionsPanel(final AlgorithmEdgeDetectOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(optionsModel);
      this.optionsModel = optionsModel;
      final JCheckBox cbHires = new JCheckBox("High resolution", optionsModel.isHires());
      cbHires.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setHires(cbHires.isSelected());
         }
      });
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            cbHires.setSelected(optionsModel.isHires());
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      this.content = cbHires;
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
