package de.jave.jave.tool.fill;

import de.jave.jave.AsciiGradientComboBoxFactory;
import de.jave.jave.algorithm.fill.GradientStyle;
import de.jave.jave.algorithm.fill.GradientStyleUi;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.plate.MouseCharacterModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class GradientFillOptionsPanel implements IComponentContainer {
   private final JComponent content;
   private final JCheckBox cbDither;
   private final JComboBox chGradient;
   private final JComboBox gradientComponent;
   private final MouseCharacterModel mouseCharacterModel;

   public GradientFillOptionsPanel(MouseCharacterModel mouseCharacterModel, final FillOptions options, AsciiGradientConfiguration gradientConfiguration) {
      Ensure.ensureArgumentNotNull(mouseCharacterModel);
      Ensure.ensureArgumentNotNull(options);
      this.mouseCharacterModel = mouseCharacterModel;
      this.gradientComponent = AsciiGradientComboBoxFactory.createComponent(gradientConfiguration);
      options.setGradient(this.getSelectedGradient());
      this.gradientComponent.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            options.setGradient(GradientFillOptionsPanel.this.getSelectedGradient());
         }
      });
      this.chGradient = new JComboBox<>(GradientStyle.values());
      this.chGradient.setRenderer(new ObjectUiListCellRenderer(new GradientStyleUi()));
      this.chGradient.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setGradientStyle((GradientStyle)GradientFillOptionsPanel.this.chGradient.getSelectedItem());
         }
      });
      this.cbDither = new JCheckBox("dither", options.isDither());
      this.cbDither.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setDither(GradientFillOptionsPanel.this.cbDither.isSelected());
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("Gradient:"), GridDialogLayoutData.RIGHT);
      panel.add(this.gradientComponent);
      panel.add(new JLabel("Style:"), GridDialogLayoutData.RIGHT);
      panel.add(this.chGradient);
      panel.add(this.cbDither);
      panel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   private char[] getSelectedGradient() {
      String s = (String)this.gradientComponent.getSelectedItem();
      char[] ch = s.toCharArray();
      if (ch.length == 0) {
         ch = new char[]{this.mouseCharacterModel.getCharacter1()};
      }

      return ch;
   }
}
