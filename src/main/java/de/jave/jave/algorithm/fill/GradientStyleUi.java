package de.jave.jave.algorithm.fill;

import de.jave.jave.icon.JaveIcons;
import javax.swing.Icon;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class GradientStyleUi extends AbstractObjectUi<GradientStyle> {
   public Icon getIcon(GradientStyle value) {
      final Icon[] icon = new Icon[1];
      value.accept(new IGradientStyleVisitor() {
         @Override
         public void visitRadial(GradientStyle style) {
            icon[0] = JaveIcons.GRADIENT_RADIAL_ICON;
         }

         @Override
         public void visitSunburst(GradientStyle style) {
            icon[0] = JaveIcons.GRADIENT_SUNBURST_ICON;
         }

         @Override
         public void visitLinear(GradientStyle style) {
            icon[0] = JaveIcons.GRADIENT_LINEAR_ICON;
         }
      });
      return icon[0];
   }

   public String getLabel(GradientStyle value) {
      final String[] label = new String[1];
      value.accept(new IGradientStyleVisitor() {
         @Override
         public void visitRadial(GradientStyle style) {
            label[0] = "Radial";
         }

         @Override
         public void visitSunburst(GradientStyle style) {
            label[0] = "Sunburst";
         }

         @Override
         public void visitLinear(GradientStyle style) {
            label[0] = "Linear";
         }
      });
      return label[0];
   }
}
