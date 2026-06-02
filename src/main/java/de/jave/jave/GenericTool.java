package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlateModel;
import de.jave.jave.pixelplate.PixelPlateOptionsView;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.listener.IChangeListener;

public abstract class GenericTool extends Tool {
   protected final PixelPlateModel pixelPlateModel;
   private IInlineToolOptions inlineOptions;

   public GenericTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
      this.pixelPlateModel = application.getPixelPlateModel();
      this.getMixCharactersModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            GenericTool.this.setMixMode(GenericTool.this.isMix());
         }
      });
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
   public final IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.inlineOptions = this.buildInlineOptions();
      }
      return this.inlineOptions;
   }

   protected IInlineToolOptions buildInlineOptions() {
      final JComponent content = new PixelPlateOptionsView(this.pixelPlateModel, this.getMixCharactersModel()).getContent();
      return () -> content;
   }
}
