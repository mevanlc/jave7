package de.jave.image2ascii.algorithm.dialog;

import de.jave.image2ascii.algorithm.AlgorithmFeltpenOptionsModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgotihmFeltpenOptionsPanel implements IDisposableComponentContainer {
   private final AlgorithmFeltpenOptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final JComponent content;

   public Image2AsciiAlgotihmFeltpenOptionsPanel(final AlgorithmFeltpenOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(optionsModel);
      this.optionsModel = optionsModel;
      final JCheckBox cbMedianCut = new JCheckBox("Median cut color reduction", optionsModel.isMedianCut());
      cbMedianCut.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setMedianCut(cbMedianCut.isSelected());
         }
      });
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            cbMedianCut.setSelected(optionsModel.isMedianCut());
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      this.content = cbMedianCut;
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
