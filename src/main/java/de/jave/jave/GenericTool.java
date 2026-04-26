package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlateModel;
import de.jave.jave.pixelplate.PixelPlateOptionsPanel;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;

public abstract class GenericTool extends Tool {
   protected final PixelPlateModel pixelPlateModel;
   private PixelPlateOptionsPanel pixelPlateOptionsPanel;

   public GenericTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
      this.pixelPlateModel = application.getPixelPlateModel();
   }

   public boolean isFeltpenMode() {
      return this.pixelPlateModel.isFeltpenMode();
   }

   public boolean isLineMode() {
      return this.pixelPlateModel.isLineMode();
   }

   public double getFeltpenPreviewDiameter() {
      return this.pixelPlateModel.getFeltpenPreviewDiameter();
   }

   @Override
   public JComponent getOptionsComponent() {
      if (this.pixelPlateOptionsPanel == null) {
         this.pixelPlateOptionsPanel = new PixelPlateOptionsPanel(this.pixelPlateModel, this.getMixCharactersModel());
         this.getMixCharactersModel().addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               GenericTool.this.setMixMode(GenericTool.this.isMix());
            }
         });
      }

      JComponent ac = this.getAdditionalOptionsComponent();
      if (ac == null) {
         return this.pixelPlateOptionsPanel;
      } else {
         JPanel p = new JPanel(new BorderLayout());
         p.add(this.pixelPlateOptionsPanel, "North");
         p.add(ac, "Center");
         return p;
      }
   }

   public JComponent getAdditionalOptionsComponent() {
      return null;
   }
}
