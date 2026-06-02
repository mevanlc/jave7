package de.jave.image2ascii.algorithm.dialog;

import de.jave.image2ascii.algorithm.AlgorithmEdgeTracingOptionsModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;

public class Image2AsciiAlgorithmEdgeTracingOptionsPanel implements IDisposableComponentContainer {
   private final AlgorithmEdgeTracingOptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final JComponent content;

   public Image2AsciiAlgorithmEdgeTracingOptionsPanel(final AlgorithmEdgeTracingOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(optionsModel);
      this.optionsModel = optionsModel;
      final JCheckBox cbSmoothing = new JCheckBox("Smooth lines", optionsModel.isSmoothing());
      cbSmoothing.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setSmoothing(cbSmoothing.isSelected());
         }
      });
      final JCheckBox cbFill = new JCheckBox("Fill", optionsModel.isFill());
      cbFill.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setFill(cbFill.isSelected());
         }
      });
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            cbFill.setSelected(optionsModel.isFill());
            cbSmoothing.setSelected(optionsModel.isSmoothing());
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(cbSmoothing);
      panel.add(cbFill);
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
