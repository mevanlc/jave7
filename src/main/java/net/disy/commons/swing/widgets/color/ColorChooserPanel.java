package net.disy.commons.swing.dialog.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class ColorChooserPanel {
   private final Color initialColor;
   private final JPanel panel;
   private final ColorModel colorModel;

   public ColorChooserPanel(ColorModel colorModel, boolean hasTransparencyChangePanel) {
      Ensure.ensureArgumentNotNull(colorModel);
      this.initialColor = colorModel.getColor();
      this.colorModel = colorModel;
      JComponent mainContent = this.createMainContent();
      JPanel additionalControls = this.createAdditionalControls(hasTransparencyChangePanel);
      additionalControls.setBorder(new EmptyBorder(6, 5, 6, 5));
      this.panel = new JPanel();
      this.panel.setLayout(new BorderLayout(LayoutUtilities.getComponentGroupsSpacing(), LayoutUtilities.getComponentGroupsSpacing()));
      this.panel.add(mainContent, "Center");
      this.panel.add(additionalControls, "South");
   }

   private JPanel createAdditionalControls(boolean hasTransparencyChangePanel) {
      GridDialogPanelBuilder builder = new GridDialogPanelBuilder();
      if (hasTransparencyChangePanel) {
         builder.add(new TransparencyBarPanel(this.colorModel));
      }

      builder.add(new ColorPreviewPanel(this.colorModel));
      return builder.createPanel();
   }

   private JComponent createMainContent() {
      final JColorChooser colorChooser = new JColorChooser();
      colorChooser.setColor(this.colorModel.getColor());
      colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent changeEvent) {
            Color plainColor = colorChooser.getColor();
            Color color = new Color(plainColor.getRed(), plainColor.getGreen(), plainColor.getBlue(), ColorChooserPanel.this.colorModel.getColor().getAlpha());
            ColorChooserPanel.this.colorModel.setColor(color);
         }
      });
      this.colorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            colorChooser.setColor(ColorChooserPanel.this.colorModel.getColor());
         }
      });
      JTabbedPane tabPane = new JTabbedPane();
      AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridwidth = 1;
      constraints.gridheight = 1;
      GridBagLayout layout = new GridBagLayout();

      for (int i = 0; i < oldPanels.length; i++) {
         JPanel layoutPanel = new JPanel();
         layoutPanel.setLayout(layout);
         layout.setConstraints(oldPanels[i], constraints);
         layoutPanel.add(oldPanels[i]);
         tabPane.addTab(oldPanels[i].getDisplayName(), layoutPanel);
      }

      return tabPane;
   }

   public JComponent getContent() {
      return this.panel;
   }

   public void resetColor() {
      this.colorModel.setColor(this.initialColor);
   }
}
