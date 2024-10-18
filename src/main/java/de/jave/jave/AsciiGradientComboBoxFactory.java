package de.jave.jave;

import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import javax.swing.JComboBox;

public class AsciiGradientComboBoxFactory {
   public static JComboBox createComponent(AsciiGradientConfiguration configuration) {
      String[] gradients = configuration.getGradients();
      JComboBox comboBox = new JComboBox<>(gradients);
      comboBox.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      comboBox.setEditable(true);
      return comboBox;
   }
}
