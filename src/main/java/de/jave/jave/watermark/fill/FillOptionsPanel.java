package de.jave.jave.tool.fill;

import de.jave.jave.algorithm.fill.FillMatchMode;
import de.jave.jave.algorithm.fill.FillMode;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.pattern.PatternList;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.preferences.JaveApplicationPreferences;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class FillOptionsPanel implements IComponentContainer {
   private final JComboBox chMatchMode;
   private final JTabbedPane tabbedPane;
   private final JComponent content;

   public FillOptionsPanel(
      final FillOptions options,
      MouseCharacterModel mouseCharacterModel,
      JaveApplicationPreferences applicationPreferences,
      PatternList patternList,
      AsciiGradientConfiguration gradientConfiguration
   ) {
      Ensure.ensureArgumentNotNull(options);
      Ensure.ensureArgumentNotNull(mouseCharacterModel);
      Ensure.ensureArgumentNotNull(applicationPreferences);
      Ensure.ensureArgumentNotNull(patternList);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.chMatchMode = new JComboBox<>(FillMatchMode.values());
      this.chMatchMode.setRenderer(new ObjectUiListCellRenderer(new FillMatchModeUi()));
      this.chMatchMode.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setMatchMode((FillMatchMode)FillOptionsPanel.this.chMatchMode.getSelectedItem());
         }
      });
      this.tabbedPane = new JTabbedPane();
      FillModeUi fillModeUi = new FillModeUi();
      this.tabbedPane.addTab(fillModeUi.getLabel(FillMode.SOLID), new SolidFillOptionsPanel(mouseCharacterModel).getContent());
      this.tabbedPane.addTab(fillModeUi.getLabel(FillMode.PATTERN), new PatternFillOptionsPanel(applicationPreferences, patternList, options).getContent());
      this.tabbedPane
         .addTab(fillModeUi.getLabel(FillMode.GRADIENT), new GradientFillOptionsPanel(mouseCharacterModel, options, gradientConfiguration).getContent());
      this.tabbedPane.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            int index = FillOptionsPanel.this.tabbedPane.getSelectedIndex();
            FillMode mode = FillMode.values()[index];
            options.setFillMode(mode);
         }
      });
      JPanel pMatchMode = new JPanel(new GridDialogLayout(2, false));
      pMatchMode.add(new JLabel("Match Mode:"), GridDialogLayoutData.RIGHT);
      pMatchMode.add(this.chMatchMode, GridDialogLayoutData.FILL_HORIZONTAL);
      JPanel optionsPanel = new JPanel(new GridDialogLayout(1, false));
      optionsPanel.add(this.tabbedPane, GridDialogLayoutData.FILL_BOTH);
      optionsPanel.add(pMatchMode, GridDialogLayoutData.FILL_HORIZONTAL);
      this.content = optionsPanel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
