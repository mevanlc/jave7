package de.jave.image2ascii.dialog;

import de.jave.gui.GSliderArrangement;
import de.jave.image2ascii.model.Image2AsciiOutputOptionsModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;

public class Image2AsciiOutputOptionsPanel {
   private static final int SHAPE_MIN = 50;
   private static final int SHAPE_MAX = 150;
   private static final int SHAPE_STEP = 5;
   private static final int SHAPE_DIVIDE = 100;
   private final JComponent content;

   public Image2AsciiOutputOptionsPanel(final Image2AsciiOutputOptionsModel model) {
      final SpinnerNumberModel ifWidth = new SpinnerNumberModel(model.getOutputWidth(), 1, 5000, 1);
      ifWidth.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model.setOutputWidth(ifWidth.getNumber().intValue());
         }
      });
      final JSpinner widthWidget = new JSpinner(ifWidth);
      final GSliderArrangement slaShape = new GSliderArrangement("Shape factor:", 50, 150, (int)(model.getShapeFactor() * 100.0), 5, 100);
      slaShape.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model.setShapeFactor(slaShape.getDValue());
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            slaShape.setValue((int)(model.getShapeFactor() * 100.0));
         }
      });
      GridDialogPanelBuilder dialogPanel = new GridDialogPanelBuilder();
      dialogPanel.add(new IDialogComponent() {
         @Override
         public int getColumnCount() {
            return 2;
         }

         @Override
         public void fillInto(JPanel panel, int columnCount) {
            panel.add(new JLabel("Text width:"), GridDialogLayoutData.RIGHT);
            panel.add(widthWidget);
         }
      });
      dialogPanel.add(slaShape);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(dialogPanel.createPanel());
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }
}
