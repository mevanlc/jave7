package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlateOptionsPanel;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;

public abstract class GenericTool extends Tool {
   protected static PixelPlateOptionsPanel pixelPlateOptionsPanel;

   public GenericTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
   }

   public boolean isFeltpenMode() {
      return pixelPlateOptionsPanel.isFeltpenMode();
   }

   public boolean isLineMode() {
      return pixelPlateOptionsPanel.isLineMode();
   }

   public double getFeltpenPreviewDiameter() {
      return pixelPlateOptionsPanel.getFeltpenPreviewDiameter();
   }

   @Override
   public JComponent getOptionsComponent() {
      if (pixelPlateOptionsPanel == null) {
         pixelPlateOptionsPanel = new PixelPlateOptionsPanel(this.getMixCharactersModel());
         this.getMixCharactersModel().addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               GenericTool.this.setMixMode(GenericTool.this.isMix());
            }
         });
      }

      JComponent ac = this.getAdditionalOptionsComponent();
      if (ac == null) {
         return pixelPlateOptionsPanel;
      } else {
         JPanel p = new JPanel(new BorderLayout());
         p.add(pixelPlateOptionsPanel, "North");
         p.add(ac, "Center");
         return p;
      }
   }

   public JComponent getAdditionalOptionsComponent() {
      return null;
   }
}
