package de.jave.jave.open;

import de.jave.jave.JaveApplication;
import de.jave.jave.WatermarkTool;
import de.jave.jave.actions.ToolBar;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import net.dizzy.commons.core.util.Ensure;

public class WatermarkImageOpenPerformStrategy implements IImageOpenPerformStrategy {
   private final WatermarkTool tool;
   private final JaveApplication application;

   public WatermarkImageOpenPerformStrategy(WatermarkTool tool, JaveApplication application) {
      Ensure.ensureArgumentNotNull(tool);
      Ensure.ensureArgumentNotNull(application);
      this.tool = tool;
      this.application = application;
   }

   @Override
   public Icon getIcon() {
      return this.tool.getIcon();
   }

   @Override
   public String getName() {
      return "Set as Watermark";
   }

   @Override
   public String getToolTipText() {
      return "Sets the image as background in the editor, so that it can be used for tracing its content with Ascii Art.";
   }

   @Override
   public void perform(Component parentComponent, File file) {
      this.application.setTool(ToolBar.WATERMARK_TOOL_INDEX);
      this.tool.performLoadImage(parentComponent, file);
   }
}
