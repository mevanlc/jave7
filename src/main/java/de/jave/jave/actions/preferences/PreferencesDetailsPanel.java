package de.jave.jave.actions.preferences;

import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.widgets.HorizontalLine;

public class PreferencesDetailsPanel {
   private static final Font TITLE_FONT = new Font("SansSerif", 1, 13);
   private final JComponent content;

   public PreferencesDetailsPanel(IJavePreferencesPanel preferencesPanel) {
      JPanel detailsPanel = new JPanel(new GridDialogLayout(1, false));
      detailsPanel.add(preferencesPanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      detailsPanel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      GridDialogLayoutData labelData = new GridDialogLayoutData();
      labelData.setHorizontalIndent(LayoutUtilities.getDefaultEmptyBorder().getBorderInsets(panel).left);
      JLabel titleLabel = new JLabel(preferencesPanel.getTitle());
      titleLabel.setFont(TITLE_FONT);
      panel.add(titleLabel, labelData);
      panel.add(new HorizontalLine(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(detailsPanel, GridDialogLayoutData.FILL_BOTH);
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }
}
