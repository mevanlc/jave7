package de.jave.image2ascii.algorithm.dialog;

import de.jave.image2ascii.algorithm.AlgorithmPixelPlateOptionsModel;
import de.jave.jave.pixelplate.PixelPlateFeltPenMode;
import de.jave.jave.pixelplate.PixelPlateMode;
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
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class Image2AsciiAlgorithmPixelPlateOptionsPanel implements IDisposableComponentContainer {
   private final AlgorithmPixelPlateOptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final JComponent content;

   public Image2AsciiAlgorithmPixelPlateOptionsPanel(final AlgorithmPixelPlateOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(optionsModel);
      this.optionsModel = optionsModel;
      PixelPlateMode[] modes = new PixelPlateMode[]{
         PixelPlateMode.THREE_BY_TWO,
         PixelPlateMode.TWO_BY_TWO,
         PixelPlateMode.PIXEL,
         PixelPlateMode.DOT,
         PixelPlateFeltPenMode.FELTPEN_8,
         PixelPlateMode.THICK_THIN
      };
      final JComboBox chStyle = new JComboBox<>(modes);
      chStyle.setSelectedItem(optionsModel.getPixelPlateMode());
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            chStyle.setSelectedItem(optionsModel.getPixelPlateMode());
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      chStyle.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setPixelPlateMode((PixelPlateMode)chStyle.getSelectedItem());
         }
      });
      chStyle.setRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<PixelPlateMode>() {
         public String getLabel(PixelPlateMode value) {
            if (value == PixelPlateMode.THREE_BY_TWO) {
               return "3by2";
            } else if (value == PixelPlateMode.TWO_BY_TWO) {
               return "2by2";
            } else if (value == PixelPlateMode.PIXEL) {
               return "lines";
            } else if (value == PixelPlateMode.DOT) {
               return "dot";
            } else if (value == PixelPlateFeltPenMode.FELTPEN_8) {
               return "thick 8";
            } else if (value == PixelPlateMode.THICK_THIN) {
               return "thick and thin";
            } else {
               throw new IllegalArgumentException();
            }
         }
      }));
      JPanel p = new JPanel(new GridDialogLayout(2, false));
      p.add(new JLabel("Style:"));
      p.add(chStyle);
      this.content = p;
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
