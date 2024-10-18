package net.disy.commons.swing.dialog.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.component.AbstractActionComponent;
import net.disy.commons.swing.component.IComponentContainer;

public abstract class AbstractColorChoosingComponent extends AbstractActionComponent implements IComponentContainer {
   private static final Font FONT = new Font("Dialog", 0, 10);
   private final ColorModel model;
   private final IColorChooserConfiguration configuration;
   private final JComponent content;
   private boolean editable = true;

   public AbstractColorChoosingComponent(ColorModel model, IColorChooserConfiguration configuration) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(configuration);
      this.model = model;
      this.configuration = configuration;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AbstractColorChoosingComponent.this.updateView();
         }
      });
      this.content = this.createContent();
      this.updateView();
   }

   protected abstract JComponent createContent();

   public AbstractColorChoosingComponent(ColorModel model) {
      this(model, new DefaultColorChooserConfiguration());
   }

   public void setEditable(boolean editable) {
      this.editable = editable;
   }

   protected final void performColorChooseDialog() {
      if (this.editable) {
         Color newColor = ColorChooserDialog.showDialog(this.getContent(), this.configuration, this.model.getColor());
         if (newColor != null) {
            this.model.setColor(newColor);
         }

         this.fireActionEvent();
      }
   }

   public ColorModel getModel() {
      return this.model;
   }

   protected final void paintColorRectangle(Graphics g, int x, int y, Dimension size, boolean enabled) {
      this.paintColorRectangle(this.model.getColor(), g, x, y, size, enabled);
   }

   private final void paintColorRectangle(Color rectColor, Graphics g, int x, int y, Dimension size, boolean enabled) {
      int h = size.height;
      int w = size.width;
      Color oldColor = g.getColor();
      g.setFont(FONT);
      g.setColor(Color.white);
      g.fillRect(x + 1, y + 1, w - 2, h - 2);
      if (this.configuration.isTransparencyEnabled()) {
         g.setColor(SwingColors.getTextAreaForegroundColor());
         int alphaPercentage = 100 - (int)Math.round((double)this.getModel().getColor().getAlpha() / 2.55);
         g.drawString(alphaPercentage + " %", x + w / 2 - 10, y + h / 2 + 4);
      }

      if (enabled) {
         g.setColor(rectColor);
      } else {
         g.setColor(SwingColors.getControlColor());
      }

      g.fillRect(x + 1, y + 1, w - 2, h - 2);
      if (enabled) {
         g.setColor(Color.black);
      } else {
         g.setColor(SwingColors.getControlShadowColor());
      }

      g.drawRect(x, y, w - 1, h - 1);
      g.setColor(oldColor);
   }

   protected void updateView() {
      this.content.repaint();
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   public final void setEnabled(boolean enabled) {
      this.content.setEnabled(enabled);
   }
}
